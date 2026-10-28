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

// Använder den valda filmens grundpris.
// Åldersavdrag och längdtillägg införs i kommande steg.
public Money calculateBasePrice(int age, Movie movie) {
    if (age < 0) {
        throw new IllegalArgumentException("Age cannot be negative");
    }

    return movie.getBasePrice();
}
    
}

