package com.mycompany.app;

import com.mycompany.app.items.Movie;

public class TicketPriceCalculator {

    // Åldersgränser för biljettkategorierna.
    private static final int ADULT_AGE = 18;
    private static final int SENIOR_AGE = 65;

    // Standardpriser som används av den äldre metoden, i ören.
    private static final long CHILD_PRICE = 8000;
    private static final long ADULT_PRICE = 12000;
    private static final long SENIOR_PRICE = 10000;

    // Regler för den nya filmprisberäkningen. Alla belopp är i ören.
    private static final int LONG_MOVIE_MINUTES = 150;
    private static final long LONG_MOVIE_EXTRA = 2000;
    private static final long CHILD_REDUCTION = 4000;
    private static final long SENIOR_REDUCTION = 2000;
    private static final long MINIMUM_PRICE = 4000;

    // Äldre metod som behålls tills anropen har flyttats till filmberäkningen.
    public Money calculateBasePrice(int age) {
        if (age < 0) {
            throw new IllegalArgumentException("Age cannot be negative");
        }

        if (age < ADULT_AGE) {
            return Money.ofPenny(CHILD_PRICE);
        } else if (age >= SENIOR_AGE) {
            return Money.ofPenny(SENIOR_PRICE);
        } else {
            return Money.ofPenny(ADULT_PRICE);
        }
    }

    // Beräknar biljettpriset före medlemsrabatt.
    // Filmens sparade grundpris ändras inte.
    public Money calculateBasePrice(int age, Movie movie) {
        if (age < 0) {
            throw new IllegalArgumentException("Age cannot be negative");
        }

        Money price = movie.getBasePrice();

        // Filmer längre än 150 minuter får ett tillägg på 20 kr.
        if (movie.getDurationMin() > LONG_MOVIE_MINUTES) {
            price = price.add(Money.ofPenny(LONG_MOVIE_EXTRA));
        }

        // Bestäm åldersavdraget innan det dras från priset.
        long reduction = 0;

        if (age < ADULT_AGE) {
            reduction = CHILD_REDUCTION;
        } else if (age >= SENIOR_AGE) {
            reduction = SENIOR_REDUCTION;
        }

        // Priset efter åldersavdrag ska vara minst 40 kr.
        // Kontrollera först för att undvika en negativ subtraktion.
        if (price.getPenny() <= MINIMUM_PRICE + reduction) {
            return Money.ofPenny(MINIMUM_PRICE);
        }

        return price.subtract(Money.ofPenny(reduction));
    }
}
