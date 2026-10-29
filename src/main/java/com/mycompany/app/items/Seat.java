package com.mycompany.app.items;

import java.util.Objects;
import java.util.Set;

public class Seat implements Comparable<Seat>{

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

    @Override
    public int compareTo(Seat other) {
        if (this.row != other.row) {
            return Character.compare(this.row, other.row);
        }
        return Integer.compare(this.number, other.number);
    }
}
