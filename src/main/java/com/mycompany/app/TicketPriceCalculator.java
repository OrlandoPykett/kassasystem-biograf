package com.mycompany.app;

public class TicketPriceCalculator {
    
    public Money calculatePrice(int age, boolean member){
         if(age < 0){
            throw new IllegalArgumentException("Age cannot be negative");
         }

        Money price;

        if (age < 18){
            price = Money.ofPenny(8000);
        }else if(age >= 65 ){
            price = Money.ofPenny(10000);
        }else{
            price = Money.ofPenny(12000);
        }

        if (member){
            price = price.subtract(Money.ofPenny(2000));
        }

        return price;
    }
}

