package com.mycompany.app.items;

import com.mycompany.app.Money;
import com.mycompany.app.StockManager;

import java.time.LocalDateTime;

public class CinemaInitialiser {

    // Populates the inventory and screenings with predetermined items
    // Changes are not saved
    // The inventory is reset on each start


    // snacks
    Food popcorn = new Food("F1", "popcorn", Money.ofPenny(7500));
    Food baconchips = new Food("F2", "baconchips", Money.ofPenny(6500));
    Food nachos = new Food("F3", "nachos", Money.ofPenny(12500));

    // Drinks
    Beverage b1s = new Beverage("B1S", "coca-cola", Money.ofPenny(4000), BeverageSize.SMALL, false);
    Beverage b1m = new Beverage("B1M", "coca-cola", Money.ofPenny(6000), BeverageSize.MEDIUM, false);
    Beverage b1l = new Beverage("B1L", "coca-cola", Money.ofPenny(8000), BeverageSize.LARGE, false);

    Beverage b2s = new Beverage("B2S", "fanta", Money.ofPenny(4000), BeverageSize.SMALL, false);
    Beverage b2m = new Beverage("B2M", "fanta", Money.ofPenny(6000), BeverageSize.MEDIUM, false);
    Beverage b2l = new Beverage("b2l", "fanta", Money.ofPenny(8000), BeverageSize.LARGE, false);

    Beverage b3 = new Beverage("B3", "beer", Money.ofPenny(1200), BeverageSize.MEDIUM, true);
    Beverage b4 = new Beverage("B4", "cider", Money.ofPenny(1000), BeverageSize.MEDIUM, true);
    Beverage b5 = new Beverage("B5", "cocktail", Money.ofPenny(16000), BeverageSize.SMALL, true);

    // Movies
    // Should be moved to a
    Movie m1 = new Movie("The Godfather", 175, AgeRequirement.AGE_15);
    Movie m2 = new Movie("The Dark Knight", 152, AgeRequirement.AGE_12);
    Movie m3 = new Movie("Pulp Fiction", 154, AgeRequirement.AGE_18);
    Movie m4 = new Movie("Parasite", 132, AgeRequirement.AGE_15);
    Movie m5 = new Movie("The Lion King", 88, AgeRequirement.AGE_0);


    public void populateInventory() {

        StockManager manager = new StockManager();

        manager.addStock(popcorn, 100);
        manager.addStock(baconchips, 100);
        manager.addStock(nachos, 100);

        manager.addStock(b1s, 100);
        manager.addStock(b1m, 100);
        manager.addStock(b1l, 100);
        manager.addStock(b2s, 100);
        manager.addStock(b2m, 100);
        manager.addStock(b2l, 100);
        manager.addStock(b3, 100);
        manager.addStock(b4, 100);
        manager.addStock(b5, 100);
    }

    public void populateMovies() {
        MovieScreening s1m1 = new MovieScreening(m1, LocalDateTime.of(2026, 12, 1, 18, 0), "Ragnar");
        MovieScreening s2m1 = new MovieScreening(m1, LocalDateTime.of(2026, 12, 3, 20, 0), "Ragnar");
        MovieScreening s3m1 = new MovieScreening(m1, LocalDateTime.of(2026, 12, 5, 18, 0), "Torsten");
        MovieScreening s4m1 = new MovieScreening(m1, LocalDateTime.of(2026, 12, 7, 18, 30), "Ragnar");

        MovieScreening s1m2 = new MovieScreening(m2, LocalDateTime.of(2026, 12, 1, 19, 45), "Torsten");
        MovieScreening s2m2 = new MovieScreening(m2, LocalDateTime.of(2026, 12, 2, 20, 0), "Torsten");
        MovieScreening s3m2 = new MovieScreening(m2, LocalDateTime.of(2026, 12, 3, 19, 30), "Torsten");
        MovieScreening s4m2 = new MovieScreening(m2, LocalDateTime.of(2026, 12, 6, 15, 0), "Ragnar");

        MovieScreening s1m3 = new MovieScreening(m3, LocalDateTime.of(2026, 12, 4, 17, 45), "Linda");

        MovieScreening s1m4 = new MovieScreening(m4, LocalDateTime.of(2026, 12, 2, 15, 45), "Linda");

        MovieScreening s1m5 = new MovieScreening(m5, LocalDateTime.of(2026, 12, 4, 19, 15), "Herman");
    }
}
