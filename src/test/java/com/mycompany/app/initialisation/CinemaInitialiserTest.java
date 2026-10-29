package com.mycompany.app.initialisation;

import com.mycompany.app.ScreeningManager;
import com.mycompany.app.StockManager;
import com.mycompany.app.items.Beverage;
import com.mycompany.app.items.Food;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CinemaInitialiserTest {

    @Test
    void foodItemsHaveCorrectInitialQuantities() {
        StockManager manager = new StockManager();
        CinemaInitialiser initialiser = new CinemaInitialiser();
        initialiser.populateInventory(manager);

        Food popcorn = (Food) manager.getItem("F1");
        Food baconchips = (Food) manager.getItem("F2");
        Food nachos = (Food) manager.getItem("F3");

        assertAll(
                () -> assertThat(manager.getStock(popcorn), is(1000)),
                () -> assertThat(manager.getStock(baconchips), is(100)),
                () -> assertThat(manager.getStock(nachos), is(100))
        );
    }

    @Test
    void beverageDispensersHaveCorrectInitialVolumes() {
        StockManager manager = new StockManager();
        CinemaInitialiser initialiser = new CinemaInitialiser();

        initialiser.populateInventory(manager);

        Beverage coke = (Beverage) manager.getItem("B1S");
        Beverage fanta = (Beverage) manager.getItem("B2S");
        Beverage beer = (Beverage) manager.getItem("B3");
        Beverage cider = (Beverage) manager.getItem("B4");
        Beverage cocktail = (Beverage) manager.getItem("B5");

        assertAll(
                () -> assertThat(coke.getDispenser().getVolume(), is(50000)),
                () -> assertThat(fanta.getDispenser().getVolume(), is(15000)),
                () -> assertThat(beer.getDispenser().getVolume(), is(10000)),
                () -> assertThat(cider.getDispenser().getVolume(), is(10000)),
                () -> assertThat(cocktail.getDispenser().getVolume(), is(5000))
        );
    }

    @Test
    void beverageDispensersUseTheSameDispenser() {
        StockManager manager = new StockManager();
        CinemaInitialiser initialiser = new CinemaInitialiser();
        initialiser.populateInventory(manager);

        Beverage cokeS = (Beverage) manager.getItem("B1S");
        Beverage cokeM = (Beverage) manager.getItem("B1M");
        Beverage cokeL = (Beverage) manager.getItem("B1L");

        assertAll( () -> {
            // Ensure drink sizes use the same dispenser
            assertThat(cokeS.getDispenser(),
                    is(sameInstance(cokeM.getDispenser())));
            assertThat(cokeM.getDispenser(),
                    is(sameInstance(cokeL.getDispenser())));

        });
    }

    @Test
    void differentDrinksUseDifferentDispensers() {
        StockManager manager = new StockManager();
        CinemaInitialiser initialiser = new CinemaInitialiser();
        initialiser.populateInventory(manager);

        Beverage coke = (Beverage) manager.getItem("B1S");
        Beverage fanta = (Beverage) manager.getItem("B2S");
        Beverage beer = (Beverage) manager.getItem("B3");

        assertAll(
                () -> assertThat(coke.getDispenser(),
                        is(not(sameInstance(fanta.getDispenser())))),
                () -> assertThat(coke.getDispenser(),
                        is(not(sameInstance(beer.getDispenser())))),
                () -> assertThat(fanta.getDispenser(),
                        is(not(sameInstance(beer.getDispenser()))))
        );
    }

    @Test
    void populateInventoryCreatesNoDuplicateIds() {
        StockManager manager = new StockManager();
        CinemaInitialiser initialiser = new CinemaInitialiser();

        initialiser.populateInventory(manager);

        assertAll( () -> {
            assertThat(manager.getItemIds(), everyItem(notNullValue()));
            // Check no duplicate ids
            assertThat(
                    manager.getItemIds(),
                    hasSize(new HashSet<>(manager.getItemIds()).size())
            );
            assertThat(
                    manager.getItemIds(),
                    containsInAnyOrder(
                            "F1", "F2", "F3",
                            "B1S", "B1M", "B1L",
                            "B2S", "B2M", "b2l",
                            "B3", "B4", "B5"
                    )
            );
        });
    }

    @Test
    void populateMoviesRegistersAllScreenings() {
        ScreeningManager manager = new ScreeningManager();
        CinemaInitialiser initialiser = new CinemaInitialiser();

        initialiser.populateMovies(manager);

        assertThat(manager.getScreenings(), hasSize(11));
    }
}
