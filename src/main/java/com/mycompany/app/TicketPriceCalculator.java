package com.mycompany.app;

public class TicketPriceCalculator {
    
    public Money calculatePrice(int age, boolean member){
        return Money.ofPenny(12000);
    }
}
