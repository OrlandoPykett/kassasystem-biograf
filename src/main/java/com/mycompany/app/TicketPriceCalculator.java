package com.mycompany.app;
import com.mycompany.app.items.Movie;

public class TicketPriceCalculator {

    public Money calculateBasePrice(int age) {
        if (age < 0) {
            throw new IllegalArgumentException("Age cannot be negative");
        }

        if (age < 18) {
            return Money.ofPenny(8000);
        } else if (age >= 65) {
            return Money.ofPenny(10000);
        } else {
            return Money.ofPenny(12000);
        }
    }

// Tillfällig implementation för att kunna köra det nya testet.
// Filmens pris används i nästa steg.
    public Money calculateBasePrice(int age, Movie movie) {
        return calculateBasePrice(age);
    }
    
}

