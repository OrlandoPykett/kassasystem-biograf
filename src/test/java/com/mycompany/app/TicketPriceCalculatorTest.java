package com.mycompany.app;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.mycompany.app.items.Beverage;
import com.mycompany.app.items.BeverageDispenser;
import com.mycompany.app.items.BeverageSize;
import com.mycompany.app.items.Movie;
import com.mycompany.app.items.AgeRequirement;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.constraints.IntRange;
import net.jqwik.api.constraints.LongRange;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TicketPriceCalculatorTest {

    @Test
    void adultTicketCosts120Kronor() {
        TicketPriceCalculator calculator = new TicketPriceCalculator();

        Money price = calculator.calculateBasePrice(18);

        assertEquals(12000L, price.getPenny());
    }

    @Test
    void childTicketCosts80Kronor() {
        TicketPriceCalculator calculator = new TicketPriceCalculator();

        Money price = calculator.calculateBasePrice(17);

        assertEquals(8000L, price.getPenny());
    }

    @Test
    void seniorTicketCosts100Kronor() {
        TicketPriceCalculator calculator = new TicketPriceCalculator();

        Money price = calculator.calculateBasePrice(65);

        assertEquals(10000L, price.getPenny());
    }

    @Test
    void negativeAgeIsRejected() {
        TicketPriceCalculator calculator = new TicketPriceCalculator();

        assertThrows(IllegalArgumentException.class,() -> calculator.calculateBasePrice(-1)
        );
    }

    @ParameterizedTest
    @CsvSource({
        "0,  8000",
        "17, 8000",
        "18, 12000",
        "64, 12000",
        "65, 10000",
        "66, 10000"
})
    void basePriceMatchesAgeGroup(int age, long expectedPenny) {
        TicketPriceCalculator calculator = new TicketPriceCalculator();

        Money price = calculator.calculateBasePrice(age);

        assertEquals(expectedPenny, price.getPenny());

    }

    @ParameterizedTest
    @CsvSource({
        "true,  false, 6000, 5700",
        "true,  true,  6000, 6000",
        "false, false, 6000, 6000",
        "false, true,  6000, 6000",
        "true,  false, 1500, 1425",
        "true,  true,  1500, 1500",
        "false, false, 1500, 1500",
        "false, true,  1500, 1500"
})
    void beveragePriceMatchesMembershipAndAlcoholRules(
            boolean active,
            boolean alcoholic,
            long originalPenny,
            long expectedPenny) {

        Membership membership = new Membership("member1");
        membership.setActiveStatus(active);

        BeverageDispenser dispenser =  new BeverageDispenser("test drink", 10000);

        Beverage beverage = new Beverage("b1",Money.ofPenny(originalPenny),"test drink",BeverageSize.MEDIUM,alcoholic,dispenser
    );

    MembershipDiscountCalculator calculator = new MembershipDiscountCalculator();

    Money price = calculator.calculatePrice(beverage, membership);

    assertEquals(expectedPenny, price.getPenny());
    assertEquals(originalPenny, beverage.getPrice().getPenny());
}

@Test
void adultTicketUsesSelectedMoviesBasePrice() {
    Movie movie = new Movie("Test Movie",120,AgeRequirement.AGE_0,Money.ofPenny(15000));

    TicketPriceCalculator calculator = new TicketPriceCalculator();

    Money price = calculator.calculateBasePrice(18, movie);

    assertEquals(15000L, price.getPenny());
}

@Test
void movieLongerThan150MinutesAdds20Kronor() {
    Movie movie = new Movie("Long Movie",151,AgeRequirement.AGE_0,Money.ofPenny(15000));

    TicketPriceCalculator calculator = new TicketPriceCalculator();

    Money price = calculator.calculateBasePrice(18, movie);

    assertEquals(17000L, price.getPenny());
}

@ParameterizedTest
@CsvSource({
    "149, 15000",
    "150, 15000",
    "151, 17000"
})
void durationSurchargeStartsAbove150Minutes(
    int durationMin, long expectedPenny) {

    Movie movie = new Movie("Test Movie",durationMin,AgeRequirement.AGE_0,Money.ofPenny(15000));

    TicketPriceCalculator calculator = new TicketPriceCalculator();

    Money price = calculator.calculateBasePrice(18, movie);

    assertEquals(expectedPenny, price.getPenny());
}

@Test
void childGets40KronorReductionFromMoviePrice() {
    Movie movie = new Movie("Test Movie",120,AgeRequirement.AGE_0,Money.ofPenny(15000));

    TicketPriceCalculator calculator = new TicketPriceCalculator();

    Money price = calculator.calculateBasePrice(17, movie);

    assertEquals(11000L, price.getPenny());
}
@Test
void seniorGets20KronorReductionFromMoviePrice() {
    Movie movie = new Movie("Test Movie",120,AgeRequirement.AGE_0,Money.ofPenny(15000));

    TicketPriceCalculator calculator = new TicketPriceCalculator();

    Money price = calculator.calculateBasePrice(65, movie);

    assertEquals(13000L, price.getPenny());
}

@Test
void childTicketPriceCannotFallBelow40Kronor() {
    Movie movie = new Movie("Cheap Movie",120,AgeRequirement.AGE_0,Money.ofPenny(3000));

    TicketPriceCalculator calculator = new TicketPriceCalculator();

    Money price = calculator.calculateBasePrice(17, movie);

    assertEquals(4000L, price.getPenny());
}

@ParameterizedTest
@CsvSource({
    "7999, 4000",
    "8000, 4000",
    "8001, 4001"
})
void childMinimumPriceBoundary(
        long moviePricePenny, long expectedPenny) {

    Movie movie = new Movie("Test Movie",120,AgeRequirement.AGE_0,Money.ofPenny(moviePricePenny));

    TicketPriceCalculator calculator = new TicketPriceCalculator();

    Money price = calculator.calculateBasePrice(17, movie);

    assertEquals(expectedPenny, price.getPenny());
}

@Property(tries = 1000)
void ticketPriceRespectsMinimumAndPreservesMoviePrice(
        @ForAll @IntRange(min = 0, max = 120) int age,
        @ForAll @IntRange(min = 1, max = 300) int durationMin,
        @ForAll @LongRange(min = 0, max = 100000) long originalPenny) {

    Movie movie = new Movie("Generated Movie",durationMin, AgeRequirement.AGE_0, Money.ofPenny(originalPenny));

    TicketPriceCalculator calculator = new TicketPriceCalculator();

    Money price = calculator.calculateBasePrice(age, movie);

    assertTrue(price.getPenny() >= 4000,"Ticket price before membership discount must be at least 40 kr");

    assertEquals(originalPenny, movie.getBasePrice().getPenny());
}

}