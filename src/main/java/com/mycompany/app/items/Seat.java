package com.mycompany.app.items;

import java.util.Objects;

public class Seat {

    private final char row;
    private final int number;

    public Seat(char row, int number) {
        this.row = row;
        this.number = number;
    }

    public char getRow() {
        return row;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return row + Integer.toString(number);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Seat other)) return false;
        return row == other.row && number == other.number;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, number);
    }
}
