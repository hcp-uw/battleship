package battleship;

/**
 * The Point class represents a 2D cartesian integer coordinate point.
 * Instances of Point are immutable.
 */
public final class Point {

    // Abstraction Function:
    //      A Point p represents the cartesian coordinate (p.x, p.y).

    // Representation Invariant:
    //      none

    /**
     * The x-value for this point.
     */
    private final int x;

    /**
     * The y-value for this point.
     */
    private final int y;

    /**
     * Constructs a new point from the given x and y values.
     *
     * @param x the x-value for this point
     * @param y the y-value for this point
     */
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the x-value for this point.
     *
     * @return the x-value for this point
     */
    public int getX() { return x; }

    /**
     * Returns the x-value for this point.
     *
     * @return the x-value for this point
     */
    public int getY() { return y; }

    /**
     * Returns true if {@code o} represents the same point as {@code this}.
     *
     * @param o the object to be compared to {@code this} for equality
     * @return true if {@code o} represents the same point as {@code this}
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Point)) {
            return false;
        }
        Point p = (Point) o;
        return x == p.x && y == p.y;
    }

    /**
     * Returns an integer representation of this point.
     *
     * @return an integer representation of this point
     */
    @Override
    public int hashCode() {
        return 31 * x + y;
    }

    /**
     * Returns a string representation of this point.
     *
     * @return a string representation of this point
     */
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
