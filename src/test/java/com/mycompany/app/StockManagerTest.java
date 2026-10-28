package com.mycompany.app;

import com.mycompany.app.items.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StockManagerTest {

    private StockManager manager;
    private Food popcorn;
    private Beverage coke;

    @BeforeEach
    void setUp() {
        manager = new StockManager();
        popcorn = new Food(
                "F1",
                Money.ofPenny(7500),
                "popcorn"
        );
        BeverageDispenser cokeDispenser = new BeverageDispenser("Coke", 1000);

        coke = new Beverage(
                "D1",
                Money.ofPenny(5000),
                "Coke",
                BeverageSize.MEDIUM,
                false,
                cokeDispenser
        );
    }

    @Test
    void addStockStoresQuantity() {
        manager.addStock(popcorn, 10);

        assertEquals(10, manager.getStock(popcorn));
    }

    @Test
    void addStockUpdatesQuantity() {
        manager.addStock(popcorn, 10);
        manager.addStock(popcorn, 5);

        assertEquals(15, manager.getStock(popcorn));
    }

    @Test
    void removeStockRemovesStock() {
        manager.addStock(popcorn, 10);
        manager.removeStock(popcorn, 3);

        assertEquals(7, manager.getStock(popcorn));
    }

    @Test
    void hasStockReturnsCorrectValue() {
        manager.addStock(popcorn, 10);

        boolean resultTrue = manager.hasStock(popcorn, 5);
        boolean resultFalse = manager.hasStock(popcorn, 15);

        assertTrue(resultTrue);
        assertFalse(resultFalse);
    }


    @Test
    void reportsNoStockForUnknownItem() {
        assertAll( () -> {
            assertEquals(0, manager.getStock(popcorn));
            assertFalse(manager.hasStock(popcorn, 1));
        });
    }

    @Test
    void doesNotAllowNegativeStock() {
        manager.addStock(popcorn, 10);
        manager.removeStock(popcorn, 15);

        assertEquals(10, manager.getStock(popcorn));
    }

    @Test
    void doesNotRemoveStockWithoutEntry() {
        assertDoesNotThrow( () -> {
            manager.removeStock(popcorn,1 );
        });
    }
}