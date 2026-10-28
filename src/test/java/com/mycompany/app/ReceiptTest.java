package com.mycompany.app;

import com.mycompany.app.items.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.tree.AbstractLayoutCache;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class ReceiptTest {

    private Receipt receipt;

    private final Theater theater = new Theater("Test", 10, 30);
    private final Food popcorn = new Food("F1", Money.ofPenny(7500), "popcorn");
    private final Food nachos = new Food("F3", Money.ofPenny(12500), "nachos");
    private final Movie movie = new Movie("The Godfather", 175,AgeRequirement.AGE_15);
    private final MovieScreening movieScreening =  new MovieScreening(movie, LocalDateTime.of(2026, 12, 7, 18, 30), theater);
    private final Ticket ticket = new Ticket("bla",Money.ofPenny(12000), null,movieScreening);

    @BeforeEach
    void setUp(){
        receipt = new Receipt();
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
    void descriptionOfItemIsIncludedOnReceipt(){
        receipt.addItem(popcorn,"popcorn");
        receipt.format();
    }

    //formating and



    //Receipt with long item names


}
