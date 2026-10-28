package com.mycompany.app;

import com.mycompany.app.items.AgeRequirement;
import com.mycompany.app.items.Beverage;
import com.mycompany.app.items.Item;

public class MembershipDiscountCalculator {

    public Money calculatePrice(Item item, Membership membership) {
        // Inaktivt medlemskap ger ingen rabatt.
        if (!membership.isActive()) {
            return item.getPrice();
        }

        // Alkohol ger ingen medlemsrabatt.
        if (item instanceof Beverage beverage
                && beverage.getAgeReq() == AgeRequirement.AGE_18) {
            return item.getPrice();
        }

        int payablePercent = 95;

        if (membership.getMembershipTier() == MembershipTier.PREMIUM) {
            payablePercent = 90;
        }

        long product = Math.multiplyExact(
                item.getPrice().getPenny(), payablePercent
        );

        long finalPenny = product / 100;

        // Avrunda till närmaste öre, halva ören uppåt.
        if (product % 100 >= 50) {
            finalPenny++;
        }

        return Money.ofPenny(finalPenny);
    }
}