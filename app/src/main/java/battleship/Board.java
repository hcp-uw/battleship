package battleship;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

// The Board class is a mutable object that represents a player's
// nxn-sized playing board
public class Board {
    // Abstraction Function:
    //      hit = unordered collection of points that have been hit
    //      miss = unordered collection of points that have been missed
    //      size = length of one edge of the n x n board

    // Rep Invariant:
    //      hit and miss are not null
    //      No point can be both a hit and a miss
    //      Points in hit are not null
    //      Points in miss are not null
    //      hit and miss do not contain duplicate Points
    //      parameters in all Points satisfy 0 <= p < size
    //      size >= 0

    private final Set<Point> hit;
    private final Set<Point> miss;
    private final int size;
    public static final boolean DEBUG = true;

    /**
     * Constructs new Board
     *
     * @throws IllegalArgumentException if size < 0
     */
    public Board(int size) throws IllegalArgumentException {
        if (size < 0) {
            throw new IllegalArgumentException();
        }
        hit = new HashSet<>();
        miss = new HashSet<>();
        this.size = size;
        checkRep();
    }

    private void checkRep() {
        assert (hit != null && miss != null) : "Board must be instantiated";
        assert (size >= 0) : "Size cannot be less than 0";
        if (DEBUG) {
            for (Point p : hit) {
                assert (p != null) : "Points cannot be null";
                assert (!miss.contains(p)) : "Points cannot be in both hit and miss";
                assert (p.getX() >= 0 && p.getX() < size && p.getY() >= 0 && p.getY() < size)
                        : "Points must exist in the board";
            }
            for (Point p : miss) {
                assert (p != null) : "Points cannot be null";
                assert (p.getX() >= 0 && p.getX() < size && p.getY() >= 0 && p.getY() < size)
                        : "Points must exist in the board";
            }
        }
    }


    /**
     * Marks the given Point as hit
     *
     * @param p the Point to be marked as hit
     * @return true if the Point was marked as hit, false if it was already guessed
     * @throws IllegalArgumentException if the point is null or does not exist on the Board
     */
    public boolean addHit(Point p) throws IllegalArgumentException {
        checkRep();
        if (p == null || p.getX() < 0 || p.getX() >= size || p.getY() < 0 || p.getY() >= size) {
            throw new IllegalArgumentException();
        }
        if (miss.contains(p) || hit.contains(p)) {
            return false;
        }
        hit.add(p);
        checkRep();
        return true;
    }

    /**
     * Marks the given Point as miss
     *
     * @param p the Point to be marked as miss
     * @return true if the Point was marked as miss, false if it was already guessed
     * @throws IllegalArgumentException if the point is null or does not exist on the Board
     */
    public boolean addMiss(Point p) throws IllegalArgumentException {
        checkRep();
        if (p == null || p.getX() < 0 || p.getX() >= size || p.getY() < 0 || p.getY() >= size) {
            throw new IllegalArgumentException();
        }
        if (miss.contains(p) || hit.contains(p)) {
            return false;
        }
        miss.add(p);
        checkRep();
        return true;
    }

    /**
     * Returns the Points that have been hit
     *
     * @return the Points that have been hit
     */
    public Set<Point> getHits() {
        checkRep();
        return Collections.unmodifiableSet(hit);
    }

    /**
     * Returns the Points that have been missed
     *
     * @return the Points that have been missed
     */
    public Set<Point> getMisses() {
        checkRep();
        return Collections.unmodifiableSet(miss);
    }


    /**
     * Return if the given Point has been guessed
     *
     * @param p the Point to be checked
     * @return true if the given Point has been guessed on the Board
     * @throws IllegalArgumentException if p is null
     */
    public boolean hasAlreadyGuessed(Point p) throws IllegalArgumentException {
        checkRep();
        if (p == null) {
            throw new IllegalArgumentException();
        }
        checkRep();
        return hit.contains(p) || miss.contains(p);
    }

    /**
     *
     * Returns String form of Board
     *
     * @return a String representation of the board, indicating points that have been hit and missed
     */
    public String toString(){
        String hits = "Hits:";
        for (Point p: hit){
            hits += " " + p.toString();
        }
        String misses = "Mises:";
        for (Point p : miss){
            misses += " " + p.toString();
        }
        return hits + "\n" + misses;
    }
}