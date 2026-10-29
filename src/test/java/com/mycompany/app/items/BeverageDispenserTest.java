
package com.mycompany.app.items;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BeverageDispenserTest {

    private BeverageDispenser dispenser;

    @BeforeEach
    void setUp() {
        dispenser = new BeverageDispenser("Coca-Cola", 1000);
    }

    @Test
    void constructorStoresNameAndVolume() {
        assertAll(
                () -> assertEquals("Coca-Cola", dispenser.getName()),
                () -> assertEquals(1000, dispenser.getVolume())
        );
    }

    @Test
    void constructorRejectsNegativeVolume() {
        assertThrows(IllegalArgumentException.class, () ->
                new BeverageDispenser("Coca-Cola", -1)
        );
    }

    @Test
    void canDispenseWhenEnoughVolumeExists() {
        assertTrue(dispenser.canDispense(500));
    }

    @Test
    void cannotDispenseMoreThanAvailable() {
        assertFalse(dispenser.canDispense(1500));
    }

    @Test
    void cannotDispenseZeroOrNegativeVolume() {
        assertAll(
                () -> assertFalse(dispenser.canDispense(0)),
                () -> assertFalse(dispenser.canDispense(-1))
        );
    }

    @Test
    void dispenseReducesVolume() {
        assertTrue(dispenser.dispense(300));

        assertEquals(700, dispenser.getVolume());
    }

    @Test
    void dispenseFailsWhenInsufficientVolumeExists() {
        assertFalse(dispenser.dispense(1500));

        assertEquals(1000, dispenser.getVolume());
    }

    @Test
    void refillIncreasesVolume() {
        dispenser.refill(500);

        assertEquals(1500, dispenser.getVolume());
    }

    @Test
    void refillRejectsZeroOrNegativeVolume() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () ->
                        dispenser.refill(0)),
                () -> assertThrows(IllegalArgumentException.class, () ->
                        dispenser.refill(-1))
        );

        assertEquals(1000, dispenser.getVolume());
    }
}
