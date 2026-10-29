package com.mycompany.app.items;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MovieTest {

    private Movie movie;

    @BeforeEach
    void setUp() {
        movie = new Movie("Test Movie", 120, AgeRequirement.AGE_18);
    }

    @Test
    void movieStoresCorrectProperties() {
        assertAll(
                () -> assertEquals("Test Movie", movie.getTitle()),
                () -> assertEquals(120, movie.getDurationMin()),
                () -> assertEquals(AgeRequirement.AGE_18, movie.getAgeReq())
        );
    }

    @Test
    void movieRejectsNullTitle() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Movie(null, 120, AgeRequirement.AGE_18)
        );
    }

    @Test
    void movieRejectsEmptyTitle() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Movie("", 120, AgeRequirement.AGE_18)
        );
    }

    @Test
    void movieRejectsBlankTitle() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Movie("   ", 120, AgeRequirement.AGE_18)
        );
    }

    @Test
    void movieRejectsZeroDuration() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Movie("Test Movie", 0, AgeRequirement.AGE_18)
        );
    }

    @Test
    void movieRejectsNegativeDuration() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Movie("Test Movie", -10, AgeRequirement.AGE_18)
        );
    }

    @Test
    void movieDefaultsToAgeZeroWhenAgeRequirementIsNull() {
        Movie movieWithoutAgeRequirement =
                new Movie("Test Movie", 120, null);

        assertEquals(
                AgeRequirement.AGE_0,
                movieWithoutAgeRequirement.getAgeReq()
        );
    }

    @Test
    void movieIsSuitableForCustomerMeetingAgeRequirement() {
        assertTrue(movie.isSuitableFor(18));
        assertTrue(movie.isSuitableFor(25));
    }

    @Test
    void movieIsNotSuitableForCustomerBelowAgeRequirement() {
        assertFalse(movie.isSuitableFor(17));
        assertFalse(movie.isSuitableFor(0));
    }

    @Test
    void getScreeningsReturnsNullWhenNoScreeningsAreSet() {
        assertNull(movie.getScreenings());
    }

    @Test
    void getScreeningsReturnsAssignedScreenings() {
        int[] screenings = {1, 2, 3};
        movie.screenings = screenings;

        assertSame(screenings, movie.getScreenings());
    }

    @Test
    void toStringReturnsMovieTitle() {
        assertEquals("Test Movie", movie.toString());
    }
}