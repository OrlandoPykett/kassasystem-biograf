package com.mycompany.app;

import com.mycompany.app.initialisation.CinemaInitialiser;
import com.mycompany.app.items.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.*;

class RegisterTest {

    /** Fake input: returns prepared answers in order, fails if the register asks for more. */
    private static class FakeInput extends InputReader {
        private final Deque<String> answers = new ArrayDeque<>();

        void answer(String... values) {
            answers.addAll(List.of(values));
        }

        @Override
        public String readString(String prompt) {
            if (answers.isEmpty()) {
                throw new IllegalStateException("No prepared answer for: " + prompt);
            }
            return answers.poll();
        }
    }

    private final ByteArrayOutputStream out = new ByteArrayOutputStream();
    private PrintStream originalOut;

    private FakeInput input;
    private StockManager stock;
    private TicketManager ticketManager;
    private ScreeningManager screeningManager;
    private List<Item> items;
    private Register register;

    @BeforeEach
    void setUp() {
        originalOut = System.out;
        System.setOut(new PrintStream(out, true, UTF_8));

        CinemaInitialiser initialiser = new CinemaInitialiser();
        stock = new StockManager();
        initialiser.populateInventory(stock);
        items = new ArrayList<>(initialiser.getItems());

        input = new FakeInput();
        ticketManager = new TicketManager();
        screeningManager = new ScreeningManager();
        initialiser.populateMovies(screeningManager);
        register = new Register(input, stock, ticketManager, items, screeningManager);
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    // ---------- helpers ----------

    private String output() {
        return out.toString(UTF_8);
    }

    private void assertOutputContains(String text) {
        assertTrue(output().contains(text), () -> "Expected output to contain '" + text + "' but was:\n" + output());
    }

    private void cmd(String command) {
        register.executeCommand(command);
    }

    private Item plainItem() {
        return items.stream().filter(i -> !(i instanceof Beverage)).findFirst().orElseThrow();
    }

    private Item adultBeverage() {
        return items.stream()
                .filter(i -> i instanceof Beverage b && b.getAgeReq() == AgeRequirement.AGE_18)
                .findFirst().orElseThrow();
    }

    /** 1-based screening number as typed by the user. */
    private int screeningNumber(boolean ageRestricted) {
        List<MovieScreening> list = screeningManager.getScreenings();
        for (int i = 0; i < list.size(); i++) {
            boolean restricted = list.get(i).getMovie().getAgeReq() != AgeRequirement.AGE_0;
            if (restricted == ageRestricted) {
                return i + 1;
            }
        }
        throw new IllegalStateException("No screening with ageRestricted=" + ageRestricted);
    }

    private MovieScreening screening(int number) {
        return screeningManager.getScreenings().get(number - 1);
    }

    /** Finds a seat text like "A1" that exists in the screening's theater. */
    private String seatText(MovieScreening screening) {
        for (char row = 'A'; row <= 'Z'; row++) {
            for (int number = 1; number <= 30; number++) {
                if (screening.getTheater().hasSeat(new Seat(row, number))) {
                    return "" + row + number;
                }
            }
        }
        throw new IllegalStateException("No seat found");
    }

    private Seat seatOf(String text) {
        return new Seat(text.charAt(0), Integer.parseInt(text.substring(1)));
    }

    /** Starts a customer and adds a ticket for an all-ages screening. Returns the seat text. */
    private String newCustomerWithTicket() {
        int number = screeningNumber(false);
        String seat = seatText(screening(number));
        cmd("nc");
        input.answer(String.valueOf(number), seat);
        cmd("ticket");
        return seat;
    }

    // ---------- command dispatch ----------

    @ParameterizedTest
    @ValueSource(strings = {"new customer", "nc", "add", "a", "remove", "r", "screenings", "sc",
            "ticket", "t", "show", "s", "pay card", "pc", "print", "p", "cancel", "c",
            "help", "h", "exit", "e"})
    void everyCommandAliasIsRecognised(String command) {
        register.executeCommand(command);
        assertFalse(output().contains("wrong command"));
    }

    @Test
    void unknownCommandPrintsError() {
        cmd("banana");
        assertOutputContains("Error, wrong command. Try again!");
    }

    @Test
    void commandsIgnoreCaseAndSurroundingSpaces() {
        cmd("  HELP ");
        assertOutputContains("Cinema register");
    }

    // ---------- start / exit / main ----------

    @Test
    void startRunsUntilExitAndCannotExitDuringPurchase() {
        input.answer("nc", "exit", "cancel", "exit");
        register.start();
        assertOutputContains("Cant exit with an ongoing purchase");
        assertOutputContains("Purchase cancelled.");
        assertOutputContains("Register closed.");
    }

    @Test
    void mainStartsRegisterAndExits() {
        InputStream originalIn = System.in;
        try {
            System.setIn(new ByteArrayInputStream("exit\n".getBytes(UTF_8)));
            Register.main(new String[0]);
        } finally {
            System.setIn(originalIn);
        }
        assertOutputContains("Register closed.");
    }

    // ---------- new customer / cancel / show / print ----------

    @Test
    void newCustomerCreatesReceipt() {
        cmd("nc");
        assertOutputContains("New customer. Receipt created.");
    }

    @Test
    void newCustomerWhilePurchasingIsRejected() {
        cmd("nc");
        cmd("nc");
        assertOutputContains("A purchase is already in progress");
    }

    @Test
    void cancelWithoutPurchaseIsRejected() {
        cmd("cancel");
        assertOutputContains("No purchase to cancel.");
    }

    @Test
    void cancelEndsPurchase() {
        cmd("nc");
        cmd("cancel");
        assertOutputContains("Purchase cancelled.");
    }

    @Test
    void showWithoutPurchaseIsRejected() {
        cmd("show");
        assertOutputContains("No purchase to show.");
    }

    @Test
    void showPrintsReceiptDuringPurchase() {
        cmd("nc");
        cmd("show");
        assertOutputContains("=== RECEIPT ===");
    }

    @Test
    void printBeforePaymentIsRejected() {
        cmd("nc");
        cmd("print");
        assertOutputContains("You can only print the receipt after payment.");
    }

    // ---------- screenings ----------

    @Test
    void screeningsListsAllAgesAndAgeRestrictedMovies() {
        cmd("screenings");
        assertOutputContains("all ages");
        assertOutputContains("+");
    }

    // ---------- add ----------

    @Test
    void addWithoutPurchaseIsRejected() {
        cmd("add");
        assertOutputContains("No purchase in progress");
    }

    @Test
    void addUnknownItemIsRejected() {
        cmd("nc");
        input.answer("nope");
        cmd("add");
        assertOutputContains("Unknown item id: nope");
    }

    @Test
    void addPlainItemIgnoresCaseOfId() {
        Item item = plainItem();
        cmd("nc");
        input.answer(item.getId().toUpperCase());
        cmd("add");
        assertOutputContains("Added ");
        assertOutputContains("Subtotal:");
    }

    @Test
    void addOutOfStockItemIsRejected() {
        Item item = plainItem();
        stock.removeStock(item, stock.getStock(item));
        cmd("nc");
        input.answer(item.getId());
        cmd("add");
        assertOutputContains("is out of stock.");
    }

    @Test
    void addAgeRestrictedItemAsTooYoungCustomerIsRejectedAfterInvalidAges() {
        Item item = adultBeverage();
        cmd("nc");
        input.answer(item.getId(), "abc", "-1", "200", "0");
        cmd("add");
        assertOutputContains("Not a valid age: abc");
        assertOutputContains("Not a valid age: -1");
        assertOutputContains("Not a valid age: 200");
        assertOutputContains("Customer is too young for");
    }

    @Test
    void addAgeRestrictedItemAsAdultSucceeds() {
        Item item = adultBeverage();
        cmd("nc");
        input.answer(item.getId(), "30");
        cmd("add");
        assertOutputContains("Added ");
    }

    // ---------- remove ----------

    @Test
    void removeWithoutPurchaseIsRejected() {
        cmd("remove");
        assertOutputContains("No purchase in progress");
    }

    @Test
    void removeExistingAndMissingItem() {
        Item item = plainItem();
        cmd("nc");
        input.answer(item.getId());
        cmd("add");

        input.answer(item.getId());
        cmd("remove");
        assertOutputContains("Removed " + item.getId() + ".");

        input.answer(item.getId());
        cmd("remove");
        assertOutputContains("No item with id " + item.getId() + " on the receipt.");
    }

    // ---------- ticket ----------

    @Test
    void ticketWithoutPurchaseIsRejected() {
        cmd("ticket");
        assertOutputContains("No purchase in progress");
    }

    @Test
    void ticketWithInvalidScreeningNumberIsRejected() {
        cmd("nc");
        input.answer("abc");
        cmd("ticket");
        input.answer("0");
        cmd("ticket");
        input.answer("999");
        cmd("ticket");
        assertOutputContains("No screening with number abc");
        assertOutputContains("No screening with number 0");
        assertOutputContains("No screening with number 999");
    }

    @Test
    void ticketForAgeRestrictedMovieAsTooYoungCustomerIsRejected() {
        cmd("nc");
        input.answer(String.valueOf(screeningNumber(true)), "0");
        cmd("ticket");
        assertOutputContains("Customer must be at least");
    }

    @Test
    void ticketWithInvalidSeatIsRejected() {
        int number = screeningNumber(false);
        cmd("nc");
        input.answer(String.valueOf(number), "A");      // too short
        cmd("ticket");
        input.answer(String.valueOf(number), "Axx");    // not a number
        cmd("ticket");
        input.answer(String.valueOf(number), "Z99");    // well formed but not in theater
        cmd("ticket");
        assertOutputContains("No such seat: A");
        assertOutputContains("No such seat: Axx");
        assertOutputContains("No such seat: Z99");
    }

    @Test
    void ticketForAllAgesMovieIsAdded() {
        newCustomerWithTicket();
        assertOutputContains("Added ticket ");
    }

    @Test
    void ticketForAgeRestrictedMovieReusesAlreadyKnownAge() {
        Item item = adultBeverage();
        cmd("nc");
        input.answer(item.getId(), "30");               // age is asked here
        cmd("add");

        int number = screeningNumber(true);
        input.answer(String.valueOf(number), seatText(screening(number))); // age is not asked again
        cmd("ticket");
        assertOutputContains("Added ticket ");
    }

    @Test
    void sameSeatTwiceOnSameReceiptIsRejected() {
        int number = screeningNumber(false);
        String seat = newCustomerWithTicket();
        input.answer(String.valueOf(number), seat);
        cmd("ticket");
        assertOutputContains("is already taken.");
    }

    @Test
    void seatBoughtByEarlierCustomerIsRejected() {
        int number = screeningNumber(false);
        String seat = newCustomerWithTicket();
        cmd("pay card");

        cmd("nc");
        input.answer(String.valueOf(number), seat);
        cmd("ticket");
        assertOutputContains("is already taken.");
    }

    // ---------- pay card / print ----------

    @Test
    void payWithoutPurchaseIsRejected() {
        cmd("pay card");
        assertOutputContains("No purchase in progress");
    }

    @Test
    void payWithEmptyReceiptIsRejected() {
        cmd("nc");
        cmd("pay card");
        assertOutputContains("The receipt is empty, nothing to pay.");
    }

    @Test
    void payCompletesPurchaseUpdatesStockAndTickets() {
        Item item = plainItem();
        int stockBefore = stock.getStock(item);

        newCustomerWithTicket();
        input.answer(item.getId());
        cmd("add");
        cmd("pay card");

        assertOutputContains("Card payment approved:");
        assertEquals(stockBefore - 1, stock.getStock(item));
        assertEquals(1, ticketManager.getTickets().size());
    }

    @Test
    void printAfterPaymentShowsReceiptAndFurtherAddIsRejected() {
        newCustomerWithTicket();
        cmd("pay card");

        cmd("print");
        assertOutputContains("=== RECEIPT ===");

        cmd("add");
        assertOutputContains("Already paid, no purchase in progress.");
    }

    @Test
    void payFailsWhenItemRanOutOfStockMeanwhile() {
        Item item = plainItem();
        cmd("nc");
        input.answer(item.getId());
        cmd("add");
        stock.removeStock(item, stock.getStock(item));

        cmd("pay card");
        assertOutputContains("is no longer in stock.");
    }

    @Test
    void payFailsWhenSeatWasBookedMeanwhile() {
        int number = screeningNumber(false);
        String seat = newCustomerWithTicket();
        ticketManager.purchaseTicket(
                new Ticket("X", Money.ofPenny(100), seatOf(seat), screening(number)), 99);

        cmd("pay card");
        assertOutputContains("is no longer free.");
    }
}
