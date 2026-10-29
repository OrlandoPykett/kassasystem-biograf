package com.mycompany.app;

import com.mycompany.app.items.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CheckoutManagerTest {

    private StockManager stockManager;
    private TicketManager ticketManager;
    private CheckoutManager checkoutManager;
    private Food popcorn;
    private Beverage coke;
    private BeverageDispenser cokeDispenser;

    @BeforeEach
    void setUp() {
        stockManager = new StockManager();
        ticketManager = new TicketManager();
        checkoutManager = new CheckoutManager(stockManager, ticketManager);

        popcorn = new Food(
                "F1",
                Money.ofPenny(7500),
                "popcorn");

      int availableMl = BeverageSize.MEDIUM.getVolumeMl() * 2;

        cokeDispenser = new BeverageDispenser(
                "Coca-Cola",
                availableMl);

        coke = new Beverage(
                "B1",
                Money.ofPenny(5000),
                "Coca-Cola",
                BeverageSize.MEDIUM,
                false,
                cokeDispenser);

        stockManager.addStock(popcorn, 10);
        stockManager.registerItem(coke);
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
            assertEquals(
        BeverageSize.MEDIUM.getVolumeMl() * 2,
        stockManager.getStock(coke));
        });
    }
}
