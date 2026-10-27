package com.mycompany.app;

import com.mycompany.app.items.Food;
import org.junit.jupiter.api.Test;

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
}
