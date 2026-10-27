package com.mycompany.app.items;

import com.mycompany.app.Money;
import com.mycompany.app.TicketManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TicketManagerTest {

    private TicketManager ticketManager;
    private MovieScreening screening;

    @BeforeEach
    void setUp() {
        ticketManager = new TicketManager(new AgeControl());

        Movie movie = new Movie(
                "Batman Begins",
                120,
                AgeRequirement.AGE_15
        );

        screening = new MovieScreening(
                movie,
                LocalDateTime.of(2026, 12, 10, 18, 0),
                new Theater("Torsten", 10, 30)
        );
    }

    @Test
    void rejectUnderageCustomerWithoutChangingTicketList() {
        Ticket ticket = new Ticket(
                "t1",
                Money.ofPenny(1800),
                new Seat('E', 15),
                screening
        );

        boolean result = ticketManager.purchaseTicket(ticket, 14);

        assertAll(
                () -> assertFalse(result, "Purchase should be rejected."),
                () -> assertEquals(
                        0,
                        ticketManager.getTickets().size(),
                        "A rejected purchase should not register a ticket."
                )
        );
    }

    @Test
    void acceptCustomerOfAgeAndRegisterTicket() {
        Ticket ticket = new Ticket(
                "t1",
                Money.ofPenny(1800),
                new Seat('E', 15),
                screening
        );

        boolean result = ticketManager.purchaseTicket(ticket, 15);

        assertAll(
                () -> assertTrue(result, "Purchase should be accepted"),
                () -> assertEquals(1, ticketManager.getTickets().size()),
                () -> assertSame(
                        ticket,
                        ticketManager.getTickets().getFirst(),
                        "Purchased ticket should be registered"
                )
        );
    }

    @Test
    void rejectPurchaseOfOccupiedSeat() {
        Ticket ticket1 = new Ticket(
                "t1",
                Money.ofPenny(1800),
                new Seat('E', 15),
                screening
        );

        Ticket ticket2 = new Ticket(
                "t2",
                Money.ofPenny(1800),
                new Seat('E', 15),
                screening
        );

        boolean firstResult = ticketManager.purchaseTicket(ticket1, 18);
        boolean secondResult = ticketManager.purchaseTicket(ticket2, 18);

        assertAll(
                () -> assertTrue(firstResult),
                () -> assertFalse(secondResult),
                () -> assertEquals(1, ticketManager.getTickets().size()),
                () -> assertSame(ticket1, ticketManager.getTickets().getFirst())
        );
    }

    @Test
    void acceptMultiTicketBooking() {
        Ticket ticket1 = new Ticket(
                "t1",
                Money.ofPenny(1800),
                new Seat('E', 15),
                screening
        );

        Ticket ticket2 = new Ticket(
                "t2",
                Money.ofPenny(1800),
                new Seat('E', 15),
                screening
        );

        Ticket[] tickets = new Ticket[]{ticket1, ticket2};
        int[] ages = new int[]{18, 18};

        boolean result = ticketManager.purchaseTickets(tickets, ages);
    }


    @Test
    void acceptsGroupWhenEveryoneMeetsAgeRequirement() {
        Ticket[] tickets = createTickets(
                new Seat('A', 1),
                new Seat('A', 2)
        );

        boolean result = ticketManager.purchaseTickets(
                tickets,
                new int[]{25, 16}
        );

        assertAll(
                () -> assertTrue(result),
                () -> assertEquals(2, ticketManager.getTickets().size()),
                () -> assertTrue(ticketManager.getTickets().containsAll(
                        Arrays.asList(tickets)
                ))
        );
    }

    @Test
    void rejects18PlusBookingWhenOneMemberIsUnder18() {
        screening = createScreening(AgeRequirement.AGE_18);

        Ticket[] tickets = createTickets(
                new Seat('A', 1),
                new Seat('A', 2)
        );

        boolean result = ticketManager.purchaseTickets(
                tickets,
                new int[]{25, 17}
        );

        assertAll(
                () -> assertFalse(result),
                () -> assertTrue(ticketManager.getTickets().isEmpty())
        );
    }

    @Test
    void rejectsRestrictedFilmBookingWhenNobodyIsAnAdult() {
        screening = createScreening(AgeRequirement.AGE_15);

        Ticket[] tickets = createTickets(
                new Seat('A', 1),
                new Seat('A', 2)
        );

        boolean result = ticketManager.purchaseTickets(
                tickets,
                new int[]{14, 12}
        );

        assertAll(
                () -> assertFalse(result),
                () -> assertTrue(ticketManager.getTickets().isEmpty())
        );
    }

    @Test
    void acceptsRestrictedFilmBookingWhenAdultAccompaniesUnderageMembers() {
        screening = createScreening(AgeRequirement.AGE_15);

        Ticket[] tickets = createTickets(
                new Seat('A', 1),
                new Seat('A', 2),
                new Seat('A', 3)
        );

        boolean result = ticketManager.purchaseTickets(
                tickets,
                new int[]{35, 14, 12}
        );

        assertAll(
                () -> assertTrue(result),
                () -> assertEquals(3, ticketManager.getTickets().size())
        );
    }

    @Test
    void rejectsEntireBookingWhenOneSeatIsOccupied() {
        Ticket[] firstOrder = createTickets(
                new Seat('B', 1),
                new Seat('B', 2)
        );

        assertTrue(ticketManager.purchaseTickets(
                firstOrder,
                new int[]{23, 23}
        ));

        Ticket[] secondOrder = createTickets(
                new Seat('B', 3),
                new Seat('B', 2)
        );

        boolean result = ticketManager.purchaseTickets(
                secondOrder,
                new int[]{23, 23}
        );

        assertAll(
                () -> assertFalse(result),
                () -> assertEquals(2, ticketManager.getTickets().size()),
                () -> assertTrue(
                        ticketManager.getTickets().stream()
                                .noneMatch(ticket -> ticket == secondOrder[0])
                )
        );
    }

    @Test
    void rejectsGroupBookingForTheSameSeat() {
        Ticket[] firstOrder = createTickets(
                new Seat('B', 1),
                new Seat('B', 2),
                new Seat('B', 3),
                new Seat('B', 4),
                new Seat('B', 5),
                new Seat('B', 2)
        );

        boolean result =ticketManager.purchaseTickets(
                firstOrder,
                new int[]{23, 23, 23, 23, 23, 23}
        );

        assertAll(
                () -> assertFalse(result),
                () -> assertTrue(ticketManager.getTickets().isEmpty())
        );
    }


    @Test
    void rejectsBookingContainingTicketsForDifferentMovies() {
        Ticket[] tickets = createTickets(
                new Seat('A', 1),
                new Seat('A', 2)
        );

        Movie otherMovie = new Movie(
                "Different Movie",
                100,
                AgeRequirement.AGE_15
        );

        tickets[1] = new Ticket(
                "t2",
                Money.ofPenny(1400),
                new Seat('A', 2),
                new MovieScreening(
                        otherMovie,
                        LocalDateTime.of(2026, 12, 18, 20, 0),
                        new Theater("Harald", 8, 38)
                )
        );

        boolean result = ticketManager.purchaseTickets(
                tickets,
                new int[]{25, 25}
        );

        assertAll(
                () -> assertFalse(result),
                () -> assertTrue(ticketManager.getTickets().isEmpty())
        );
    }

    @Test
    void rejectsBookingWithUnequalNumberOfTicketsAndAges() {
        Ticket[] tickets = createTickets(
                new Seat('B', 4),
                new Seat('B', 5)
        );

        int[] ages = new int[]{18};

        assertThrows(
                IllegalArgumentException.class,
                () -> ticketManager.purchaseTickets(tickets, ages)
        );
    }

    @Test
    void rejectEmptyBooking() {
        Ticket[] tickets = new Ticket[0];
        int[] ages = new int[0];

        assertThrows(
                IllegalArgumentException.class,
                () -> ticketManager.purchaseTickets(tickets, ages)
        );
    }


    private MovieScreening createScreening(AgeRequirement requirement) {
        Movie movie = new Movie("Movie", 120, requirement);

        return new MovieScreening(
                movie,
                LocalDateTime.of(2026, 12, 18, 18, 0),
                new Theater("Harald", 8, 38)
        );
    }

    private Ticket[] createTickets(Seat... seats) {
        Ticket[] tickets = new Ticket[seats.length];

        for (int i = 0; i < seats.length; i++) {
            tickets[i] = new Ticket(
                    "t" + (i + 1),
                    Money.ofPenny(1400),
                    seats[i],
                    screening
            );
        }

        return tickets;
    }


}
