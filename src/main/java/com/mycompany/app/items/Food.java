package com.mycompany.app.items;

import com.mycompany.app.Money;

public class Food extends Item {
    private final String name;

    public Food(String id,Money price, String name) {
        super(id, price);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
