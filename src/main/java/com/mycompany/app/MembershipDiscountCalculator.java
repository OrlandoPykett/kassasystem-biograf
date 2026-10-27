package com.mycompany.app;

import com.mycompany.app.items.Item;

public class MembershipDiscountCalculator {

    public Money calculatePrice(Item item, Membership membership) {
        return item.getPrice();
    }
}
