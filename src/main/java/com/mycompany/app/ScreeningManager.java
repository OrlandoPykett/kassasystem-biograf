package com.mycompany.app;

import com.mycompany.app.items.Movie;
import com.mycompany.app.items.MovieScreening;
import com.mycompany.app.items.Theater;

import java.util.ArrayList;
import java.util.List;

public class ScreeningManager {

    private final List<MovieScreening> screenings = new ArrayList<>();


    public List<MovieScreening> getScreenings() {
        return new ArrayList<>(screenings);
    }

    public boolean addScreening(MovieScreening screening) {
        if (scheduleConflict(screening)) return false;

        return screenings.add(screening);
    }

    public boolean removeScreening(MovieScreening screening) {
        return screenings.remove(screening);
    }

    public List<MovieScreening> getScreeningsForMovie(Movie movie) {
        List<MovieScreening> movieScreenings = new ArrayList<>();

        for (MovieScreening screening : screenings) { // Doesn't work if multiple instances of the same movie are created
            if (screening.getMovie() == movie) {
                movieScreenings.add(screening);
            }
        }
        return movieScreenings;
    }

    public List<MovieScreening> getScreeningsInTheater(Theater theater) {
        List<MovieScreening> theaterScreenings = new ArrayList<>();

        for (MovieScreening screening : screenings) { // Doesn't work if multiple instances of the same movie are created
            if (screening.getTheater().equals(theater)) {
                theaterScreenings.add(screening);
            }
        }
        return theaterScreenings;
    }

    public boolean scheduleConflict(MovieScreening screening) {
        List<MovieScreening> sameTheater = getScreeningsInTheater(screening.getTheater());

        for (MovieScreening screening1 : sameTheater) {
            if( screening.getStartTime().isBefore(screening1.getEndTime())
                && screening1.getStartTime().isBefore(screening.getEndTime())) return true;
        }

        return false;
    }
}
