package com.mycompany.app;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

public class MoneyTest {

    private Money getDefaultMoney() {
        return Money.ofPenny(10);
    }

    @Test
    public void money_subtraction_is_correct() {
        Money money = Money.ofPenny(3);
        Money threeMoney = Money.ofPenny(3);

        assertEquals(0, money.subtract(threeMoney).getPenny());
    }

    @Test
    public void money_addition_is_correct() {
        Money money = Money.ofPenny(0);
        Money threeMoney = Money.ofPenny(3);

        assertEquals(3, money.add(threeMoney).getPenny());
    }

    @Test
    public void negative_addend_throws_exception() {
        Money anyMoney = getDefaultMoney();
        assertThrows(IllegalArgumentException.class, () -> {
            anyMoney.add(-1);
        });
    }

    @Test
    public void negative_subtrahend_throws_exception() {
        Money anyMoney = getDefaultMoney();
        assertThrows(IllegalArgumentException.class, () -> {
            anyMoney.subtract(-1);
        });
    }

    @Test
    public void money_subration_to_below_zero_throws_exception() {
        long moneyValue = 5;
        Money money = Money.ofPenny(moneyValue);

        assertThrows(IllegalArgumentException.class, () -> {
            money.subtract(moneyValue + 1);
        });
    }

    @Test
    public void money_percentage_below_zero_throws_exception() {
        Money money = getDefaultMoney();
        assertThrows(IllegalArgumentException.class, () -> {
            money.percentageOfPenny((float) -0.1);
        });
    }

    @Test
    public void money_percentage_above_one_throws_exception() {
        Money money = getDefaultMoney();
        assertThrows(IllegalArgumentException.class, () -> {
            money.percentageOfPenny((float) 1.1);
        });
    }

    @Test
    public void money_percentage_is_correct() {
        long moneyValue = 10;
        Money money = Money.ofPenny(moneyValue);
        float percentage = (float) 0.4;
        long expected = Math.multiplyExact(moneyValue, (long) percentage);
        long actual = money.percentageOfPenny(percentage).getPenny();

        assertEquals(expected, actual);
    }

    @Test
    public void penny_is_set_correctly() {
        long moneyValue = 5;
        Money money = Money.ofPenny(moneyValue);

        assertEquals(moneyValue, money.getPenny());
    }

    @Test
    public void crown_is_calculated_correctly() {
        long moneyValue = 5;
        Money money = Money.ofPenny(moneyValue);

        long expected = 5 / 100;
        long actual = money.getCrown();

        assertEquals(expected, actual);
    }

    @Test
    public void money_is_compared_correctly() {
        Money oneMoney = Money.ofPenny(1);
        Money twoMoney = Money.ofPenny(2);
        Money threeMoney = Money.ofPenny(3);

        ArrayList<Money> expected = new ArrayList<>(Arrays.asList(oneMoney, twoMoney, threeMoney));
        ArrayList<Money> actual = new ArrayList<>(Arrays.asList(twoMoney, threeMoney, oneMoney));

        Collections.sort(actual);

        assertEquals(expected, actual);
    }

    @Test
    public void money_with_same_value_is_equal() {
        long moneyValue = 3;
        Money money1 = Money.ofPenny(moneyValue);
        Money money2 = Money.ofPenny(moneyValue);

        assertAll(
            () -> assertTrue(money1.equals(money2)),
            () -> assertEquals(money1.hashCode(), money2.hashCode())
        );
    }
    
    @Test
    public void money_with_different_value_is_not_equal() {
        Money money1 = Money.ofPenny(3);
        Money money2 = Money.ofPenny(1);
    
        assertAll(
            () -> assertFalse(money1.equals(money2)),
            () -> assertFalse(money1.equals(new Object())),
            () -> assertNotEquals(money1.hashCode(), money2.hashCode())
        );
    }
}
