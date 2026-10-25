package com.mycompany.app;

import com.mycompany.app.items.MovieScreening;
import com.mycompany.app.items.Seat;
import com.mycompany.app.items.Ticket;

import java.util.ArrayList;
import java.util.List;

public class TicketManager {

    private final List<Ticket> tickets = new ArrayList<>();


    public List<Ticket> getTickets() {
        return new ArrayList<>(tickets);
    }

    public boolean removeTicket(Ticket ticket) {
        return tickets.remove(ticket);
    }

    public boolean addTicket(Ticket ticket) {
        if (seatIsEmpty(ticket.getScreening(), ticket.getSeat())) {
            return tickets.add(ticket);
        }

        return false;
    }

    public boolean seatIsEmpty(MovieScreening screening, Seat seat) {
        for (Ticket ticket : tickets) {
            if (ticket.getScreening() == screening
                && ticket.getSeat() == seat) {
                return false;
            }
        }
        return true;
    }
}
