package com.mycompany.app;

public record Point(int points) {
    private static final int MAX_POINTS = 100;

    public Point addPoints(int points) {
        if (points < 0) {
            throw new IllegalArgumentException("Points to be added cannot be negative");
        }
        else if (Math.addExact(this.points, points) > MAX_POINTS) {
            throw new IllegalStateException("Total amount of points would have exceeded the max allowed points");
        }

        return new Point(Math.addExact(this.points, points));
    }

    public static Point getZeroPoint() {
        return new Point(0);
    }

    public Point addPoints(Point point) {
        return addPoints(point.points);
    }

    public Point subtractPoints(int points) {
        if (points < 0) {
            throw new IllegalArgumentException("Points to be subtracted cannot be negative");
        }
        else if (Math.subtractExact(this.points, points) < 0) {
            throw new IllegalStateException("Total amount of points would have decreased to below zero");
        }

        return new Point(Math.subtractExact(this.points, points));
    }

    public Point subtractPoints(Point point) {
        return subtractPoints(point.points);
    }
}
