package com.mycompany.app.items;

import com.mycompany.app.Money;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class ItemTest {

    @Test
    void itemsStoreCorrectPrice() {
        Item food = new Food("f1", Money.ofPenny(5000), "food");
        Item drink = new Beverage("b1", Money.ofPenny(2300), "drink", BeverageSize.MEDIUM, false);

        assertEquals(Money.ofPenny(5000), food.getPrice());
        assertEquals(Money.ofPenny(2300), drink.getPrice());
    }

    @Test
    void pricesConvertCorrectlyToCrown() {
        Item food1 = new Food("f1", Money.ofPenny(5015), "food");
        Item drink = new Beverage("b1", Money.ofPenny(2399), "drink", BeverageSize.MEDIUM, false);
        Item food2 = new Food("f1", Money.ofPenny(2550), "food");

        assertEquals(5015, food1.getPrice().getPenny());
        assertEquals(50, food1.getPrice().getCrown());

        assertEquals(2399, drink.getPrice().getPenny());
        assertEquals(24, drink.getPrice().getCrown());

        assertEquals(2550, food2.getPrice().getPenny());
        assertEquals(26, food2.getPrice().getCrown());
    }

    @Test
    void itemsAreRejectedWithNegativePrices() {
        assertThrows(IllegalArgumentException.class, () ->
                new Food("f1", Money.ofPenny(-200), "food"));
    }

    @Test
    void itemsHaveCorrectProperties() {
        Food food = new Food("f1", Money.ofPenny(500), "food");
        Beverage drink = new Beverage("b1", Money.ofPenny(500), "drink", BeverageSize.MEDIUM, false);

        Movie movie = new Movie("move", 120, AgeRequirement.AGE_0);
        MovieScreening screening = new MovieScreening(movie, LocalDateTime.of(2026, 12, 12, 18, 18, 0), "Harald");
        Ticket ticket = new Ticket("m1", Money.ofPenny(500), 12, screening);

        assertEquals("f1", food.getId());
        assertEquals(Money.ofPenny(500), food.getPrice());
        assertEquals("food", food.getName());

        assertEquals("b1", drink.getId());
        assertEquals(Money.ofPenny(500), drink.getPrice());
        assertEquals("drink", drink.getName());
        assertEquals(BeverageSize.MEDIUM, drink.getSize());
        assertEquals(AgeRequirement.AGE_0, drink.getAgeReq());

        assertEquals("m1", ticket.getId());
        assertEquals(Money.ofPenny(500), ticket.getPrice());
        assertEquals(12, ticket.getSeat());
        assertEquals(screening, ticket.getScreening());
    }
}
