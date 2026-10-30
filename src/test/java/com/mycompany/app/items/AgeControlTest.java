package com.mycompany.app.items;

import com.mycompany.app.Money;
import com.mycompany.app.items.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AgeControlTest {

    // -------------------------------------------------------
    // isAccepted(int age, AgeRequirement ageReq)
    // -------------------------------------------------------

    @Test
    void acceptsAgeAboveMinimumRequirement() {
        assertTrue(AgeControl.isAccepted(20, AgeRequirement.AGE_18));
    }

    @Test
    void acceptsAgeExactlyAtMinimumRequirement() {
        assertTrue(AgeControl.isAccepted(18, AgeRequirement.AGE_18));
    }

    @Test
    void rejectsAgeBelowMinimumRequirement() {
        assertFalse(AgeControl.isAccepted(17, AgeRequirement.AGE_18));
    }

    @Test
    void acceptsAnyNonNegativeAgeForAge0Requirement() {
        assertTrue(AgeControl.isAccepted(0, AgeRequirement.AGE_0));
    }

    @Test
    void acceptsAgeForAge0Requirement() {
        assertTrue(AgeControl.isAccepted(25, AgeRequirement.AGE_0));
    }

    // -------------------------------------------------------
    // isAccepted(int age, Item item)
    // -------------------------------------------------------

    @Test
    void acceptsAdultForAgeRestrictedBeverage() {
        Beverage beverage = alcoholicBeverage();

        assertTrue(AgeControl.isAccepted(18, beverage));
    }

    @Test
    void rejectsUnderageCustomerForAgeRestrictedBeverage() {
        Beverage beverage = alcoholicBeverage();

        assertFalse(AgeControl.isAccepted(17, beverage));
    }

    @Test
    void acceptsUnderageCustomerForNonAgeRestrictedBeverage() {
        Beverage beverage = nonAlcoholicBeverage();

        assertTrue(AgeControl.isAccepted(10, beverage));
    }

    @Test
    void acceptsAdultForAgeRestrictedTicket() {
        Ticket ticket = ageRestrictedTicket();

        assertTrue(AgeControl.isAccepted(18, ticket));
    }

    @Test
    void rejectsUnderageCustomerForAgeRestrictedTicket() {
        Ticket ticket = ageRestrictedTicket();

        assertFalse(AgeControl.isAccepted(17, ticket));
    }

    @Test
    void acceptsUnderageCustomerForAllAgesTicket() {
        Ticket ticket = allAgesTicket();

        assertTrue(AgeControl.isAccepted(10, ticket));
    }

    @Test
    void acceptsUnderageCustomerForFoodItem() {
        Item food = foodItem();

        assertTrue(AgeControl.isAccepted(10, food));
    }

    @Test
    void acceptsAdultForFoodItem() {
        Item food = foodItem();

        assertTrue(AgeControl.isAccepted(30, food));
    }

    // -------------------------------------------------------
    // Test object creation
    // -------------------------------------------------------

    private Beverage alcoholicBeverage() {
        return new Beverage(
                "TEST-BEER",
                Money.ofPenny(5000),
                "Test beer",
                BeverageSize.MEDIUM,
                true,
                null
        );
    }

    private Beverage nonAlcoholicBeverage() {
        return new Beverage(
                "TEST-COKE",
                Money.ofPenny(2000),
                "Test coke",
                BeverageSize.MEDIUM,
                false,
                null
        );
    }

    private Ticket ageRestrictedTicket() {
        Movie movie = new Movie(
                "Test movie",
                120,
                AgeRequirement.AGE_18
        );

        MovieScreening screening = new MovieScreening(
                movie,
                java.time.LocalDateTime.now(),
                new Theater("Test theater", 12, 12)
        );

        return new Ticket(
                "TEST-TICKET",
                Money.ofPenny(12000),
                new Seat('A', 1),
                screening
        );
    }

    private Ticket allAgesTicket() {
        Movie movie = new Movie(
                "Test movie",
                90,
                AgeRequirement.AGE_0
        );

        MovieScreening screening = new MovieScreening(
                movie,
                java.time.LocalDateTime.now(),
                new Theater("Test theater", 10, 10)
        );

        return new Ticket(
                "TEST-TICKET",
                Money.ofPenny(12000),
                new Seat('B', 2),
                screening
        );
    }

    private Item foodItem() {
        return new Food("TEST-FOOD",  Money.ofPenny(7500), "Test popcorn");
    }
}
