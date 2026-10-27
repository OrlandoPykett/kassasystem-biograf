package com.mycompany.app;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

        assertThrows(
                IllegalArgumentException.class,
                () -> calculator.calculateBasePrice(-1)
        );
    }
}