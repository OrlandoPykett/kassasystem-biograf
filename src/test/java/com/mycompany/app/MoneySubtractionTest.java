package com.mycompany.app;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
    
    class MoneySubtractionTest{

        @Test
        
        void subtract20KronorFrom120Gives100(){
            Money price = Money.ofPenny(12000);
            Money discount = Money.ofPenny(2000);

            Money result = price.subtract(discount);
            assertEquals(10000L, result.getPenny());
        }
    }
