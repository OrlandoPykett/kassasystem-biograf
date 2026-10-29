package com.mycompany.app.initialisation;

import com.mycompany.app.Money;
import com.mycompany.app.ScreeningManager;
import com.mycompany.app.StockManager;
import com.mycompany.app.items.*;

import java.time.LocalDateTime;

public class CinemaInitialiser {

    // Populates the inventory and screenings with predetermined items
    // Changes are not saved
    // The inventory is reset on each start


    // snacks
    private final Food popcorn = new Food("F1", Money.ofPenny(7500), "popcorn");
    private final Food baconchips = new Food("F2", Money.ofPenny(6500), "baconchips");
    private final Food nachos = new Food("F3", Money.ofPenny(12500), "nachos");

    // Drinks
    private final BeverageDispenser cokeDispenser = new BeverageDispenser("Coca-Cola", 50000);
    private final Beverage cokeS = new Beverage("B1S", Money.ofPenny(4000), "coca-cola", BeverageSize.SMALL, false, cokeDispenser);
    private final Beverage cokeM = new Beverage("B1M", Money.ofPenny(6000), "coca-cola", BeverageSize.MEDIUM, false, cokeDispenser);
    private final Beverage cokeL = new Beverage("B1L", Money.ofPenny(8000), "coca-cola", BeverageSize.LARGE, false, cokeDispenser);

    private final BeverageDispenser fantaDispenser = new BeverageDispenser("Fanta", 15000);
    private final Beverage fantaS = new Beverage("B2S", Money.ofPenny(4000), "fanta", BeverageSize.SMALL, false, fantaDispenser);
    private final Beverage fantaM = new Beverage("B2M", Money.ofPenny(6000), "fanta", BeverageSize.MEDIUM, false, fantaDispenser);
    private final Beverage fantaL = new Beverage("b2l", Money.ofPenny(8000), "fanta", BeverageSize.LARGE, false, fantaDispenser);

    private final BeverageDispenser beerDispenser = new BeverageDispenser("Pripps Blå",10000);
    private final Beverage beer = new Beverage("B3", Money.ofPenny(1200), "beer", BeverageSize.MEDIUM, true, beerDispenser);
    private final BeverageDispenser ciderDispenser = new BeverageDispenser("Kopparberg Pear", 10000);
    private final Beverage cider = new Beverage("B4", Money.ofPenny(1000), "cider", BeverageSize.MEDIUM, true, ciderDispenser);
    private final BeverageDispenser cocktailDispenser = new BeverageDispenser("Generic Cocktail", 5000);
    private final Beverage cocktail = new Beverage("B5", Money.ofPenny(16000), "cocktail", BeverageSize.SMALL, true, cocktailDispenser);

    // Movies
    // Should be moved to a
    private final Movie m1 = new Movie("The Godfather", 175, AgeRequirement.AGE_15);
    private final Movie m2 = new Movie("The Dark Knight", 152, AgeRequirement.AGE_12);
    private final Movie m3 = new Movie("Pulp Fiction", 154, AgeRequirement.AGE_18);
    private final Movie m4 = new Movie("Parasite", 132, AgeRequirement.AGE_15);
    private final Movie m5 = new Movie("The Lion King", 88, AgeRequirement.AGE_0);

    public void populateInventory(StockManager manager) {
        manager.addStock(popcorn, 1000);
        manager.addStock(baconchips, 100);
        manager.addStock(nachos, 100);

        // Register beverages without adding volume again
        manager.registerItem(cokeS);
        manager.registerItem(cokeM);
        manager.registerItem(cokeL);

        manager.registerItem(fantaS);
        manager.registerItem(fantaM);
        manager.registerItem(fantaL);

        manager.registerItem(beer);
        manager.registerItem(cider);
        manager.registerItem(cocktail);
    }

    public void populateMovies(ScreeningManager manager) {
        Theater t1 = new Theater("Ragner", 10, 30);
        Theater t2 = new Theater("Torsten", 5, 6);
        Theater t3 = new Theater("Linda", 30, 50);
        Theater t4 = new Theater("Harald", 25, 60);

        MovieScreening s1m1 = new MovieScreening(m1, LocalDateTime.of(2026, 12, 1, 18, 0), t1);
        manager.addScreening(s1m1);
        MovieScreening s2m1 = new MovieScreening(m1, LocalDateTime.of(2026, 12, 3, 20, 0), t1);
        manager.addScreening(s2m1);
        MovieScreening s3m1 = new MovieScreening(m1, LocalDateTime.of(2026, 12, 5, 18, 0), t2);
        manager.addScreening(s3m1);
        MovieScreening s4m1 = new MovieScreening(m1, LocalDateTime.of(2026, 12, 7, 18, 30), t1);
        manager.addScreening(s4m1);

        MovieScreening s1m2 = new MovieScreening(m2, LocalDateTime.of(2026, 12, 1, 19, 45), t2);
        manager.addScreening(s1m2);
        MovieScreening s2m2 = new MovieScreening(m2, LocalDateTime.of(2026, 12, 2, 20, 0), t2);
        manager.addScreening(s2m2);
        MovieScreening s3m2 = new MovieScreening(m2, LocalDateTime.of(2026, 12, 3, 19, 30), t2);
        manager.addScreening(s3m2);
        MovieScreening s4m2 = new MovieScreening(m2, LocalDateTime.of(2026, 12, 6, 15, 0), t1);
        manager.addScreening(s4m2);

        MovieScreening s1m3 = new MovieScreening(m3, LocalDateTime.of(2026, 12, 4, 17, 45), t3);
        manager.addScreening(s1m3);

        MovieScreening s1m4 = new MovieScreening(m4, LocalDateTime.of(2026, 12, 2, 15, 45), t3);
        manager.addScreening(s1m4);

        MovieScreening s1m5 = new MovieScreening(m5, LocalDateTime.of(2026, 12, 4, 19, 15), t4);
        manager.addScreening(s1m5);
    }


}
