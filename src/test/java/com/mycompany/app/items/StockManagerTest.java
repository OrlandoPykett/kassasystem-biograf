package com.mycompany.app.items;

import com.mycompany.app.Money;
import com.mycompany.app.StockManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StockManagerTest {

    @Test
    void addStockStoresQuantity() {
        StockManager manager = new StockManager();
        Item apple = new Food("AB1", "apple", Money.ofPenny(1500));
        manager.addStock(apple, 10);

        assertEquals(10, manager.getStock(apple));
    }

    @Test
    void addStockIncreasesQuantityWhenItemIsPresent() {
        StockManager manager = new StockManager();
        Item apple = new Food("AB1", "apple", Money.ofPenny(1500));
        manager.addStock(apple, 10);
        manager.addStock(apple, 5);

        assertEquals(15, manager.getStock(apple));
    }

    @Test
    void removeStockRemovesStock() {
        StockManager manager = new StockManager();
        Item apple = new Food("AB1", "apple", Money.ofPenny(1500));
        manager.addStock(apple, 10);
        manager.removeStock(apple, 3);

        assertEquals(7, manager.getStock(apple));
    }

    @Test
    void hasStockReturnsTrueWhenStockIsSufficient() {
        StockManager manager = new StockManager();
        Item apple = new Food("AB1", "apple", Money.ofPenny(1500));
        manager.addStock(apple, 10);

        boolean result = manager.hasStock(apple, 5);
        assertTrue(result);
    }

    @Test
    void hasStockReturnsFalseWhenStockIsInsufficient() {
        StockManager manager = new StockManager();
        Item apple = new Food("AB1", "apple", Money.ofPenny(1500));
        manager.addStock(apple, 10);

        boolean result = manager.hasStock(apple, 15);
        assertFalse(result);
    }

    @Test
    void getStockReturnsZeroForItemsNotAdded() {
        StockManager manager = new StockManager();
        Item apple = new Food("AB1", "apple", Money.ofPenny(1500));
        assertEquals(0, manager.getStock(apple));
    }

    @Test
    void removeStockDoesNotRemoveMoreThanExists() {
        StockManager manager = new StockManager();
        Item apple = new Food("AB1", "apple", Money.ofPenny(1500));
        manager.addStock(apple, 10);
        manager.removeStock(apple, 15);

        assertEquals(10, manager.getStock(apple));
    }
}