package com.mycompany.app;

import com.mycompany.app.items.*;
import net.jqwik.api.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;


import static org.junit.jupiter.api.Assertions.*;

public class ReceiptTest {

    private static final Map<String, Food> CATALOG = Map.of(
            "F1", new Food("F1", Money.ofPenny(7500), "popcorn"),
            "F2", new Food ("F2", Money.ofPenny(6500), "baconchips"),
            "F3",new Food("F3", Money.ofPenny(12500), "nachos")
    );

    private static final Clock CLOCK =
            Clock.fixed(Instant.parse("2026-12-01T10:00:00Z"), ZoneOffset.UTC);

    private final Theater theater = new Theater("Test", 10, 30);
    private final Food popcorn = new Food("F1", Money.ofPenny(7500), "popcorn");
    private final Food nachos = new Food("F3", Money.ofPenny(12500), "nachos");
    private final Movie movie = new Movie("The Godfather", 175,AgeRequirement.AGE_15);
    private final MovieScreening movieScreening =  new MovieScreening(movie, LocalDateTime.of(2026, 12, 7, 18, 30), theater);
    private final Ticket ticket = new Ticket("bla",Money.ofPenny(12000), null,movieScreening);

    private Receipt receipt;

    @BeforeEach
    void setUp(){
        receipt = new Receipt(CLOCK);
    }

    @Test
    void newReceiptIsEmpty(){
        assertTrue(receipt.isEmpty());
    }

    @Test
    void newReceiptHasZeroCost(){
        assertEquals(0,receipt.total().getPenny());
    }

    // adding an item, and quantity
    @Test
    void receiptNotEmptyAfterAddingItem(){
        receipt.addItem(popcorn,"popcorn");
        assertFalse(receipt.isEmpty());
    }

    @Test
    void subtotalCorrectForAddedItem(){
        receipt.addItem(popcorn,"popcorn");
        assertEquals(popcorn.getPrice(), receipt.subtotal());
    }

    @Test
    void subtotalCorrectForTwoAddedItems(){
        receipt.addItem(popcorn,"popcorn");
        receipt.addItem(nachos,"nachos");
        assertEquals(popcorn.getPrice().add(nachos.getPrice()),receipt.subtotal());
    }

    @Test
    void noQuantityForItemThatWasNotAdded(){
        assertEquals(0,receipt.countOf(nachos));
    }

    @Test
    void addingSameItemTwiceIncreasesQuantity(){
        receipt.addItem(popcorn,"popcorn");
        receipt.addItem(popcorn,"popcorn");
        assertEquals( 2,receipt.countOf(popcorn));
    }

    @Test
    void sameItemTwiceWithCorrectSubtotal(){
        receipt.addItem(nachos,"nachos");
        receipt.addItem(nachos,"nachos");
        assertEquals(nachos.getPrice().add(nachos.getPrice()), receipt.subtotal());
    }

    //Removing

    @Test
    void removingAnItemOnReceiptRemovesItFromEntries(){
        receipt.addItem(nachos,"nachos");
        assertTrue(receipt.removeItem("F3"));
        assertTrue(receipt.isEmpty());
    }

    @Test
    void removingNonExistentItemReturnsFalse(){
        assertFalse(receipt.removeItem("F3"));
    }

    @Test
    void removingOneItemDoesntAffectOtherItems(){
        receipt.addItem(nachos,"nachos");
        receipt.addItem(popcorn,"popcorn");
        receipt.removeItem("F3");
        assertEquals(1,receipt.countOf(popcorn));
    }

    @Test
    void removingItemGivesCorrectSubtotalForRemainingItems(){
        receipt.addItem(nachos,"nachos");
        receipt.addItem(popcorn,"popcorn");
        receipt.removeItem("F3");
        assertEquals(7500,receipt.subtotal().getPenny());
    }

    //Tickets

    @Test
    void receiptContainsAddedTicket(){
        receipt.addTicket(ticket);
        assertTrue(receipt.hasTicket(ticket));
    }

    @Test
    void receiptDoesNotContainTicket(){
        assertFalse(receipt.hasTicket(ticket));
    }

    @Test
    void ticketPriceCorrectForSubtotal(){
        receipt.addTicket(ticket);
        assertEquals(12000,receipt.subtotal().getPenny());
    }

    @Test
    void ticketCanBeRemovedFromReceipt(){
        receipt.addTicket(ticket);
        receipt.removeItem("bla");
        assertTrue(receipt.isEmpty());
    }

