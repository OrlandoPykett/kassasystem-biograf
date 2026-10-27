package com.mycompany.app;

import com.mycompany.app.items.Food;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.mycompany.app.items.Beverage;
import com.mycompany.app.items.BeverageSize;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MembershipDiscountCalculatorTest {

    @Test
    void activeMemberGets20KronorDiscountOnFood() {
        Membership membership = new Membership("member1");
        Food food = new Food(
                "f1",
                Money.ofPenny(6000),
                "popcorn"
        );

        MembershipDiscountCalculator calculator = new MembershipDiscountCalculator();
        Money price = calculator.calculatePrice(food, membership);

        assertEquals(4000L, price.getPenny());
    }

    @Test
    void inactiveMemberGetsNoDiscountOnFood() {
    Membership membership = new Membership("member1");
    membership.setActiveStatus(false);

    Food food = new Food(
            "f1",
            Money.ofPenny(6000),
            "popcorn"
    );

    MembershipDiscountCalculator calculator =
            new MembershipDiscountCalculator();

    Money price = calculator.calculatePrice(food, membership);

    assertEquals(6000L, price.getPenny());
}
@Test
void activeMemberGetsNoDiscountOnAlcohol() {
    Membership membership = new Membership("member1");

    Beverage beer = new Beverage(
            "b1",
            Money.ofPenny(6000),
            "beer",
            BeverageSize.MEDIUM,
            true
    );

    MembershipDiscountCalculator calculator =
            new MembershipDiscountCalculator();

    Money price = calculator.calculatePrice(beer, membership);

    assertEquals(6000L, price.getPenny());
}

@Test
void discountCannotMakePriceNegative() {
    Membership membership = new Membership("member1");
    Food food = new Food(
            "f1",
            Money.ofPenny(1500),
            "snack"
    );

    MembershipDiscountCalculator calculator =
            new MembershipDiscountCalculator();

    Money price = calculator.calculatePrice(food, membership);

    assertEquals(0L, price.getPenny());
}

@ParameterizedTest
@CsvSource({
    "0,    0",
    "1999, 0",
    "2000, 0",
    "2001, 1"
})
void discountedFoodPriceNeverFallsBelowZero(
        long originalPenny, long expectedPenny) {

    Membership membership = new Membership("member1");
    Food food = new Food(
            "f1",
            Money.ofPenny(originalPenny),
            "snack"
    );

    MembershipDiscountCalculator calculator =
            new MembershipDiscountCalculator();

    Money price = calculator.calculatePrice(food, membership);

    assertEquals(expectedPenny, price.getPenny());
}

}
