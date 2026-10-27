package com.mycompany.app;

import com.mycompany.app.items.Item;
import com.mycompany.app.items.Beverage;
import com.mycompany.app.items.AgeRequirement;


public class MembershipDiscountCalculator {

    public Money calculatePrice(Item item, Membership membership) {
    if (!membership.isActive()) {
        return item.getPrice();
    }

     if (item instanceof Beverage beverage
            && beverage.getAgeReq() == AgeRequirement.AGE_18) {
        return item.getPrice();
    }

    if (item.getPrice().getPenny() <= 2000) {
    return Money.ZERO;
}

    return item.getPrice().subtract(Money.ofPenny(2000));
}


}
