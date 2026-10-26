package com.mycompany.app.items;

import java.time.LocalDateTime;

public class MovieScreening {

    private final Movie movie;
    private final LocalDateTime startTime;
    private final Theater theater;

    public MovieScreening(Movie movie, LocalDateTime startTime, Theater theater) {
        this.movie = movie;
        this.startTime = startTime;
        this.theater = theater;
    }

    public Movie getMovie() {
        return movie;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return startTime.plusMinutes(movie.getDurationMin());
    }

    public Theater getTheater() {
        return theater;
    }

    @Override
    public String toString() {
        return "%s, at %s - %s, in %s"
                .formatted(movie, startTime, getEndTime(), theater);
    }
}