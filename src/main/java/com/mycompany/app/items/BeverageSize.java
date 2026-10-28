package com.mycompany.app.items;

public enum BeverageSize {
    SMALL(330),
    MEDIUM(750),
    LARGE(1000);

    private final int volumeMl;

    BeverageSize(int volumeMl) {
        this.volumeMl = volumeMl;
    }

    public int getVolumeMl() {
        return volumeMl;
    }
}
