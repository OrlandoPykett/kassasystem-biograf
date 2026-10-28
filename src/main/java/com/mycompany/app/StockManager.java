package com.mycompany.app;

import com.mycompany.app.items.Item;

import java.util.HashMap;
import java.util.Map;

public class StockManager {

    private final Map<String, Integer> stock = new HashMap<>();

    public int getStock(Item item) {
        if (item == null) return 0;
        return stock.getOrDefault(item.getId(), 0);
    }

    public void addStock(Item item, int quantity) {
        if (item == null) return;
        if (quantity <= 0) return;

        int currentQuant = stock.getOrDefault(item.getId(), 0);
        stock.put(item.getId(), currentQuant + quantity);
    }

    public void removeStock(Item item, int quantity) {
        if (item == null) return;
        if (quantity <= 0) return;

        int currentQuant = stock.getOrDefault(item.getId(), 0);

        if (currentQuant < quantity) return;

        stock.put(item.getId(), currentQuant - quantity);
    }

    public boolean hasStock(Item item, int quantity) {
        if (item == null || quantity < 0) return false;
        return getStock(item) >= quantity;
    }

}