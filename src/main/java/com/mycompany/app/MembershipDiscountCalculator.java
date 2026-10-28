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

        // Fem procent rabatt: kunden betalar 95 procent.
        long product = Math.multiplyExact(
                item.getPrice().getPenny(), 95
        );

        long finalPenny = product / 100;

        // Avrunda till närmaste öre, halva ören uppåt.
        if (product % 100 >= 50) {
            finalPenny++;
        }

        return Money.ofPenny(finalPenny);
    }
}