    @Test
    void sameTicketAddedTwice_ThrowsIllegalArgumentException() {
        receipt.addTicket(ticket);
        assertThrows(IllegalArgumentException.class, () ->{receipt.addTicket(ticket);});
    }

    //Discount tests?

    @Test
    void addingItemAfterLockingReceipt_ThrowsIllegalStateException(){
        receipt.lock();
        assertThrows(IllegalStateException.class, ()->{receipt.addItem(popcorn,"popcorn");});
    }

    @Test
    void addingTicketAfterLockingReceipt_ThrowsIllegalStateException(){
        receipt.lock();
        assertThrows(IllegalStateException.class, ()->{receipt.addTicket(ticket);});
    }

    @Test
    void removingItemAfterLockingReceipt_ThrowsIllegalStateException(){
        receipt.addItem(popcorn,"popcorn");
        receipt.lock();
        assertThrows(IllegalStateException.class, ()->{receipt.removeItem("F1");});
    }

    @Test
    void informationCanBeReadAfterLockingReceipt(){
        receipt.addItem(popcorn,"popcorn");
        receipt.lock();
        assertEquals(7500,receipt.subtotal().getPenny());
        assertEquals(1,receipt.countOf(popcorn));
    }

    @Test
    void descriptionOfItemAndTicketIsIncludedOnReceipt(){
        receipt.addTicket(ticket);
        receipt.addItem(popcorn,"popcorn");// en riktig eller mockad Ticket med screening, theater, seat
        String output = receipt.format();
        assertTrue(output.contains(ticket.getScreening().getTheater().getName()));
        assertTrue(output.contains(popcorn.getName()));
    }

    //formating

    //Empty receipt
    @Test
    void firstLineOfReceiptIsCorrect(){
        String[] lines = receipt.format().split("\n");
        assertEquals("=== RECEIPT ===", lines[0]);
    }

    @Test
    void secondLineOfReceiptWithCorrectDate(){
        String[] lines = receipt.format().split("\n");
        assertEquals("2026-12-01 10:00", lines[1]);
    }

    @Test
    void LastLineContainsTotalWithZeroAmount(){
        String[] lines = receipt.format().split("\n");
        assertTrue(lines[lines.length - 1].startsWith("TOTAL"));
    }


    @ParameterizedTest(name = "{0} penny -> {1}")
    @CsvSource(delimiter = '|', value = {
            "0      | 0,00 kr",
            "1      | 0,01 kr",
            "5      | 0,05 kr",
            "100    | 1,00 kr",
            "101    | 1,01 kr",
            "8000   | 80,00 kr",
            "12345  | 123,45 kr"
    })
    void formatsMoneyAsKronorAndOre(long penny, String expected) {
        assertEquals(expected, Receipt.format(Money.ofPenny(penny)));
    }


    @Test
    void getAllItemsReturnsEveryItem(){
        receipt.addItem(popcorn,"popcorn");
        receipt.addTicket(ticket);
        List<Item> list = receipt.getAllItems();
        assertTrue(list.contains(popcorn));
        assertTrue(list.contains(ticket));

    }

    @Test
    void getTicketsReturnsAllTickets(){
        receipt.addTicket(ticket);
        List<Ticket> list = receipt.getTickets();
        assertTrue(list.contains(ticket));
    }

    @Test
    void countOfSkipsTicketsAndNonMatchingItems() {
        receipt.addTicket(ticket);
        receipt.addItem(popcorn, "Popcorn");
        assertEquals(1, receipt.countOf(popcorn));
    }

    @Test
    void moneyIsFormatedInPenny(){
        Money money = Money.ofPenny(1200);
    }


    //Property-based

    @Provide("receiptWithRandomAmountOfFood")
    Arbitrary<List<Food>> receiptWithRandomAmountOfFood() {
        Arbitrary<String> ids = Arbitraries.of("F1", "F2", "F3");

        Arbitrary<Food> catalogItems = ids.map(CATALOG::get);

        return catalogItems.list().ofMinSize(1).ofMaxSize(10);
    }

    @Property
    void subtotalShouldAlwaysSumOfItemCosts(@ForAll("receiptWithRandomAmountOfFood")List<Food> items){
        Receipt receipt = new Receipt(CLOCK);
        for(Food item: items){
            receipt.addItem(item, item.getName());
        }
        long expectedTotal = items.stream()
                .mapToLong(item -> item.getPrice().getPenny())
                .sum();

        assertEquals(receipt.subtotal().getPenny(),expectedTotal);
    }



}
