package com.mycompany.app;


import com.mycompany.app.items.Beverage;
import com.mycompany.app.items.BeverageDispenser;
import com.mycompany.app.items.BeverageSize;
import com.mycompany.app.items.Food;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StockManagerTest {

    private StockManager manager;
    private Food popcorn;
    private Beverage cokeS;
    private Beverage cokeM;
    private BeverageDispenser dispenser;

    @BeforeEach
    void setUp() {
        manager = new StockManager();
        popcorn = new Food(
                "F1",
                Money.ofPenny(7500),
                "popcorn"
        );

        dispenser = new BeverageDispenser("Coca-Cola", 1000);

        cokeS = new Beverage(
                "B1S",
                Money.ofPenny(4000),
                "coca-cola",
                BeverageSize.SMALL,
                false,
                dispenser
        );

        cokeM = new Beverage(
                "B1M",
                Money.ofPenny(6000),
                "coca-cola",
                BeverageSize.MEDIUM,
                false,
                dispenser
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


    @Test
    void registersBeverageWithoutAddingStock() {
        manager.registerItem(cokeS);

        assertAll(
                () -> assertEquals(1000, manager.getStock(cokeS)),
                () -> assertEquals(cokeS, manager.getItem("B1S"))
        );
    }

    @Test
    void addStockRegistersBeverageWithoutRefillingDispenser() {
        manager.addStock(cokeS, 100);

        assertAll(
                () -> assertEquals(1000, manager.getStock(cokeS)),
                () -> assertEquals(cokeS, manager.getItem("B1S"))
        );
    }

    @Test
    void hasStockChecksBeverageVolume() {
        assertAll(
                () -> assertTrue(manager.hasStock(cokeS, 3)),
                () -> assertFalse(manager.hasStock(cokeS, 4))
        );
    }

    @Test
    void removeStockDispensesBeverageVolume() {
        manager.removeStock(cokeS, 2);

        assertEquals(340, dispenser.getVolume());
    }

    @Test
    void cannotDispenseBeverageWhenVolumeIsInsufficient() {
        manager.removeStock(cokeM, 2);

        assertEquals(1000, dispenser.getVolume());
    }

    @Test
    void handlesNullItems() {
        assertAll(
                () -> assertEquals(0, manager.getStock(null)),
                () -> assertFalse(manager.hasStock(null, 1)),
                () -> assertDoesNotThrow(() -> manager.addStock(null, 1)),
                () -> assertDoesNotThrow(() -> manager.removeStock(null, 1)),
                () -> assertDoesNotThrow(() -> manager.registerItem(null)),
                () -> assertNull(manager.getItem(null))
        );
    }

    @Test
    void rejectsInvalidQuantities() {
        manager.addStock(popcorn, 10);

        assertAll(
                () -> assertEquals(10, manager.getStock(popcorn)),
                () -> assertFalse(manager.hasStock(popcorn, -1)),
                () -> assertDoesNotThrow(() -> manager.addStock(popcorn, -1)),
                () -> assertDoesNotThrow(() -> manager.removeStock(popcorn, -1))
        );
    }

    @Test
    void registersItemsAndReturnsTheirIds() {
        manager.registerItem(cokeS);
        manager.registerItem(cokeM);

        assertAll(
                () -> assertEquals(2, manager.getItems().size()),
                () -> assertTrue(manager.getItemIds().contains("B1S")),
                () -> assertTrue(manager.getItemIds().contains("B1M")),
                () -> assertEquals(cokeM, manager.getItem("B1M"))
        );
    }

}