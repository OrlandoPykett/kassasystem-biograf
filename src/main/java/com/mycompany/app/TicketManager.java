package com.mycompany.app;

import com.mycompany.app.items.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TicketManager {

    private final List<Ticket> tickets = new ArrayList<>();

    public TicketManager() {
    }

    public List<Ticket> getTickets() {
        return new ArrayList<>(tickets);
    }

    public boolean purchaseTicket(Ticket ticket, int custAge) {

        boolean isOfAge = ticket.getMovie().getAgeReq().isOfAge(custAge);

        if (isOfAge) {
            return addTicket(ticket);
        }

        return false;
    }

    public boolean purchaseTickets(Ticket[] ticketOrder, int[] ages) {
        /*
        Group booking age requirement rules:

        18+ uses all or nothing approach:
        every member in the group must be at least 18

        Lower age requirement use "adult company" approach:
        if any member in the group is at least 18,
        the purchase is accepted

        ---

        All or nothing approach for seat occupation
        All seats must be empty to accept purchase

        ---
        All tickets must be for the same screening
         */

        // Reject empty orders
        if (ticketOrder.length == 0) {
            throw new IllegalArgumentException(
                    "Ticket purchase must include tickets"
            );
        }

        // Number of tickets must equal to number of ages
        if (ticketOrder.length != ages.length ) {
            throw new IllegalArgumentException(
                    "Number of tickets must match number of ages"
            );
        }

        Movie movie = ticketOrder[0].getMovie();
        MovieScreening screening = ticketOrder[0].getScreening();
        // Reject order if all ticket screenings are not the same
        for (int i = 1; i < ticketOrder.length; i++) {
            if (!ticketOrder[i].getScreening().equals(screening)) {
                return false;
            }
        }

        // Find age of youngest person in group
        int youngestBooker = ages[0];
        boolean someoneIsAdult = false;

        for (int age : ages) {
            if (age < youngestBooker) {
                youngestBooker = age;
            }
            if (age >= 18) someoneIsAdult = true;
        }

        // Reject booking if age requirement is not met
        if( !movie.isSuitableFor(youngestBooker) ) {
            // case 18+: all or nothing
            if (movie.getAgeReq() == AgeRequirement.AGE_18) {
                return false;
            }
            else if (!someoneIsAdult) return false;
        }

        // Ensure seats are empty
        for(int i = 0; i < ticketOrder.length; i++) {
            // Seat is not occupied by other order
            if (!seatIsEmpty(
                    ticketOrder[i].getScreening(),
                    ticketOrder[i].getSeat()
            )) {
                return false;
            }
            // Seat is not a duplicate in current order
            for (int n = i + 1; n < ticketOrder.length; n++) {
                if (
                        ticketOrder[i].getSeat().equals(
                                ticketOrder[n].getSeat()
                        )
                ) {
                    return false;
                }
            }
        }

        addTickets(ticketOrder);

        return true;
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

    private void addTickets(Ticket[] ticketOrder) {
        Collections.addAll(tickets, ticketOrder);
    }

    public boolean seatIsEmpty(MovieScreening screening, Seat seat) {
        for (Ticket ticket : tickets) {
            if (ticket.getScreening() == screening
                && ticket.getSeat().equals(seat)) {
                return false;
            }
        }
        return true;
    }
}
