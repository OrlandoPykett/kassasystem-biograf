package com.mycompany.app.items;

import com.mycompany.app.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TicketTest {

    private Ticket ticket;
    private Movie movie;
    private MovieScreening screening;
    private Seat seat;
    private Theater theater;

    @BeforeEach
    void setUp() {
        movie = new Movie("Test Movie", 120, AgeRequirement.AGE_18);
        theater = new Theater("Harald", 25, 60);
        seat = new Seat('D', 10);
        screening = new MovieScreening(
                movie,
                LocalDateTime.of(2026, 12, 12, 18, 18, 0),
                theater
        );

        ticket = new Ticket(
                "t1",
                Money.ofPenny(500),
                seat,
                screening
        );
    }

    @Test
    void constructorStoresCorrectProperties() {
        assertAll(
                () -> assertEquals("t1", ticket.getId()),
                () -> assertEquals(Money.ofPenny(500), ticket.getPrice()),
                () -> assertSame(seat, ticket.getSeat()),
                () -> assertSame(screening, ticket.getScreening())
        );
    }

    @Test
    void getScreeningReturnsCorrectScreening() {
        assertSame(screening, ticket.getScreening());
    }

    @Test
    void getMovieReturnsMovieFromScreening() {
        assertSame(movie, ticket.getMovie());
    }

    @Test
    void getSeatReturnsCorrectSeat() {
        assertSame(seat, ticket.getSeat());
    }

    @Test
    void getNameReturnsMovieTitle() {
        assertEquals("Test Movie", ticket.getName());
    }

    @Test
    void customerIsOfAgeWhenMeetingAgeRequirement() {
        assertTrue(ticket.customerIsOfAge(18));
        assertTrue(ticket.customerIsOfAge(25));
    }

    @Test
    void customerIsNotOfAgeWhenBelowAgeRequirement() {
        assertFalse(ticket.customerIsOfAge(17));
        assertFalse(ticket.customerIsOfAge(0));
    }

    @Test
    void toStringContainsMovieScreeningSeatAndPrice() {
        String result = ticket.toString();

        assertEquals(
                "Movie: %s, Seat: %s, Price: %s".formatted(
                        screening.toString(),
                        seat.toString(),
                        ticket.getPrice()
                ),
                result
        );
    }

    @Test
    void ticketsWithSameIdAreEqual() {
        Ticket other = new Ticket(
                "t1",
                Money.ofPenny(1000),
                new Seat('A', 1),
                screening
        );

        assertEquals(ticket, other);
    }

    @Test
    void ticketsWithDifferentIdsAreNotEqual() {
        Ticket other = new Ticket(
                "t2",
                Money.ofPenny(500),
                seat,
                screening
        );

        assertNotEquals(ticket, other);
    }

    @Test
    void ticketIsEqualToItself() {
        assertEquals(ticket, ticket);
    }

    @Test
    void ticketIsNotEqualToNull() {
        assertNotEquals(ticket, null);
    }

    @Test
    void ticketsWithDifferentClassesAreNotEqual() {
        Food food = new Food(
                "t1",
                Money.ofPenny(500),
                "Test Movie"
        );

        assertNotEquals(ticket, food);
    }

    @Test
    void equalTicketsHaveEqualHashCodes() {
        Ticket other = new Ticket(
                "t1",
                Money.ofPenny(1000),
                new Seat('A', 1),
                screening
        );

        assertEquals(ticket.hashCode(), other.hashCode());
    }
}