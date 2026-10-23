package com.mycompany.app;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TicketPriceCalculatorTest {

    @Test 
    void adultTicketCosts120Kronor(){
        TicketPriceCalculator calculator = new TicketPriceCalculator();
        Money price = calculator.calculatePrice(18, false);

        assertEquals(12000L, price.getPenny());
    }

    @Test
    void childTicketCosts80Kronor(){
        TicketPriceCalculator calculator = new TicketPriceCalculator();
        Money price = calculator.calculatePrice(17, false);

        assertEquals(8000, price.getPenny());
    } 

    @Test 
    void seniorTicketCosts100Kronor(){
        TicketPriceCalculator calculator = new TicketPriceCalculator();
        Money price = calculator.calculatePrice(65, false);

        assertEquals(10000, price.getPenny());

    }

    @Test 
    void negativeAgeIsRejected(){
        TicketPriceCalculator calculator = new TicketPriceCalculator();
        
        assertThrows(IllegalArgumentException.class, () -> {
            calculator.calculatePrice(-1, false);

        });

    }

    
}
