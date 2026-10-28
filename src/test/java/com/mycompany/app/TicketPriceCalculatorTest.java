package com.mycompany.app;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.mycompany.app.items.Beverage;
import com.mycompany.app.items.BeverageDispenser;
import com.mycompany.app.items.BeverageSize;

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
}