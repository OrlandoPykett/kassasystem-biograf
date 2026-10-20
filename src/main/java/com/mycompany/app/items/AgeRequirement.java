package com.mycompany.app.items;

public enum AgeRequirement {
    AGE_0(0),
    AGE_16(16),
    AGE_18(18);

    private final int minimumAge;

    AgeRequirement(int minimumAge) {
        this.minimumAge = minimumAge;
    }

    public int getMinimumAge() {
        return minimumAge;
    }

    public boolean isOfAge(int age) {
        return age >= minimumAge;
    }

}
