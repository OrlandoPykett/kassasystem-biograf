package com.mycompany.app.items;

public class Movie {

    private final String title;
    private final int durationMin;
    private final AgeRequirement ageReq;

    public int[] screenings;

    public Movie(String title, int durationMin, AgeRequirement ageReq) {
        this.title = title;
        this.durationMin = durationMin;
        this.ageReq = ageReq;

        if (this.title == null || title.isBlank()) {
            throw new IllegalArgumentException(
                    "Movie title cannot be empty"
            );
        }

        if (durationMin <= 0) {
            throw new IllegalArgumentException(
                    "Movie length must be greater than 0"
            );
        }

        // If a movie is created without an age requirement, ageReq is set to 0
        if (ageReq == null) {
            ageReq = AgeRequirement.AGE_0;
        }
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
