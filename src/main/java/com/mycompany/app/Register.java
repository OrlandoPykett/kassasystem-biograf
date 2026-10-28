package com.mycompany.app;

import com.mycompany.app.initialisation.CinemaInitialiser;
import com.mycompany.app.items.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Register {

    private enum State {IDLING, PURCHASING, PAID }

    private static final String EXIT = "exit";
    private static final String NEW_CUSTOMER = "new customer";
    private static final String ADD = "add";
    private static final String REMOVE = "remove";
    private static final String SCREENINGS = "screenings";
    private static final String TICKET = "ticket";
    private static final String SHOW = "show";
    private static final String PAY_CARD = "pay card";
    private static final String PRINT = "print";
    private static final String CANCEL = "cancel";
    private static final String HELP = "help";

    /////
    private static final Money TICKET_PRICE = Money.ofPenny(12000);
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    /////

    private final ScreeningManager screeningManager;
    private final TicketManager ticketManager;
    private final StockManager stock;
    private final InputReader input;

    private final Map<String, Item> items = new HashMap<>();

    private Receipt receipt;
    private Integer customerAge;
    private State state = State.IDLING;
    private boolean running = true;

    public Register(InputReader input, StockManager stock, TicketManager ticketManager,
                    Collection<Item> items, ScreeningManager screeningManager) {
        this.input = input;
        this.stock = stock;
        this.ticketManager = ticketManager;
        this.screeningManager = screeningManager;
        for (Item item : items) {
            this.items.put(item.getId().toLowerCase(), item);
        }
    }

    protected void start() {
        setUp();
        runCommandLoop();
        shutDown();
    }

    private void setUp() {
        printMenu();
    }

    private void runCommandLoop() {
        while (running) {
            executeCommand(readCommand());
        }
    }

    private void shutDown() {
        System.out.println("Register closed.");
    }

    private String readCommand() {
        return input.readString("Enter command");
    }

    void executeCommand(String command) {
        switch (command.toLowerCase().trim()) {
            case NEW_CUSTOMER:
            case "nc":
                newCustomerCommand();
                break;
            case ADD:
            case "a":
                addCommand();
                break;
            case REMOVE:
            case "r":
                removeCommand();
                break;
            case SCREENINGS:
            case "sc":
                screeningsCommand();
                break;
            case TICKET:
            case "t":
                ticketCommand();
                break;
            case SHOW:
            case "s":
                showCommand();
                break;
            case PAY_CARD:
            case "pc":
                payCardCommand();
                break;
            case PRINT:
            case "p":
                printCommand();
                break;                      // without this break, print would fall into cancel
            case CANCEL:
            case "c":
                cancelCommand();
                break;
            case HELP:
            case "h":
                printMenu();
                break;
            case EXIT:
            case "e":
                exitCommand();
                break;
            default:
                printErrorMessage("Error, wrong command. Try again!");
        }
    }

    private void exitCommand(){
        if (state == State.PURCHASING){
            printErrorMessage("Cant exit with an ongoing purchase");
            return;
        }
        running = false;
    }

    private void newCustomerCommand(){
        if (state == State.PURCHASING) {
            printErrorMessage("A purchase is already in progress. Pay or cancel first.");
            return;
        }
        customerAge = null;
        receipt = new Receipt();
        state = State.PURCHASING;
        System.out.println("New customer. Receipt created.");
    }

    private void addCommand() {
        if (!currentlyShopping()) return;
        String id = input.readString("Item id");
        Item item = items.get(id.toLowerCase());
        if (item == null) {
            printErrorMessage("Unknown item id: " + id);
            return;
        }
        // The stock is only reduced when the customer pays, so count what is already on the receipt
        if (stock.getStock(item) <= receipt.countOf(item)) {
            printErrorMessage(describe(item) + " is out of stock.");
            return;
        }
        if (!isOfAge(ageRequirementOf(item))) {
            printErrorMessage("Customer is too young for " + describe(item) + ".");
            return;
        }
        receipt.addItem(item, describe(item));
        System.out.println("Added " + describe(item) + ". Subtotal: " + Receipt.format(receipt.total()));
    }

    private void removeCommand() {
        if (!currentlyShopping()) return;
        String id = input.readString("Item id");
        if (receipt.removeItem(id)) {
            System.out.println("Removed " + id + ".");
        } else {
            printErrorMessage("No item with id " + id + " on the receipt.");
        }
    }

    private void screeningsCommand() {
        for (int i = 0; i < screeningManager.getScreenings().size(); i++) {
            MovieScreening s = screeningManager.getScreenings().get(i);
            int minAge = s.getMovie().getAgeReq().getMinimumAge();
            System.out.printf("%2d. %-16s %s  %-8s %s%n",
                    i + 1, s.getMovie().getTitle(), s.getStartTime().format(TIME_FORMAT),
                    s.getTheater().getName(), minAge == 0 ? "all ages" : minAge + "+");
        }
    }

    private void ticketCommand() {
        if (!currentlyShopping()) return;
        String number = input.readString("Screening number (see 'screenings')");
        MovieScreening screening;
        try {
            screening = screeningManager.getScreenings().get(Integer.parseInt(number) - 1);
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            printErrorMessage("No screening with number " + number);
            return;
        }
        AgeRequirement required = screening.getMovie().getAgeReq();
        if (!isOfAge(required)) {
            printErrorMessage("Customer must be at least " + required.getMinimumAge()
                    + " to see " + screening.getMovie().getTitle() + ".");
            return;
        }
        String seatText = input.readString("Seat (e.g. A5)");
        Seat seat = parseSeat(seatText);
        if (seat == null || !screening.getTheater().hasSeat(seat)) {
            printErrorMessage("No such seat: " + seatText);
            return;
        }
        Ticket ticket = new Ticket(ticketId(screening, seat), TICKET_PRICE, seat, screening);
        if (!ticketManager.seatIsEmpty(screening, seat) || receipt.hasTicket(ticket)) {
            printErrorMessage("Seat " + seat + " is already taken.");
            return;
        }
        receipt.addTicket(ticket);
        System.out.println("Added ticket " + ticket.getId() + " (" + ticket.getName() + ", seat " + seat
                + "). Subtotal: " + Receipt.format(receipt.total()));
    }

    private void showCommand() {
        if (state == State.IDLING) {
            printErrorMessage("No purchase to show. Type 'new customer'.");
            return;
        }
        System.out.println(receipt.format());
    }

    private void printCommand() {
        if (state != State.PAID) {
            printErrorMessage("You can only print the receipt after payment.");
            return;
        }
        // TODO: send to a Printer interface so a mock can be used in tests
        System.out.println(receipt.format());
    }

    private void payCardCommand(){
        if (!currentlyShopping()) return;
        if (receipt.isEmpty()) {
            printErrorMessage("The receipt is empty, nothing to pay.");
            return;
        }
        // TODO: Payment interface with a fake card terminal for tests
        if (!completePurchase()) return;
        System.out.println("Card payment approved: " + Receipt.format(receipt.total()));

    }

    private void cancelCommand(){
        if (state != State.PURCHASING) {
            printErrorMessage("No purchase to cancel.");
            return;
        }
        receipt = null;     // nothing was taken from stock or booked yet, so there is nothing to undo
        state = State.IDLING;
        System.out.println("Purchase cancelled.");
    }

    private boolean completePurchase() {
        for (Map.Entry<Item, Integer> entry : receipt.getItemQuantities().entrySet()) {
            if (stock.getStock(entry.getKey()) < entry.getValue()) {
                printErrorMessage(describe(entry.getKey()) + " is no longer in stock. Remove it and try again.");
                return false;
            }
        }
        for (Ticket ticket : receipt.getTickets()) {
            if (!ticketManager.seatIsEmpty(ticket.getScreening(), ticket.getSeat())) {
                printErrorMessage("Seat " + ticket.getSeat() + " is no longer free. Remove ticket "
                        + ticket.getId() + " and try again.");
                return false;
            }
        }

        for (Map.Entry<Item, Integer> entry : receipt.getItemQuantities().entrySet()) {
            stock.removeStock(entry.getKey(), entry.getValue());
        }
        for (Ticket ticket : receipt.getTickets()) {
            ticketManager.addTicket(ticket);
        }
        receipt.lock();
        state = State.PAID;
        return true;
    }

    //helpers
    private boolean currentlyShopping() {
        if (state == State.IDLING) {
            printErrorMessage("No purchase in progress");
            return false;
        }
        if (state == State.PAID) {
            printErrorMessage("Already paid, no purchase in progress.");
            return false;
        }
        return true;
    }

    private boolean isOfAge(AgeRequirement required) {
        if (required == AgeRequirement.AGE_0) return true;
        if (customerAge == null) customerAge = askAge();
        return required.isOfAge(customerAge);
    }

    private int askAge() {
        while (true) {
            String text = input.readString("Customer age");
            try {
                int age = Integer.parseInt(text);
                if (age >= 0 && age <= 130) return age;
            } catch (NumberFormatException e) {
                // fall through to the error message
            }
            printErrorMessage("Not a valid age: " + text);
        }
    }

    private AgeRequirement ageRequirementOf(Item item) {
        if (item instanceof Beverage beverage) return beverage.getAgeReq();
        return AgeRequirement.AGE_0;
    }

    private String describe(Item item) {
        if (item instanceof Beverage beverage) {
            return beverage.getName() + " (" + beverage.getSize().name().toLowerCase() + ")";
        }
        return item.getName();
    }

    private String ticketId(MovieScreening screening, Seat seat) {
        return "T" + (screeningManager.getScreenings().indexOf(screening) + 1) + "-" + seat;
    }


    private Seat parseSeat(String text) {
        String t = text.trim().toUpperCase();
        if (t.length() < 2) return null;
        try {
            return new Seat(t.charAt(0), Integer.parseInt(t.substring(1)));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /** "100" or "99,50" (crowns) -> Money. */
    private Money parseMoney(String text) {
        long penny = new BigDecimal(text.trim().replace(',', '.'))
                .movePointRight(2).setScale(0, RoundingMode.HALF_UP).longValueExact();
        return Money.ofPenny(penny);
    }

    private void printErrorMessage(String message) {
        System.out.println(message);
    }

    private void printMenu() {
        System.out.println("Cinema register");
        System.out.println("New customer (nc) | Add (a) | Remove (r) | Screenings (sc) | Ticket (t) | Show (s)");
        System.out.println("Pay card (pc)  | Print (p) | Cancel (c) | Help (h) | Exit (e)");
    }

    public static void main(String[] args) {
        CinemaInitialiser initialiser = new CinemaInitialiser();
        StockManager stock = new StockManager();
        initialiser.populateInventory(stock);

        Register register = new Register(new InputReader(), stock, new TicketManager(),
                initialiser.getItems(), new ScreeningManager());
        register.start();
    }

}
