package com.mycompany.app;

import com.mycompany.app.items.Item;

public class MembershipDiscountCalculator {

    public Money calculatePrice(Item item, Membership membership) {
    if (!membership.isActive()) {
        return item.getPrice();
    }

    return item.getPrice().subtract(Money.ofPenny(2000));
}
}
