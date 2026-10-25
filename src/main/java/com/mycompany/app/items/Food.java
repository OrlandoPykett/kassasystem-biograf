package com.mycompany.app.items;

import com.mycompany.app.Money;

import java.util.Objects;

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
