package com.mycompany.app.items;

import com.mycompany.app.Money;

import java.util.Objects;

public abstract class Item {
    private final String id;
    private final String name;
    private final Money price;

    public Item(String id, String name, Money price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
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

    @Override
    public String toString() {
        return "id=%s,name=%s,price=%L".formatted(getId(), getName(), getPrice());
    }
}
