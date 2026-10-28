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

        // STANDARD får 5 procent rabatt och betalar därför 95 procent.
        // Aktiva PREMIUM- och VIP-medlemmar får högre rabatt.
        int payablePercent = 95;

        if (membership.getMembershipTier() == MembershipTier.PREMIUM) {
            payablePercent = 90;
        } else if (membership.getMembershipTier() == MembershipTier.VIP) {
            payablePercent = 85;
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