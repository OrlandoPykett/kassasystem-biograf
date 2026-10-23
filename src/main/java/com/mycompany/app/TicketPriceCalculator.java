package com.mycompany.app;

public class TicketPriceCalculator {
    
    public Money calculatePrice(int age, boolean member){
        if (age < 18){
            return Money.ofPenny(8000);
        }

        if(age >= 65 ){
            return Money.ofPenny(10000); 
        }

        return Money.ofPenny(12000);
        
    }
}
