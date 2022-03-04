package battleship;

/**
 * The Ship class represents a ship in the game of Battleship.
 */
public class Ship {

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
     * Constructs a new ship spanning the line segment from {@code p1} to {@code p2}.
     *
     * @param p1 the point of one end of the ship
     * @param p2 the point of the other end of the ship
     */
    public Ship(Point p1, Point p2) {
        if (p1 == null || p2 == null) {
            throw new IllegalArgumentException("null start or end point");
        }
        if (p1.getX() == p2.getX()) {
            if (p1.getY() < p2.getY()) {
                startPoint = p1;
                endPoint = p2;
            } else {
                startPoint = p2;
                endPoint = p1;
            }
        } else if (p1.getY() == p2.getY()) {
            if (p1.getX() < p2.getX()) {
                startPoint = p1;
                endPoint = p2;
            } else {
                startPoint = p2;
                endPoint = p1;
            }
        } else {
            throw new IllegalArgumentException("start and end point must fall on the same line");
        }
    }

    /**
     * Returns the top-left point of this ship.
     *
     * @return the top-left point of this ship
     */
    public Point startPoint() {
        return startPoint;
    }

    /**
     * Returns the bottom-right point of this ship.
     *
     * @return the bottom-right point of this ship
     */
    public Point endPoint() {
        return endPoint;
    }

    public int length() {
        return endPoint.getX() - startPoint.getX() + endPoint.getY() - startPoint.getY();
    }

    public boolean isSunk() {
        return hp == 0;
    }

    public boolean hit(Point p) {
        if (!containsPoint(p)) {
            return false;
        }
        if (isSunk()) {
            throw new IllegalArgumentException("ship is already sunk");
        }
        takeHit();
        return true;
    }

    private void takeHit() {
        hp--;
    }

    private boolean containsPoint(Point p) {
        return p.getX() == startPoint.getX()
                    && p.getY() >= startPoint.getY()
                    && p.getY() <= endPoint.getY()
                || p.getY() == startPoint.getY()
                    && p.getX() >= startPoint.getX()
                    && p.getX() <= endPoint.getX();
    }
}
