
package com.mycompany.app;

import com.mycompany.app.items.Item;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collection;
import java.util.Map;

public class StockManager {
    private final Map<Item, Integer> stock = new HashMap<>();

    public int getStock(Item item) {
        if (item == null) return 0;
        return stock.getOrDefault(item, 0);
    }

    public void addStock(Item item, int quantity) {
        if (item == null) return;
        if (quantity <= 0) return;

        int currentQuant = stock.getOrDefault(item, 0);
        stock.put(item, currentQuant + quantity);
    }

    public void removeStock(Item item, int quantity) {
        if (item == null) return;
        if (quantity <= 0) return;

        int currentQuant = stock.getOrDefault(item, 0);
        if (currentQuant < quantity) return;

        stock.put(item, currentQuant - quantity);
    }

    public boolean hasStock(Item item, int quantity) {
        if (item == null || quantity < 0) return false;
        return getStock(item) >= quantity;
    }

    public Collection<Item> getItems() {
        return new ArrayList<>(stock.keySet());
    }

    public Item getItem(String id) {
        if (id == null) return null;

        for (Item item : stock.keySet()) {
            if (item.getId().equalsIgnoreCase(id)) {
                return item;
            }
        }
        return null;
    }
}
