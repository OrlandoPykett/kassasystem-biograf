package com.mycompany.app;

import com.mycompany.app.items.Food;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.mycompany.app.items.AgeRequirement;
import com.mycompany.app.items.Beverage;
import com.mycompany.app.items.BeverageSize;
import com.mycompany.app.items.Movie;
import com.mycompany.app.items.MovieScreening;
import com.mycompany.app.items.Seat;
import com.mycompany.app.items.Theater;
import com.mycompany.app.items.Ticket;
import com.mycompany.app.items.BeverageDispenser;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MembershipDiscountCalculatorTest {

    @Test
    void activeMemberGets20KronorDiscountOnFood() {
        Membership membership = new Membership("member1");
        Food food = new Food(
                "f1",
                Money.ofPenny(6000),
                "popcorn"
        );

        MembershipDiscountCalculator calculator = new MembershipDiscountCalculator();
        Money price = calculator.calculatePrice(food, membership);

        assertEquals(4000L, price.getPenny());
    }

    @Test
    void inactiveMemberGetsNoDiscountOnFood() {
    Membership membership = new Membership("member1");
    membership.setActiveStatus(false);

    Food food = new Food(
            "f1",
            Money.ofPenny(6000),
            "popcorn"
    );

    MembershipDiscountCalculator calculator =
            new MembershipDiscountCalculator();

    Money price = calculator.calculatePrice(food, membership);

    assertEquals(6000L, price.getPenny());
}
@Test
void activeMemberGetsNoDiscountOnAlcohol() {
    Membership membership = new Membership("member1");

  BeverageDispenser dispenser =
        new BeverageDispenser("beer", 10000);

Beverage beer = new Beverage(
        "b1",
        Money.ofPenny(6000),
        "beer",
        BeverageSize.MEDIUM,
        true,
        dispenser
);

    MembershipDiscountCalculator calculator =
            new MembershipDiscountCalculator();

    Money price = calculator.calculatePrice(beer, membership);

    assertEquals(6000L, price.getPenny());
}

@Test
void discountCannotMakePriceNegative() {
    Membership membership = new Membership("member1");
    Food food = new Food(
            "f1",
            Money.ofPenny(1500),
            "snack"
    );

    MembershipDiscountCalculator calculator =
            new MembershipDiscountCalculator();

    Money price = calculator.calculatePrice(food, membership);

    assertEquals(0L, price.getPenny());
}

@ParameterizedTest
@CsvSource({
    "0,    0",
    "1999, 0",
    "2000, 0",
    "2001, 1"
})
void discountedFoodPriceNeverFallsBelowZero(
        long originalPenny, long expectedPenny) {

    Membership membership = new Membership("member1");
    Food food = new Food(
            "f1",
            Money.ofPenny(originalPenny),
            "snack"
    );

    MembershipDiscountCalculator calculator =
            new MembershipDiscountCalculator();

    Money price = calculator.calculatePrice(food, membership);

    assertEquals(expectedPenny, price.getPenny());
}

@Test
void adultTicketGetsMembershipDiscountOnceFromBasePrice() {
    Membership membership = new Membership("member1");

    TicketPriceCalculator ticketCalculator =
            new TicketPriceCalculator();

    Money basePrice = ticketCalculator.calculateBasePrice(18);

    Movie movie = new Movie("Test Movie", 120, AgeRequirement.AGE_0);
    Theater theater = new Theater("Harald", 8, 38);

    MovieScreening screening = new MovieScreening(
            movie,
            LocalDateTime.of(2026, 12, 18, 18, 0),
            theater
    );

    Ticket ticket = new Ticket(
            "t1",
            basePrice,
            new Seat('A', 1),
            screening
    );

    MembershipDiscountCalculator discountCalculator =
            new MembershipDiscountCalculator();

    Money finalPrice =
            discountCalculator.calculatePrice(ticket, membership);

    assertEquals(12000L, ticket.getPrice().getPenny());
    assertEquals(10000L, finalPrice.getPenny());
}

@ParameterizedTest
@CsvSource({
    "0,  false, 8000",
    "17, false, 8000",
    "18, false, 12000",
    "64, false, 12000",
    "65, false, 10000",
    "66, false, 10000",
    "0,  true,  6000",
    "17, true,  6000",
    "18, true,  10000",
    "64, true,  10000",
    "65, true,  8000",
    "66, true,  8000"
})
void ticketPriceMatchesAgeAndMembershipStatus(
        int age, boolean active, long expectedPenny) {

    Membership membership = new Membership("member1");
    membership.setActiveStatus(active);

    TicketPriceCalculator ticketCalculator =
            new TicketPriceCalculator();

    Money basePrice = ticketCalculator.calculateBasePrice(age);

    MovieScreening screening = new MovieScreening(
            new Movie("Test Movie", 120, AgeRequirement.AGE_0),
            LocalDateTime.of(2026, 12, 18, 18, 0),
            new Theater("Harald", 8, 38)
    );

    Ticket ticket = new Ticket(
            "t1",
            basePrice,
            new Seat('A', 1),
            screening
    );

    MembershipDiscountCalculator discountCalculator =
            new MembershipDiscountCalculator();

    Money finalPrice =
            discountCalculator.calculatePrice(ticket, membership);

    assertEquals(expectedPenny, finalPrice.getPenny());
}

}
