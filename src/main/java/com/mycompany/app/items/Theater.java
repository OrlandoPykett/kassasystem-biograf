package com.mycompany.app.items;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Theater {

    private final String name;
    private final int numberOfRows;
    private final int seatsPerRow;
    private final Set<Seat> seats =  new HashSet<>();

    public Theater(String name, int numberOfRows, int seatsPerRow) {
        this.name = name;
        this.numberOfRows = numberOfRows;
        this.seatsPerRow = seatsPerRow;

        for (int row = 0; row < numberOfRows; row++) {
            for (int i = 0; i < seatsPerRow; i++) {
                seats.add(new Seat((char) ('A' + row), i));
            }
        }
    }

    public String getName() {
        return name;
    }

    public Set<Seat> getSeats() {
        return Collections.unmodifiableSet(seats);
    }

    public boolean hasSeat(Seat seat) {
        return seats.contains(seat);
    }
}
