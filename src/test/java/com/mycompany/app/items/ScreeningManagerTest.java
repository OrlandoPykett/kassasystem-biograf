package com.mycompany.app.items;

import com.mycompany.app.ScreeningManager;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class ScreeningManagerTest {

    @Test
    void addScreeningStoresScreening() {
        // Arrange
        ScreeningManager manager = new ScreeningManager();
        Movie movie = new Movie("Avengers", 120, AgeRequirement.AGE_0);
        MovieScreening screening = new MovieScreening(movie, LocalDateTime.of(2026, 10, 20, 14, 0), "Holger");

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

        MovieScreening screening1 = new MovieScreening(avengers, LocalDateTime.of(2026, 10, 20, 14, 0), "Holger");
        MovieScreening screening2 = new MovieScreening(endgame, LocalDateTime.of(2026, 10, 20, 15, 0), "Holger");

        manager.addScreening(screening1);
        boolean result = manager.addScreening(screening2);

        assertFalse(result);
        assertEquals(1, manager.getScreenings().size());
    }

    @Test
    void addScreeningRemovesScreening() {
        ScreeningManager manager = new ScreeningManager();
        Movie movie = new Movie("Avengers", 120, AgeRequirement.AGE_0);
        MovieScreening screening = new MovieScreening(movie, LocalDateTime.of(2026, 10, 20, 14, 0), "Holger");

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

        MovieScreening screening1 = new MovieScreening(avengers, LocalDateTime.of(2026, 10, 20, 14, 0), "Holger");
        MovieScreening screening2 = new MovieScreening(endgame, LocalDateTime.of(2026, 10, 20, 18, 0), "Holger");

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

        MovieScreening screening1 = new MovieScreening(avengers, LocalDateTime.of(2026, 10, 20, 14, 0), "Holger");
        MovieScreening screening2 = new MovieScreening(endgame, LocalDateTime.of(2026, 10, 20, 14, 0), "Torsten");

        manager.addScreening(screening1);
        boolean result = manager.addScreening(screening2);

        assertTrue(result);
        assertEquals(2, manager.getScreenings().size());
    }
}
