package com.mycompany.app;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class PointTest {
    @Test
    public void zeroPoint_gives_zero() {
        Point p = Point.zeroPoint();
        assertEquals(0, p.points());
    }

    @ParameterizedTest
    @CsvSource({"0,0", "1,1", "40,40", "100,100"})
    public void actual_point_is_expected(int expected, int actual) {
        Point p = new Point(expected);
        assertEquals(p.points(), actual);
    }

    @Test
    public void negative_addend_throws_exception() {
        Point p = new Point(1);
        assertThrows(IllegalArgumentException.class, () -> {
            p.addPoints(-1);
        });
    }
    
    @Test
    public void negative_subtrahend_throws_exception() {
        Point p = new Point(1);
        assertThrows(IllegalArgumentException.class, () -> {
            p.subtractPoints(-1);
        });
    }

    @ParameterizedTest
    @CsvSource({"0,1", "20,70", "50,50"})
    public void valid_sum_does_not_throw_exception(int augend, int addend) {
        Point augendP = new Point(augend);
        Point addendP = new Point(addend);
        assertDoesNotThrow(() -> {
            augendP.addPoints(addendP);
        });
    }

    @ParameterizedTest
    @CsvSource({"0,101", "1,100"})
    public void invalid_sum_throws_exception(int augend, int addend) {
        Point augendP = new Point(augend);
        Point addendP = new Point(addend);
        assertThrows(IllegalStateException.class, () -> {
            augendP.addPoints(addendP);
        });
    }

    @ParameterizedTest
    @CsvSource({"0,0,0", "0,1,1", "99,1,100", "30,40,70"})
    public void correct_sum_is_calculated(int augend, int addend, int result) {
        Point augendP = new Point(augend);
        Point addendP = new Point(addend);
        Point summedPoint = augendP.addPoints(addendP);
        assertEquals(summedPoint.points(), result);
    }

    @ParameterizedTest
    @CsvSource({"0,0,0", "100,1,99", "40,10,30"})
    public void correct_difference_is_calculated(int minuend, int subtrahend, int result) {
        Point minuendP = new Point(minuend);
        Point subtrahendP = new Point(subtrahend);
        Point subtractedPoint = minuendP.subtractPoints(subtrahendP);
        assertEquals(subtractedPoint.points(), result);
    }

    @ParameterizedTest
    @CsvSource({"100,1", "100,100", "100,50"})
    public void valid_difference_does_not_throw_exception(int minuend, int subtrahend) {
        Point minuendP = new Point(minuend);
        Point subtrahendP = new Point(subtrahend);
        assertDoesNotThrow(() -> {
            minuendP.subtractPoints(subtrahendP);
        });
    }
    
    @ParameterizedTest
    @CsvSource({"1,100", "100,101", "50,100"})
    public void invalid_difference_throws_exception(int minuend, int subtrahend) {
        Point minuendP = new Point(minuend);
        Point subtrahendP = new Point(subtrahend);
        assertThrows(IllegalStateException.class, () -> {
            minuendP.subtractPoints(subtrahendP);
        });
    }
}
