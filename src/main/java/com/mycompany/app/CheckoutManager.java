package com.mycompany.app;

import com.mycompany.app.items.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckoutManager {

    private final StockManager stockManager;

    public CheckoutManager(StockManager stockManager) {
        this.stockManager = stockManager;
    }

    public boolean checkout(List<Item> order) {

        Map<Item, Integer> itemQuantities = new HashMap<>();

        if (order.isEmpty()) return false;

        // Populate map
        for (Item item : order) {
            itemQuantities.merge(item, 1, Integer::sum);
        }

        // Check that items are stocked
        for (Item item : itemQuantities.keySet()) {
            if (!stockManager.hasStock(
                    item, itemQuantities.get(item))) {
                return false;
            }
        }

        return true;
    }
}
