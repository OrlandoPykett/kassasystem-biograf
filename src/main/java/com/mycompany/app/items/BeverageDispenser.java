package com.mycompany.app.items;

public class BeverageDispenser {

    private final String name;
    private int volumeMl;

    public BeverageDispenser(String name, int volumeMl) {
        if (volumeMl < 0) {
            throw new IllegalArgumentException("Volume cannot be negative");
        }

        this.name = name;
        this.volumeMl = volumeMl;
    }

    public String getName() {
        return name;
    }

    public int getVolume() {
        return volumeMl;
    }

    public boolean canDispense(int volumeMl) {
        return volumeMl > 0 && this.volumeMl >= volumeMl;
    }

    public boolean dispense(int volumeMl) {
        if (!canDispense(volumeMl)) return false;

        this.volumeMl -= volumeMl;
        return true;
    }

    public void refill(int volumeMl) {
        if (volumeMl <= 0) {
            throw new IllegalArgumentException("Refill volume must be positive");
        }

        this.volumeMl += volumeMl;
    }
}