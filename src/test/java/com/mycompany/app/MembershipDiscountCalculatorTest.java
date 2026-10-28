package com.mycompany.app;

import com.mycompany.app.items.Food;
import com.mycompany.app.membership.Membership;
import com.mycompany.app.membership.MembershipDiscountCalculator;
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
import com.mycompany.app.membership.MembershipTier;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MembershipDiscountCalculatorTest {

    @Test
    void activeMemberGetsFivePercentDiscountOnFood(){
        Membership membership = new Membership("member1");
        Food food = new Food("f1",Money.ofPenny(6000),"popcorn");

        MembershipDiscountCalculator calculator = new MembershipDiscountCalculator();
        Money price = calculator.calculatePrice(food, membership);

        assertEquals(5700L, price.getPenny());
    }

    @Test
    void inactiveMemberGetsNoDiscountOnFood() {
        Membership membership = new Membership("member1");
        membership.setActiveStatus(false);

        Food food = new Food("f1",Money.ofPenny(6000),"popcorn");

    MembershipDiscountCalculator calculator = new MembershipDiscountCalculator();
    Money price = calculator.calculatePrice(food, membership);

    assertEquals(6000L, price.getPenny());
}

@Test
void activeMemberGetsNoDiscountOnAlcohol() {
    Membership membership = new Membership("member1");

        BeverageDispenser dispenser = new BeverageDispenser("beer", 10000);

        Beverage beer = new Beverage("b1",Money.ofPenny(6000),"beer",BeverageSize.MEDIUM,true,dispenser);

    MembershipDiscountCalculator calculator = new MembershipDiscountCalculator();

        Money price = calculator.calculatePrice(beer, membership);

        assertEquals(6000L, price.getPenny());
}

@Test
void cheapFoodKeepsPositivePriceAfterStandardDiscount() {
    Membership membership = new Membership("member1");
    Food food = new Food("f1",Money.ofPenny(1500),"snack");

    MembershipDiscountCalculator calculator = new MembershipDiscountCalculator();

        Money price = calculator.calculatePrice(food, membership);

        assertEquals(1425L, price.getPenny());
}

@ParameterizedTest
@CsvSource({
    "0,    0",
    "1999, 1899",
    "2000, 1900",
    "2001, 1901"
})
void standardDiscountCalculatesCheapFoodPrices(long originalPenny, long expectedPenny) {

    Membership membership = new Membership("member1");
    Food food = new Food("f1",Money.ofPenny(originalPenny),"snack");

    MembershipDiscountCalculator calculator = new MembershipDiscountCalculator();

        Money price = calculator.calculatePrice(food, membership);

        assertEquals(expectedPenny, price.getPenny());
}

@Test
void adultTicketGetsMembershipDiscountOnceFromBasePrice() {
    Membership membership = new Membership("member1");

    TicketPriceCalculator ticketCalculator = new TicketPriceCalculator();

    Money basePrice = ticketCalculator.calculateBasePrice(18);

    Movie movie = new Movie("Test Movie", 120, AgeRequirement.AGE_0);
    Theater theater = new Theater("Harald", 8, 38);

    MovieScreening screening = new MovieScreening( movie, LocalDateTime.of(2026, 12, 18, 18, 0),theater);

    Ticket ticket = new Ticket("t1",basePrice,new Seat('A', 1),screening);

    MembershipDiscountCalculator discountCalculator =new MembershipDiscountCalculator();

    Money finalPrice = discountCalculator.calculatePrice(ticket, membership);

    assertEquals(12000L, ticket.getPrice().getPenny());
    assertEquals(11400L, finalPrice.getPenny());
}

@ParameterizedTest
@CsvSource({
    "0,  false, 8000",
    "17, false, 8000",
    "18, false, 12000",
    "64, false, 12000",
    "65, false, 10000",
    "66, false, 10000",
    "0,  true,  7600",
    "17, true,  7600",
    "18, true,  11400",
    "64, true,  11400",
    "65, true,  9500",
    "66, true,  9500"
})
void ticketPriceMatchesAgeAndMembershipStatus(int age, boolean active, long expectedPenny) {

    Membership membership = new Membership("member1");
    membership.setActiveStatus(active);

    TicketPriceCalculator ticketCalculator = new TicketPriceCalculator();

    Money basePrice = ticketCalculator.calculateBasePrice(age);

    MovieScreening screening = new MovieScreening(new Movie("Test Movie", 120, AgeRequirement.AGE_0),
            LocalDateTime.of(2026, 12, 18, 18, 0), new Theater("Harald", 8, 38));

    Ticket ticket = new Ticket("t1",basePrice,new Seat('A', 1), screening);

    MembershipDiscountCalculator discountCalculator = new MembershipDiscountCalculator();

    Money finalPrice = discountCalculator.calculatePrice(ticket, membership);

    assertEquals(expectedPenny, finalPrice.getPenny());
}

@Test 
void activeStandardMemberGetsFivePercentDiscountOnFood(){

        Membership membership = new Membership("member1");
        Food food = new Food("f1", Money.ofPenny(6000), "popcorn");

        MembershipDiscountCalculator calculator = new MembershipDiscountCalculator();
        Money finalPrice = calculator.calculatePrice(food, membership);

        assertEquals(5700L, finalPrice.getPenny());
}

@Test
void activePremiumMemberGetsTenPercentDiscountOnFood() {
    Membership membership = new Membership("member1");
    membership.updateMembershipTier(MembershipTier.PREMIUM);

    Food food = new Food("f1",Money.ofPenny(6000),"popcorn");

    MembershipDiscountCalculator calculator = new MembershipDiscountCalculator();

    Money finalPrice = calculator.calculatePrice(food, membership);

    assertEquals(5400L, finalPrice.getPenny());
}

@Test
void activeVipMemberGetsFifteenPercentDiscountOnFood() {
    Membership membership = new Membership("member1");
    membership.updateMembershipTier(MembershipTier.VIP);

    Food food = new Food("f1",Money.ofPenny(6000),"popcorn");

    MembershipDiscountCalculator calculator = new MembershipDiscountCalculator();

    Money finalPrice = calculator.calculatePrice(food, membership);

    assertEquals(5100L, finalPrice.getPenny());
}

@Test
void inactiveVipMemberGetsNoDiscount() {
    Membership membership = new Membership("member1");
    membership.updateMembershipTier(MembershipTier.VIP);
    membership.setActiveStatus(false);

    Food food = new Food("f1",Money.ofPenny(6000),"popcorn");

    MembershipDiscountCalculator calculator = new MembershipDiscountCalculator();

    Money finalPrice = calculator.calculatePrice(food, membership);

    assertEquals(6000L, finalPrice.getPenny());
}

@ParameterizedTest
@CsvSource({
    "STANDARD, 9,  9",
    "STANDARD, 10, 10",
    "STANDARD, 11, 10",
    "PREMIUM,  4,  4",
    "PREMIUM,  5,  5",
    "PREMIUM,  6,  5",
    "VIP,      9,  8",
    "VIP,      10, 9",
    "VIP,      11, 9"
})
void finalPriceRoundsToNearestPenny(MembershipTier tier,long originalPenny,long expectedPenny) {

    Membership membership = new Membership("member1");
    membership.updateMembershipTier(tier);

    Food food = new Food("f1",Money.ofPenny(originalPenny),"snack");

    MembershipDiscountCalculator calculator =new MembershipDiscountCalculator();

    Money finalPrice = calculator.calculatePrice(food, membership);

    assertEquals(expectedPenny, finalPrice.getPenny());
}

@Test
void customerWithoutMembershipPaysOriginalPrice() {
    Food food = new Food("f1",Money.ofPenny(6000),"popcorn");

    MembershipDiscountCalculator calculator = new MembershipDiscountCalculator();

    Money finalPrice = calculator.calculatePrice(food, null);

    assertEquals(6000L, finalPrice.getPenny());
}

@Test
void childVipPriceCombinesMovieSurchargeAndDiscount() {
    Movie movie = new Movie("Long Movie",160,AgeRequirement.AGE_0,Money.ofPenny(15000));

    TicketPriceCalculator ticketCalculator =new TicketPriceCalculator();

    Money ticketPrice = ticketCalculator.calculateBasePrice(17, movie);

    MovieScreening screening = new MovieScreening(movie,LocalDateTime.of(2026, 12, 18, 18, 0),new Theater("Test Theater", 8, 38));
    Ticket ticket = new Ticket("t1",ticketPrice,new Seat('A', 1),screening);

    Membership membership = new Membership("member1");
    membership.updateMembershipTier(MembershipTier.VIP);

    MembershipDiscountCalculator discountCalculator = new MembershipDiscountCalculator();

    Money finalPrice = discountCalculator.calculatePrice(ticket, membership);

    assertEquals(15000L, movie.getBasePrice().getPenny());
    assertEquals(13000L, ticket.getPrice().getPenny());
    assertEquals(11050L, finalPrice.getPenny());
}

}
