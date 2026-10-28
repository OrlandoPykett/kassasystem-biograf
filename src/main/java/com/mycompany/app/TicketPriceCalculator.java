package com.mycompany.app;

import com.mycompany.app.items.Movie;

public class TicketPriceCalculator {

    // Behålls tills anrop har flyttats till filmberäkningen.
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

    // Beräknar biljettpriset från filmpris, längdtillägg och åldersavdrag.
    // Priset före medlemsrabatt är minst 40 kr.
    public Money calculateBasePrice(int age, Movie movie) {
        if (age < 0) {
            throw new IllegalArgumentException("Age cannot be negative");
        }

        Money price = movie.getBasePrice();

        // Filmer över 150 minuter kostar 20 kr extra.
        if (movie.getDurationMin() > 150) {
            price = price.add(Money.ofPenny(2000));
        }

        // Bestäm åldersavdraget utan att dra av det ännu.
        long reductionPenny = 0;

        if (age < 18) {
            reductionPenny = 4000;
        } else if (age >= 65) {
            reductionPenny = 2000;
        }

        // Kontrollera minimipriset före subtraktionen.
        // Då försöker vi aldrig skapa ett negativt Money-belopp.
        if (price.getPenny() <= 4000 + reductionPenny) {
            return Money.ofPenny(4000);
        }

        return price.subtract(Money.ofPenny(reductionPenny));
    }
}

