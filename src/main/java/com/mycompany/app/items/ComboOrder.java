package com.mycompany.app.items;

import com.mycompany.app.Money;

import java.util.ArrayList;
import java.util.List;

public class ComboOrder {

    private final Money price;
    private final Item[] items = new Item[5];
    private final ArrayList<Item> excess = new ArrayList<>();

    public ComboOrder(Item[] inputItems) {
        this.price = Money.ofPenny(40000);

        /*
        Populate items with:
        1 food with price of at least 50 kr
        2 beverages
        2 tickets

        Populate with the most expensive items of each category
         */
        int cheapestBeverage = 1;
        int cheapestTicket = 1;


        for (Item item : inputItems) {
            if (item instanceof Food) {
                if (item.getPrice().getPenny() < 5000) {
                    excess.add(item);
                } else if (items[0] == null) {
                    items[0] = item;
                } else if (item.getPrice().getPenny() > items[0].getPrice().getPenny()) {
                    excess.add(items[0]);
                    items[0] = item;
                } else {
                    excess.add(item);
                }

            } else if (item instanceof Beverage) {
                if (items[1] == null) {
                    items[1] = item;
                } else if (items[2] == null) {
                    items[2] = item;
                } else {
                    int cheapest = items[1].getPrice().getPenny()
                            <= items[2].getPrice().getPenny() ? 1 : 2;

                    if (item.getPrice().getPenny() > items[cheapest].getPrice().getPenny()) {
                        excess.add(items[cheapest]);
                        items[cheapest] = item;
                    } else {
                        excess.add(item);
                    }
                }

            } else if (item instanceof Ticket) {
                if (items[3] == null) {
                    items[3] = item;
                } else if (items[4] == null) {
                    items[4] = item;
                } else {
                    int cheapest = items[3].getPrice().getPenny()
                            <= items[4].getPrice().getPenny() ? 3 : 4;

                    if (item.getPrice().getPenny() > items[cheapest].getPrice().getPenny()) {
                        excess.add(items[cheapest]);
                        items[cheapest] = item;
                    } else {
                        excess.add(item);
                    }
                }
            }
        }


        // Throw error if items has not been populated
        for (Item item : items) {
            if (item == null) {
                throw new IllegalArgumentException("A combo must contain exactly 1 food item over 50 kr, 2 beverages, and 2 tickets");
            }
        }

    }

    public Money getPrice() {
        return price;
    }

    public Item[] getItems() {
        return items;
    }

    public List<Item> getExcess() {
        return List.copyOf(excess);
    }

}
