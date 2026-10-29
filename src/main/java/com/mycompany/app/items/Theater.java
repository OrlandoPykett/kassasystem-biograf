package com.mycompany.app.items;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class Theater {

    private final String name;
    private final int numberOfRows;
    private final int seatsPerRow;
    private final TreeSet<Seat> seats = new TreeSet<>();

    public Theater(String name, int numberOfRows, int seatsPerRow) {
        this.name = name;
        this.numberOfRows = numberOfRows;
        this.seatsPerRow = seatsPerRow;

        for (int row = 0; row < numberOfRows; row++) {
            for (int i = 1; i < seatsPerRow + 1; i++) {
                seats.add(new Seat((char) ('A' + row), i));
            }
        }
    }

    public String getName() {
        return name;
    }

    public TreeSet<Seat> getSeats() {
        return new TreeSet<>(seats);
    }

    public boolean hasSeat(Seat seat) {
        return seats.contains(seat);
    }
}
