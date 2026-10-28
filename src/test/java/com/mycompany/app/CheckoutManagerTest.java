package com.mycompany.app;

import com.mycompany.app.items.Beverage;
import com.mycompany.app.items.BeverageSize;
import com.mycompany.app.items.Food;
import com.mycompany.app.items.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CheckoutManagerTest {

    private StockManager stockManager;
    private CheckoutManager checkoutManager;
    private Food popcorn;
    private Beverage coke;

    @BeforeEach
    void setUp() {
        stockManager = new StockManager();
        checkoutManager = new CheckoutManager(stockManager);

        popcorn = new Food(
                "F1",
                Money.ofPenny(7500),
                "popcorn"
        );

        coke = new Beverage(
                "B1",
                Money.ofPenny(5000),
                "Coca-Cola",
                BeverageSize.MEDIUM,
                false
        );

        stockManager.addStock(popcorn, 10);
        stockManager.addStock(coke, 2);
    }

    @Test
    void rejectOrderWhenAllItemsAreNotAvailable() {
        List<Item> order = List.of(popcorn, coke, coke, coke);

        boolean result = checkoutManager.checkout(order);

        assertAll( () -> {
            assertFalse(result);
            assertEquals(10, stockManager.getStock(popcorn));
            assertEquals(2, stockManager.getStock(coke));
        });
    }
}
