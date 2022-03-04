package battleship;

/**
 * The Ship class represents a ship in the game of Battleship.
 */
public class Ship {

    // Abstraction Function:
    //      A Ship s is characterized by a start and end point, and spans the length from
    //      s.startPoint to s.endPoint (inclusive). s.hp represents the remaining health
    //      of the ship, which is decremented each time the ship is hit.

    // Representation Invariant:
    //      startPoint != null
    //      && endPoint != null
    //      && hp >= 0
    //      && startPoint.getX() == endPoint.getX() || startPoint.getY() == endPoint.getY()
    //      && startPoint.getX() <= endPoint.getX()
    //      && startPoint.getY() <= endPoint.getY()

    /**
     * The top-left point of this ship.
     */
    private final Point startPoint;

    /**
     * The bottom-right point of this ship.
     */
    private final Point endPoint;

    /**
     * The remaining hp of this ship.
     */
    private int hp;

    /**
     * Throws an exception is the representation invariant is violated.
     */
    private void checkRep() {
        assert (startPoint != null): "null start point";
        assert (endPoint != null): "null end point";
        assert (hp >= 0): "negative hp";
        assert (startPoint.getX() == endPoint.getX() || startPoint.getY() == endPoint.getY()):
                "start and end points do not span a vertical or horizontal line segment";
        assert(startPoint.getX() <= endPoint.getX() && startPoint.getY() <= endPoint.getY()):
                "start point is not above or to the left of end point";
    }

    /**
     * Constructs a new ship spanning the line segment from {@code p1} to {@code p2}.
     *
     * @param p1 the point of one end of the ship
     * @param p2 the point of the other end of the ship
     * @throws IllegalArgumentException if {@code p1 == null || p2 == null} or
     * if {@code p1} and {@code p2} do not span a vertical or horizontal line segment
     */
    public Ship(Point p1, Point p2) {
        if (p1 == null || p2 == null) {
            throw new IllegalArgumentException("null start or end point");
        }
        if (p1.getX() == p2.getX() && p1.getY() < p2.getY()
            || p1.getY() == p2.getY() && p1.getX() < p2.getX()) {
            startPoint = p1;
            endPoint = p2;
        } else if (p1.getX() == p2.getX() || p1.getY() == p2.getY()) {
            startPoint = p2;
            endPoint = p1;
        } else {
            throw new IllegalArgumentException("start and end point must span a vertical or horizontal line segment");
        }
        checkRep();
    }

    /**
     * Returns the top-left point of this ship.
     *
     * @return the top-left point of this ship
     */
    public Point startPoint() {
        checkRep();
        return startPoint;
    }

    /**
     * Returns the bottom-right point of this ship.
     *
     * @return the bottom-right point of this ship
     */
    public Point endPoint() {
        checkRep();
        return endPoint;
    }

    /**
     * Returns the length of this ship.
     *
     * @return the length of this ship
     */
    public int length() {
        checkRep();
        return endPoint.getX() - startPoint.getX() + endPoint.getY() - startPoint.getY();
    }

    /**
     * Returns {@literal true} iff this ship has been sunk.
     *
     * @return {@literal true} iff this ship has been sunk
     */
    public boolean isSunk() {
        checkRep();
        return hp == 0;
    }

    /**
     * Returns {@literal true} iff this ship can be hit at the point {@code p}.
     *
     * @param p the point at which to hit this ship
     * @return {@literal true} iff this ship can be hit at the point {@code p}
     * @throws IllegalArgumentException if this ship contains {@code p} and the ship has already
     * been sunk
     */
    public boolean hit(Point p) {
        checkRep();
        if (!containsPoint(p)) {
            return false;
        }
        if (isSunk()) {
            throw new IllegalArgumentException("ship is already sunk");
        }
        takeHit();
        checkRep();
        return true;
    }

    /**
     * Decrements this ship's hp.
     */
    private void takeHit() {
        checkRep();
        hp--;
        checkRep();
    }

    /**
     * Returns {@literal true} iff the given point is contained in this ship.
     *
     * @param p the point being tested for containment in this ship
     * @return {@literal true} iff {@code p} is contained in this ship
     */
    private boolean containsPoint(Point p) {
        checkRep();
        return p.getX() == startPoint.getX()
                    && p.getY() >= startPoint.getY()
                    && p.getY() <= endPoint.getY()
                || p.getY() == startPoint.getY()
                    && p.getX() >= startPoint.getX()
                    && p.getX() <= endPoint.getX();
    }
}
