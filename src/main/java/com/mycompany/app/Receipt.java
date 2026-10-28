package com.mycompany.app;

import com.mycompany.app.items.Item;
import com.mycompany.app.items.MovieScreening;
import com.mycompany.app.items.Ticket;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

 // TODO discounts

public class Receipt {

    private static final int DESCRIPTION_WIDTH = 30;
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private abstract static class Entry {
        final String description;
        final long unitPrice;
        int quantity = 1;

        Entry(String description, long unitPrice) {
            this.description = description;
            this.unitPrice = unitPrice;
        }

        //TODO Implement discounts
        long total() {
            return Math.multiplyExact(unitPrice, quantity);
        }
    }

    private static class ItemEntry extends Entry {
        final Item item;

        ItemEntry(String description, long unitPrice, Item item) {
            super(description, unitPrice);
            this.item = item;
        }
    }

    private static class TicketEntry extends Entry {
        final Ticket ticket;
        final String detail;

        TicketEntry(Ticket ticket) {
            super("Ticket " + ticket.getName() + " " + ticket.getSeat(), ticket.getPrice().getPenny());
            this.ticket = ticket;

            MovieScreening screening = ticket.getScreening();
            this.detail = screening.getTheater().getName() + ", " + screening.getStartTime().format(TIME_FORMAT);
        }
    }

    private final List<Entry> entries = new ArrayList<>();
    private final Clock clock;
    private boolean locked = false;

    public Receipt() {
        this(Clock.systemDefaultZone());
    }

    public Receipt(Clock clock) {
        this.clock = clock;
    }


    public void addItem(Item item, String description) {
        checkNotLocked();
        ItemEntry existing = findItemEntry(item);
        if (existing != null) {
            existing.quantity++;
            entries.remove(existing);
            entries.add(existing);
            return;
        }
        entries.add(new ItemEntry(description, item.getPrice().getPenny(), item));
    }

    public void addTicket(Ticket ticket) {
        checkNotLocked();
        if (hasTicket(ticket)) {
            throw new IllegalArgumentException("That ticket is already on the receipt");
        }
        entries.add(new TicketEntry(ticket));
    }

    public boolean removeItem(String id) {
        checkNotLocked();
        return entries.removeIf(e ->
                (e instanceof ItemEntry itemEntry && itemEntry.item.getId().equalsIgnoreCase(id))
                        || (e instanceof TicketEntry ticketEntry && ticketEntry.ticket.getId().equalsIgnoreCase(id)));
    }

    public void lock() {
        locked = true;
    }

    public boolean isEmpty() {
        return entries.isEmpty();
    }

    public int countOf(Item item) {
        ItemEntry entry = findItemEntry(item);
        return entry == null ? 0 : entry.quantity;
    }

    public boolean hasTicket(Ticket ticket) {
        for (Entry entry : entries) {
            if (entry instanceof TicketEntry ticketEntry && ticketEntry.ticket.equals(ticket)) {
                return true;
            }
        }
        return false;
    }

    public Money subtotal() {
        long sum = 0;
        for (Entry entry : entries) {
            sum = Math.addExact(sum, entry.total());
        }
        return Money.ofPenny(sum);
    }

    public Money total() {
        return subtotal();      // TODO: subtract discounts, never below 0
    }

    public Map<Item, Integer> getItemQuantities() {
        Map<Item, Integer> result = new LinkedHashMap<>();
        for (Entry entry : entries) {
            if (entry instanceof ItemEntry itemEntry) {
                result.put(itemEntry.item, itemEntry.quantity);
            }
        }
        return result;
    }

    public List<Ticket> getTickets() {
        List<Ticket> result = new ArrayList<>();
        for (Entry entry : entries) {
            if (entry instanceof TicketEntry ticketEntry) {
                result.add(ticketEntry.ticket);
            }
        }
        return result;
    }

    public List<Item> getAllItems() {
        List<Item> result = new ArrayList<>();
        for (Entry entry : entries) {
            if (entry instanceof ItemEntry itemEntry) {
                for (int i = 0; i < itemEntry.quantity; i++) {
                    result.add(itemEntry.item);
                }
            } else if (entry instanceof TicketEntry ticketEntry) {
                result.add(ticketEntry.ticket);
            }
        }
        return result;
    }


    public String format() {
        StringBuilder sb = new StringBuilder("=== RECEIPT ===\n");
        sb.append(LocalDateTime.now(clock).format(TIME_FORMAT)).append('\n');

        List<Entry> sorted = new ArrayList<>(entries);
        sorted.sort(Comparator.comparing(e -> e.description.toLowerCase()));

        for (Entry entry : sorted) {
            sb.append(String.format("%-" + DESCRIPTION_WIDTH + "s %3d %12s%n",
                    shorten(entry.description), entry.quantity, money(entry.total())));
            if (entry instanceof TicketEntry ticket) {
                sb.append("  ").append(ticket.detail).append('\n');
            }
        }

        sb.append("-".repeat(DESCRIPTION_WIDTH + 17)).append('\n');
        sb.append(String.format("%-" + DESCRIPTION_WIDTH + "s %16s%n", "TOTAL", money(total().getPenny())));
        return sb.toString();
    }

    public static String format(Money money) {
        return money(money.getPenny());
    }

    private static String money(long penny) {
        return String.format("%d,%02d kr", penny / 100, Math.abs(penny % 100));
    }

    private String shorten(String text) {
        return text.length() <= DESCRIPTION_WIDTH ? text : text.substring(0, DESCRIPTION_WIDTH - 1) + "…";
    }

    private ItemEntry findItemEntry(Item item) {
        for (Entry entry : entries) {
            if (entry instanceof ItemEntry itemEntry && itemEntry.item.equals(item)) {
                return itemEntry;
            }
        }
        return null;
    }

    private void checkNotLocked() {
        if (locked) {
            throw new IllegalStateException("The receipt is locked");
        }
    }
}