package com.mycompany.app.items;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SeatTest {

    @Test
    void seatsWithSameRowAndNumberAreEqual() {
        Seat seat1 = new Seat('A', 1);
        Seat seat2 = new Seat('A', 1);

        assertEquals(seat1, seat2);
    }

    @Test
    void seatIsEqualToItself() {
        Seat seat = new Seat('A', 1);

        assertEquals(seat, seat);
    }

    @Test
    void seatsWithDifferentRowsAreNotEqual() {
        Seat seat1 = new Seat('A', 1);
        Seat seat2 = new Seat('B', 1);

        assertNotEquals(seat1, seat2);
    }

    @Test
    void seatsWithDifferentNumbersAreNotEqual() {
        Seat seat1 = new Seat('A', 1);
        Seat seat2 = new Seat('A', 2);

        assertNotEquals(seat1, seat2);
    }

    @Test
    void seatIsNotEqualToNull() {
        Seat seat = new Seat('A', 1);

        assertNotEquals(seat, null);
    }

    @Test
    void seatIsNotEqualToDifferentObjectType() {
        Seat seat = new Seat('A', 1);

        assertNotEquals(seat, "A1");
    }

    @Test
    void equalSeatsHaveSameHashCode() {
        Seat seat1 = new Seat('A', 1);
        Seat seat2 = new Seat('A', 1);

        assertEquals(seat1.hashCode(), seat2.hashCode());
    }

    @Test
    void seatsCanBeFoundInHashSet() {
        Seat seat1 = new Seat('A', 1);
        Seat seat2 = new Seat('A', 1);

        Set<Seat> seats = new HashSet<>();
        seats.add(seat1);

        assertTrue(seats.contains(seat2));
    }
}