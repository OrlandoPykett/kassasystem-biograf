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
    
        return new Money(Math.subtractExact(this.penny, penny));
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
        return "Money: %L,%L kr".formatted(getCrown(), getPenny());
    }
}

//     // Factories: two ways to create, both end up as öre
//     public static Money ofOre(long ore) {

//     } // SEK

//   public static Money ofKronor(long kronor, long ore) // ofKronor(125, 50) -> 12550

//   public static Money of(long minorUnits)

//   public static Money zero()

//     // Arithmetic (all return new Money, all check same currency)
//   public Money add(Money other)        // use Math.addExact

//   public Money subtract(Money other)

//   public Money multiply(int quantity)  // 3 tickets x price

//   public Money percentage(int percent) // rounds HALF_UP to whole öre

//   public Money negate()

//     // Queries
//   public long getOre()                 // or getMinorUnits()

//   public long wholeKronor()            // ore / 100

//   public long remainderOre()           // ore % 100

//     public boolean isZero();

//     public boolean isNegative();

//     // toString -> "125,50 kr"
