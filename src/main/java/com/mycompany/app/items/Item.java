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

    public abstract String getName();

    public String getId() {
        return id;
    }

    public Money getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;

        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }

        Item other = (Item) obj;
        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
