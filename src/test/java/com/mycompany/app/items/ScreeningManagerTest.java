package com.mycompany.app.items;

import com.mycompany.app.ScreeningManager;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ScreeningManagerTest {

    LocalDateTime exampleTime = LocalDateTime.of(2026, 12, 18, 18, 0);
    Theater exampleTheater = new Theater("Harald", 8, 38);

    private MovieScreening createScreening(Movie movie) {
        return new MovieScreening(
                movie,
                exampleTime,
                exampleTheater
        );
    }

    @Test
    void addScreeningStoresScreening() {
        // Arrange
        ScreeningManager manager = new ScreeningManager();
        Movie movie = new Movie("Avengers", 120, AgeRequirement.AGE_0);
        Theater theater = new Theater("Harald", 25, 60);
        MovieScreening screening = new MovieScreening(movie, LocalDateTime.of(2026, 10, 20, 14, 0), theater);

        // Act
        boolean result = manager.addScreening(screening);

        // Assert
        assertTrue(result);
        assertEquals(1, manager.getScreenings().size());
    }

    @Test
    void addScreeningRejectsOverlappingScreenings() {
        ScreeningManager manager = new ScreeningManager();

        Movie avengers = new Movie("Avengers", 120, AgeRequirement.AGE_0);
        Movie endgame = new Movie("Endgame", 180, AgeRequirement.AGE_0);

        Theater theater = new Theater("Harald", 25, 60);

        MovieScreening screening1 = new MovieScreening(avengers, LocalDateTime.of(2026, 10, 20, 14, 0), theater);
        MovieScreening screening2 = new MovieScreening(endgame, LocalDateTime.of(2026, 10, 20, 15, 0), theater);

        manager.addScreening(screening1);
        boolean result = manager.addScreening(screening2);

        assertFalse(result);
        assertEquals(1, manager.getScreenings().size());
    }

    @Test
    void addScreeningRemovesScreening() {
        ScreeningManager manager = new ScreeningManager();
        Movie movie = new Movie("Avengers", 120, AgeRequirement.AGE_0);
        Theater theater = new Theater("Harald", 25, 60);
        MovieScreening screening = new MovieScreening(movie, LocalDateTime.of(2026, 10, 20, 14, 0), theater);

        manager.addScreening(screening);
        boolean result = manager.removeScreening(screening);

        assertTrue(result);
        assertEquals(0, manager.getScreenings().size());
    }

    @Test
    void addScreeningNonOverlappingScreenings() {
        ScreeningManager manager = new ScreeningManager();

        Movie avengers = new Movie("Avengers", 120, AgeRequirement.AGE_0);
        Movie endgame = new Movie("Endgame", 180, AgeRequirement.AGE_0);

        Theater theater = new Theater("Harald", 25, 60);

        MovieScreening screening1 = new MovieScreening(avengers, LocalDateTime.of(2026, 10, 20, 14, 0), theater);
        MovieScreening screening2 = new MovieScreening(endgame, LocalDateTime.of(2026, 10, 20, 18, 0), theater);

        manager.addScreening(screening1);
        boolean result = manager.addScreening(screening2);

        assertTrue(result);
        assertEquals(2, manager.getScreenings().size());
    }

    @Test
    void addScreeningAllowsOverlapInDifferentTheaters() {
        ScreeningManager manager = new ScreeningManager();

        Movie avengers = new Movie("Avengers", 120, AgeRequirement.AGE_0);
        Movie endgame = new Movie("Endgame", 180, AgeRequirement.AGE_0);
        Theater theater1 = new Theater("Harald", 25, 60);
        Theater theater2 = new Theater("Astrit", 25, 60);


        MovieScreening screening1 = new MovieScreening(avengers, LocalDateTime.of(2026, 10, 20, 14, 0), theater1);
        MovieScreening screening2 = new MovieScreening(endgame, LocalDateTime.of(2026, 10, 20, 14, 0), theater2);

        manager.addScreening(screening1);
        boolean result = manager.addScreening(screening2);

        assertTrue(result);
        assertEquals(2, manager.getScreenings().size());
    }

    @Test
    void getScreeningsForMovieReturnsMatchingScreenings() {
        ScreeningManager manager = new ScreeningManager();

        Movie movie1 = new Movie("Movie 1", 120, AgeRequirement.AGE_0);
        Movie movie2 = new Movie("Movie 2", 100, AgeRequirement.AGE_0);

        MovieScreening screening1 = createScreening(movie1);
        MovieScreening screening2 = createScreening(movie2);

        manager.addScreening(screening1);
        manager.addScreening(screening2);

        assertEquals(List.of(screening1), manager.getScreeningsForMovie(movie1));
    }

    @Test
    void getScreeningsForMovieReturnsAllMatchingScreenings() {
        ScreeningManager manager = new ScreeningManager();

        Movie movie = new Movie("Movie", 120, AgeRequirement.AGE_0);

        MovieScreening screening1 = createScreening(movie);
        MovieScreening screening2 = new MovieScreening(
                movie,
                LocalDateTime.of(2026, 12, 18, 20, 0),
                screening1.getTheater()
        );

        manager.addScreening(screening1);
        manager.addScreening(screening2);

        assertEquals(
                List.of(screening1, screening2),
                manager.getScreeningsForMovie(movie)
        );
    }

    @Test
    void getScreeningsForMovieReturnsEmptyListWhenNoMatchesExist() {
        ScreeningManager manager = new ScreeningManager();

        Movie movie = new Movie("Movie", 120, AgeRequirement.AGE_0);

        assertTrue(manager.getScreeningsForMovie(movie).isEmpty());
    }
}
