
package com.mycompany.app;

import com.mycompany.app.items.Beverage;
import com.mycompany.app.items.Item;

import java.util.*;

public class StockManager {
    private final Map<Item, Integer> stock = new HashMap<>();
    private final Map<String, Item> items = new HashMap<>();

    public int getStock(Item item) {
        if (item == null) return 0;

        if (item instanceof Beverage beverage) {
            return beverage.getDispenser().getVolume();
        }

        return stock.getOrDefault(item, 0);
    }

    public Collection<Item> getItems() {
        return new ArrayList<>(items.values());
    }

    public Set<String> getItemIds() {
        return items.keySet();
    }

    public Item getItem(String id) {
        if (id == null) return null;
        return items.get(id);
    }

    public void registerItem(Item item) {
        if (item == null) return;

        items.put(item.getId(), item);
    }

    public void addStock(Item item, int quantity) {
        if (item == null || quantity <= 0) return;

        if (item instanceof Beverage) {
            registerItem(item);
            return;
        }

        registerItem(item);

        int currentQuant = stock.getOrDefault(item, 0);
        stock.put(item, currentQuant + quantity);
    }

    public void removeStock(Item item, int quantity) {
        if (item == null || quantity <= 0) return;

        if (item instanceof Beverage beverage) {
            int volumeMl = quantity * beverage.getVolumeMl();
            beverage.getDispenser().dispense(volumeMl);
            return;
        }

        int currentQuant = stock.getOrDefault(item, 0);
        if (currentQuant < quantity) return;

        stock.put(item, currentQuant - quantity);
    }

    public boolean hasStock(Item item, int quantity) {
        if (item == null || quantity < 0) return false;

        if (item instanceof Beverage beverage) {
            int volumeMl = quantity * beverage.getVolumeMl();
            return quantity == 0 || beverage.getDispenser().canDispense(volumeMl);
        }

        return getStock(item) >= quantity;
    }
}
