package com.mycompany.app.items;

import com.sun.source.tree.Tree;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

public class TheaterTest {

    @Test
    void theaterStoresCorrectSeats() {
        Theater theater = new Theater("Theater", 7, 25);
        TreeSet<Seat> seats = theater.getSeats();

        assertAll( () -> {
           assertEquals(7*25, seats.size());
           assertEquals('A', seats.first().getRow());
           assertEquals(1, seats.getFirst().getNumber());
           assertEquals('G', seats.getLast().getRow() );
           assertEquals(25, seats.getLast().getNumber());

           assertTrue(theater.hasSeat(new Seat('E', 23)));
           assertFalse(theater.hasSeat(new Seat('A', 32)));
           assertFalse(theater.hasSeat(new Seat('H', 9)));

        });
    }

    @Test
    void seatHasCorrectToString() {
        Seat seat = new Seat('E', 15);
        assertEquals("E15", seat.toString());
    }
}
