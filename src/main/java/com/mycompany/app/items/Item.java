package com.mycompany.app.items;

import com.mycompany.app.Money;

import java.util.Objects;

public abstract class Item {
    private final String id;
    private final Money price;

    public Item(String id, Money price) {
        if(price.getPenny() < 0){
            throw new IllegalArgumentException(
                "Price cannot be negative"
            );
        }
        this.id = id;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public Money getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Item other) {
            return obj == other.getId();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this);
    }
}
