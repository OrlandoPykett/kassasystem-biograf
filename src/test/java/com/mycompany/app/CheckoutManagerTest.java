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

    @Test
    void rejectEmptyReceipt() {
        Receipt receipt = new Receipt();

        boolean result = checkoutManager.checkout(receipt, 18);

        assertFalse(result);
        assertEquals(10, stockManager.getStock(popcorn));
        assertEquals(
                BeverageSize.MEDIUM.getVolumeMl() * 2,
                stockManager.getStock(coke));
    }

    @Test
    void successfullyCheckoutFood() {
        Receipt receipt = new Receipt();
        receipt.addItem(popcorn, popcorn.getName());

        boolean result = checkoutManager.checkout(receipt, 18);

        assertAll(
                () -> assertTrue(result),
                () -> assertEquals(9, stockManager.getStock(popcorn))
        );
    }

    @Test
    void successfullyCheckoutBeverage() {
        Receipt receipt = new Receipt();
        receipt.addItem(coke, coke.getName());

        assertTrue(stockManager.hasStock(coke, 1));

        boolean result = checkoutManager.checkout(receipt, 18);

        assertAll(
                () -> assertTrue(result),
                () -> assertEquals(
                        BeverageSize.MEDIUM.getVolumeMl(),
                        stockManager.getStock(coke)
                )
        );
    }

    @Test
    void rejectOrderWhenFoodStockIsInsufficient() {
        Receipt receipt = new Receipt();

        for (int i = 0; i < 11; i++) {
            receipt.addItem(popcorn, popcorn.getName());
        }

        boolean result = checkoutManager.checkout(receipt, 18);

        assertAll(
                () -> assertFalse(result),
                () -> assertEquals(10, stockManager.getStock(popcorn))
        );
    }

    @Test
    void rejectOrderWhenBeverageStockIsInsufficient() {
        Receipt receipt = new Receipt();

        for (int i = 0; i < 3; i++) {
            receipt.addItem(coke, coke.getName());
        }

        boolean result = checkoutManager.checkout(receipt, 18);

        assertAll(
                () -> assertFalse(result),
                () -> assertEquals(
                        BeverageSize.MEDIUM.getVolumeMl() * 2,
                        stockManager.getStock(coke))
        );
    }
}
