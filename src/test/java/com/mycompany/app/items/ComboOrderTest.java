package com.mycompany.app.items;

import com.mycompany.app.Money;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

import static org.hamcrest.MatcherAssert.assertThat;

public class ComboOrderTest {

    @Test
    void comboOrderHasFixedPrice() {
        ComboOrder combo = new ComboOrder(createSampleOrder());

        assertEquals(Money.ofPenny(40000), combo.getPrice());
    }

    @Test
    void comboOrderAcceptsRequiredItems() {
        Item[] order = createSampleOrder();
        ComboOrder combo = new ComboOrder(order);

        Item[] intendedItems = combo.getItems();

        assertThat(intendedItems, arrayContainingInAnyOrder(order));
    }

    @Test
    void comboOrderRequiresMinimumItems() {
        Item[] order = createSampleOrder();

        // replace a beverage with a food item
        order[1] = new Food("f2", Money.ofPenny(500), "crisps");

        assertThrows(IllegalArgumentException.class, () -> {
            new ComboOrder(order);
        });
    }

    @Test
    void foodItemMustCostAtLeast50KR() {
        Item[] order = createSampleOrder();

        // replace food item with a food item below 50 kr
        order[0] = new Food("f2", Money.ofPenny(1000), "crisps");

        assertThrows(IllegalArgumentException.class, () -> {
            new ComboOrder(order);
        });
    }

    @Test
    void comboOrderSelectsMostExpensiveEligibleItems() {
        Item[] order = Arrays.copyOf(createSampleOrder(), 9);

        // add cheaper items to order
        order[5] = new Food("f2", Money.ofPenny(5000), "crisps");
        order[6] = new Beverage(
                "b3",
                Money.ofPenny(1000),
                "Coke",
                BeverageSize.MEDIUM,
                false
        );

        // add more expensive items to order
        order[7] = new Food("f2", Money.ofPenny(7000), "crisps");
        order[8] = new Beverage(
                "b3",
                Money.ofPenny(5000),
                "Coke",
                BeverageSize.MEDIUM,
                false
        );

        ComboOrder combo =  new ComboOrder(order);

        long foodPrice = combo.getItems()[0].getPrice().getPenny();

        long priciestBeverage = 0;
        long priceBeverage1 = combo.getItems()[1].getPrice().getPenny();
        long priceBeverage2 = combo.getItems()[2].getPrice().getPenny();

        priciestBeverage = Math.max(priceBeverage1, priceBeverage2);

        assertEquals(7000, foodPrice);
        assertEquals(5000, priciestBeverage);
    }

    @Test
    void comboOrderReturnsExcessItems() {
        Item[] order = Arrays.copyOf(createSampleOrder(), 7);

        // add excess items to order
        Food food = new Food("f2", Money.ofPenny(2000), "crisps");
        Beverage drink = new Beverage(
                "b3",
                Money.ofPenny(1000),
                "Coke",
                BeverageSize.MEDIUM,
                false
        );

        order[5] = food;
        order[6] = drink;

        ComboOrder combo = new ComboOrder(order);

        assertTrue(combo.getExcess().stream().anyMatch(item -> item == food));

        assertEquals(2, combo.getExcess().size());
        assertTrue(combo.getExcess().contains(food));
        assertTrue(combo.getExcess().contains(drink));

        assertThat(combo.getExcess(), containsInAnyOrder(food, drink));
    }

    private Item[] createSampleOrder() {
        Food f1 = new Food("f1", Money.ofPenny(6000), "popcorn");
        Beverage b1 = new Beverage("b1", Money.ofPenny(2000), "Coke", BeverageSize.MEDIUM, false);
        Beverage b2 = new Beverage("b2", Money.ofPenny(3000), "Pepsi", BeverageSize.MEDIUM, false);

        MovieScreening screening = new MovieScreening(
                new Movie("Movie", 120, AgeRequirement.AGE_0),
                LocalDateTime.of(2026, 12, 18, 18, 0),
                new Theater("Harald", 8, 38));

        Ticket t1 = new Ticket("t1", Money.ofPenny(1400), new Seat('E',12), screening);
        Ticket t2 = new Ticket("t2", Money.ofPenny(1400), new Seat('E',13), screening);

        return new Item[]{f1, b1, b2, t1, t2};
    }

    @Test
    void foodItemAtExactly50KronorIsAccepted() {
    Item[] order = createSampleOrder();
    order[0] = new Food("f1", Money.ofPenny(5000), "popcorn");

    assertDoesNotThrow(() -> new ComboOrder(order));
}
}
