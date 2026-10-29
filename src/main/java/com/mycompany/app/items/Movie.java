package com.mycompany.app.items;

import com.mycompany.app.Money;

public class Movie {

    private final String title;
    private final int durationMin;
    private final AgeRequirement ageReq;
    private final Money basePrice;

    public int[] screenings;

    // anrop fungerar fortfarande och får standardpriset 120 kr.
    public Movie(String title, int durationMin, AgeRequirement ageReq) {
        this(title, durationMin, ageReq, Money.ofPenny(12000));
    }

    // Den här konstruktorn låter varje film få ett eget grundpris.
    public Movie(String title,int durationMin,AgeRequirement ageReq,Money basePrice) {

        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Movie title cannot be empty");
        }

        if (durationMin <= 0) {
            throw new IllegalArgumentException("Movie length must be greater than 0");
        }

        if (basePrice == null || basePrice.getPenny() < 0) {
            throw new IllegalArgumentException("Movie base price must exist and cannot be negative");
        }

        this.title = title;
        this.durationMin = durationMin;

        // Saknad åldersgräns innebär att filmen är tillåten för alla.
        this.ageReq = ageReq == null ? AgeRequirement.AGE_0 : ageReq;

        this.basePrice = basePrice;
    }

    public String getTitle() {
        return title;
    }

    public int getDurationMin() {
        return durationMin;
    }

    public AgeRequirement getAgeReq() {
        return ageReq;
    }

    // Prisberäkningen kan läsa filmens grundpris här.
    public Money getBasePrice() {
        return basePrice;
    }

    public boolean isSuitableFor(int age) {
        return ageReq.isOfAge(age);
    }

    public int[] getScreenings() {
        return screenings;
    }

    @Override
    public String toString() {
        return title;
    }
}
