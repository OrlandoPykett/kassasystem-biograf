package com.mycompany.app;

import java.lang.Math;
import java.util.Objects;

public final class Money implements Comparable<Money> {
    public static final Money ZERO = new Money(0);

    private final long penny;

    private Money(long penny) {
        this.penny = penny;
    }

    public static Money ofPenny(long penny) {
        return new Money(penny);
    }

    public Money add(Money addend) {
        return add(addend.getPenny());
    }
    
    public Money add(long addend) {
        if (addend < 0) {
            throw new IllegalArgumentException("Addend cannot be negative");
        }

        return new Money(Math.addExact(this.penny, addend));
    }

    public Money subtract(Money subtrahend) {
        return subtract(subtrahend.getPenny());
    }
    
    public Money subtract(long subtrahend) {
        if (subtrahend < 0) {
            throw new IllegalArgumentException("Subtrahend cannot be negative");
        } 
        
        // TODO
        // for example penny=5 and subtrahend=14, penny - subtrahend = -9
        // allow subtrahend to be bigger than penny?
        else if (this.penny - subtrahend < 0) {
            throw new IllegalArgumentException("Subtrahend too big!");
        }
    
        return new Money(Math.subtractExact(this.penny, subtrahend));
    }

    public Money percentageOfPenny(float percentage) {
        if (percentage > 1 || percentage < 0) {
            throw new IllegalArgumentException("Percentage is not valid. Must be between 0 and 1");
        }

        // TODO
        // rounding?
        return new Money(Math.multiplyExact(penny, (long) percentage));
    }

    public long getPenny() {
        return this.penny;
    }

    public long getCrown() {
        return penny / 100;
    }

    @Override
    public int compareTo(Money other) {
        return (int) Math.subtractExact(this.penny, other.getPenny());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Money other) {
            return penny == other.getPenny();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this);
    }

    @Override
    public String toString() {
        return "Money: %d,%02d kr".formatted(getCrown(), getPenny());
    }
}