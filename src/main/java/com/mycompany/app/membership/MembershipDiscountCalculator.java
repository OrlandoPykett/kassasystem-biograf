package com.mycompany.app.membership;

import com.mycompany.app.Money;
import com.mycompany.app.items.AgeRequirement;
import com.mycompany.app.items.Beverage;
import com.mycompany.app.items.Item;

public class MembershipDiscountCalculator {

    // Hur många procent av originalpriset kunden betalar.
    private static final int STANDARD_PAY_PERCENT = 95;
    private static final int PREMIUM_PAY_PERCENT = 90;
    private static final int VIP_PAY_PERCENT = 85;

    // Beräknar medlemspriset utan att ändra varans originalpris.
    public Money calculatePrice(Item item, Membership membership) {

        // Saknat eller inaktivt medlemskap ger ingen rabatt.
        if (membership == null || !membership.isActive()) {
            return item.getPrice();
        }

        // Alkohol har AGE_18 i vår nuvarande dryckesmodell.
        if (item instanceof Beverage beverage
                && beverage.getAgeReq() == AgeRequirement.AGE_18) {
            return item.getPrice();
        }

        // STANDARD får 5 %, PREMIUM 10 % och VIP 15 % rabatt.
        int payPercent = STANDARD_PAY_PERCENT;

        if (membership.getMembershipTier() == MembershipTier.PREMIUM) {
            payPercent = PREMIUM_PAY_PERCENT;
        } else if (membership.getMembershipTier() == MembershipTier.VIP) {
            payPercent = VIP_PAY_PERCENT;
        }

        // Multiplicera först för att behålla precisionen i hela ören.
        long product = Math.multiplyExact(
                item.getPrice().getPenny(), payPercent
        );

        long finalPenny = product / 100;

        // Resten 50 motsvarar ett halvt öre. Då avrundar vi uppåt.
        if (product % 100 >= 50) {
            finalPenny++;
        }

        return Money.ofPenny(finalPenny);
    }
}