package com.mycompany.app;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

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

        assertEquals(8000L, price.getPenny());
    } 

    @Test 
    void seniorTicketCosts100Kronor(){
        TicketPriceCalculator calculator = new TicketPriceCalculator();
        Money price = calculator.calculatePrice(65, false);

        assertEquals(10000L, price.getPenny());

    }

    @Test 
    void negativeAgeIsRejected(){
        TicketPriceCalculator calculator = new TicketPriceCalculator();
        
        assertThrows(IllegalArgumentException.class, () -> {
            calculator.calculatePrice(-1, false);

        });

    }

    @Test 
    void adultMemberTicketCosts100Kronor(){
        TicketPriceCalculator calculator = new TicketPriceCalculator();
        Money price = calculator.calculatePrice(18, true);

        assertEquals(10000L, price.getPenny());
    }

    @ParameterizedTest 
    @CsvSource ({
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

    void ticketPriceMatchesAgeAndMembership(
        int age, boolean member, long expectedPenny
    ){
        TicketPriceCalculator calculator = new TicketPriceCalculator();
        Money price = calculator.calculatePrice(age, member);

        assertEquals(expectedPenny, price.getPenny());
    }

    
}
