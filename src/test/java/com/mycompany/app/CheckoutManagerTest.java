package com.mycompany.app;

import com.mycompany.app.items.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CheckoutManagerTest {

    private StockManager stockManager;
    private TicketManager ticketManager;
    private CheckoutManager checkoutManager;
    private Food popcorn;
    private Beverage coke;

    @BeforeEach
    void setUp() {
        stockManager = new StockManager();
        ticketManager = new TicketManager();
        checkoutManager = new CheckoutManager(stockManager, ticketManager);

        popcorn = new Food(
                "F1",
                Money.ofPenny(7500),
                "popcorn"
        );

        BeverageDispenser cokeDispenser = new BeverageDispenser("Coke", 2000);
        coke = new Beverage(
                "B1",
                Money.ofPenny(5000),
                "Coca-Cola",
                BeverageSize.MEDIUM,
                false,
                cokeDispenser
        );

        stockManager.addStock(popcorn, 10);
        stockManager.addStock(coke, 2);
    }

    @Test
    void rejectOrderWhenAllItemsAreNotAvailable() {
        Receipt receipt = new Receipt();
        receipt.addItem(popcorn, popcorn.getName());
        receipt.addItem(coke, coke.getName());
        receipt.addItem(coke, coke.getName());
        receipt.addItem(coke, coke.getName());

        boolean result = checkoutManager.checkout(receipt, 18);

        assertAll( () -> {
            assertFalse(result);
            assertEquals(10, stockManager.getStock(popcorn));
            assertEquals(2, stockManager.getStock(coke));
        });
    }
}
