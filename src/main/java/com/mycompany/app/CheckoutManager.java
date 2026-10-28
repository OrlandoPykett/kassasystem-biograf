package com.mycompany.app;

import com.mycompany.app.items.Beverage;
import com.mycompany.app.items.Item;
import com.mycompany.app.items.Ticket;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckoutManager {

    private final StockManager stockManager;
    private final TicketManager ticketManager;

    public CheckoutManager(StockManager stockManager, TicketManager ticketManager) {
        this.stockManager = stockManager;
        this.ticketManager = ticketManager;
    }

    public boolean checkout(Receipt receipt, int customerAge) {
        if (receipt.isEmpty()) return false;

        Map<Item, Integer> itemQuantities = receipt.getItemQuantities();

        // Check that items are stocked
        Map<Item, Integer> stockQuantities = new HashMap<>();

        for (Item item : itemQuantities.keySet()) {
            int quantity = itemQuantities.get(item);

            if (item instanceof Beverage beverage) {
                quantity *= beverage.getVolumeMl();
            }

            stockQuantities.put(item, quantity);

            if (!stockManager.hasStock(item, itemQuantities.get(item))) {
                return false;
            }
        }

        // Purchase tickets
        if (!purchaseTickets(receipt.getTickets(), customerAge)) {
            return false;
        }

        // Remove purchased items from stock
        for (Item item : stockQuantities.keySet()) {
            stockManager.removeStock(item, stockQuantities.get(item));
        }

        return true;
    }

    private boolean purchaseTickets(List<Ticket> tickets, int customerAge) {
        if (tickets.isEmpty()) return true;

        Ticket[] ticketOrder = tickets.toArray(new Ticket[0]);
        int[] ages = new int[ticketOrder.length];

        Arrays.fill(ages, customerAge);

        return ticketManager.purchaseTickets(ticketOrder, ages);
    }
}