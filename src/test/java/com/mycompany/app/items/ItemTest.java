package com.mycompany.app.items;

import com.mycompany.app.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class ItemTest {

    private Food food;
    private Beverage drink;

    @BeforeEach
    void setUp() {
        food = new Food("f1", Money.ofPenny(5000), "food");
        drink = new Beverage(
                "b1", Money.ofPenny(2300),
                "drink", BeverageSize.MEDIUM,
                false, new BeverageDispenser("Drink", 1000));
    }

    @Test
    void itemsStoreCorrectPrice() {
        assertEquals(Money.ofPenny(5000), food.getPrice());
        assertEquals(Money.ofPenny(2300), drink.getPrice());
    }

//    @Test
//    void pricesConvertCorrectlyToCrown() {
//        Item item1 = new Food("f1", Money.ofPenny(5015), "food");
//        Item item2 = new Food("f1", Money.ofPenny(2550), "food");
//        Item item3 = new Food("f3", Money.ofPenny(2399), "food");
//
//        assertAll( () -> {
//            assertEquals(5015, food.getPrice().getPenny());
//            assertEquals(50, item1.getPrice().getCrown());
//
//            assertEquals(2550, item2.getPrice().getPenny());
//            assertEquals(26, item2.getPrice().getCrown());
//
//            assertEquals(2399, item3.getPrice().getPenny());
//            assertEquals(24, item3.getPrice().getCrown());
//        });
//    }

    @Test
    void itemsAreRejectedWithNegativePrices() {
        assertThrows(IllegalArgumentException.class, () ->
                new Food("f1", Money.ofPenny(-200), "food"));
    }

    @Test
    void itemsHaveCorrectProperties() {
        Movie movie = new Movie("move", 120, AgeRequirement.AGE_0);
        Theater theater = new Theater("Harald", 25, 60);
        Seat seat = new Seat('D', 10);
        MovieScreening screening = new MovieScreening(movie, LocalDateTime.of(2026, 12, 12, 18, 18, 0), theater);
        Ticket ticket = new Ticket("m1", Money.ofPenny(500), seat, screening);

        assertEquals("f1", food.getId());
        assertEquals(Money.ofPenny(5000), food.getPrice());
        assertEquals("food", food.getName());

        assertEquals("b1", drink.getId());
        assertEquals(Money.ofPenny(2300), drink.getPrice());
        assertEquals("drink", drink.getName());
        assertEquals(BeverageSize.MEDIUM, drink.getSize());
        assertEquals(AgeRequirement.AGE_0, drink.getAgeReq());

        assertEquals("m1", ticket.getId());
        assertEquals(Money.ofPenny(500), ticket.getPrice());
        assertEquals(seat, ticket.getSeat());
        assertEquals(screening, ticket.getScreening());
    }

    @Test
    void itemsWithSameIdAreEqual() {
        Food food2 = new Food("f1", Money.ofPenny(5000), "Other food");
        Beverage drink2 = new Beverage("b1", Money.ofPenny(3000),
                "Other drink", BeverageSize.SMALL,
                true, new BeverageDispenser("Dispenser", 1000));
        Ticket ticket1 = createTicket("t1", 'E', 9);
        Ticket ticket2 = createTicket("t1", 'C', 16);

        assertAll( () -> {
            assertEquals(food, food2);
            assertEquals(drink, drink2);
            assertEquals(ticket1, ticket2);
        });
    }

    @Test
    void itemsWithoutSameIdAreNotEqual() {
        Food food2 = new Food("f2", Money.ofPenny(5000), "food");
        Beverage drink2 = new Beverage("b2", Money.ofPenny(2300),
                "drink", BeverageSize.MEDIUM,
                false, new BeverageDispenser("Dispenser", 1000));
        Ticket ticket1 = createTicket("t1", 'E', 9);
        Ticket ticket2 = createTicket("t2", 'E', 9);

        assertAll( () -> {
            assertNotEquals(food, food2);
            assertNotEquals(drink, drink2);
            assertNotEquals(ticket1, ticket2);
        });
    }

    private Ticket createTicket(String id, char row, int num) {
        Movie movie = new Movie("move", 120, AgeRequirement.AGE_0);
        Theater theater = new Theater("Harald", 25, 60);
        Seat seat = new Seat(row, num);
        MovieScreening screening = new MovieScreening(movie, LocalDateTime.of(2026, 12, 12, 18, 18, 0), theater);
        return new Ticket(id, Money.ofPenny(500), seat, screening);
    }
}
