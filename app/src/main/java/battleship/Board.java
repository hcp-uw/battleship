package battleship;

import battleship.Point;

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
    //      parameters in all Points satsify 0 <= p < size
    //      size >= 0

    private Set<Point> hit;
    private Set<Point> miss;
    private int size;
    public static final boolean DEBUG = true;

    /**
     * Constructs new Board
     *
     * @throw IllegalArgumentException if size < 0
     */
    public Board(int size) throws IllegalArgumentException{
        if (size < 0){
            throw new IllegalArgumentException();
        }
        hit = new HashSet<>();
        miss = new HashSet<>();
        this.size = size;
        checkRep();
    }

    private void checkRep(){
        assert(hit != null && miss != null) : "Board must be instantiated";
        assert(size >= 0) : "Size cannot be less than 0";
        if (DEBUG){
            for (Point p : hit){
                assert(p != null) : "Points cannot be null";
                assert(!miss.contains(p)) : "Points cannot be in both hit and miss";
                assert(p.getX() >= 0 && p.getX() < size && p.getY() >= 0 && p.getY() < size)
                        : "Points must exist in the board";
            }
            for (Point p : miss){
                assert(p != null) : "Points cannot be null";
                assert(p.getX() >= 0 && p.getX() < size && p.getY() >= 0 && p.getY() < size)
                        : "Points must exist in the board";
            }
        }
    }

    /**
     * Marks the given Point as hit
     *
     * @spec.requires the Point p has not been marked as a miss
     * @param p the Point to be marked as hit
     * @spec.modifies this
     * @spec.effects marks the given Point as hit
     * @throws IllegalArgumentException if the point is null or does not exist on the Board
     */
    public void addHit(Point p) throws IllegalArgumentException{
        checkRep();
        if (p == null || p.getX < 0 || p.getX >= size || p.getY < 0 || p.getY >= size){
            throw new IllegalArgumentException();
        }
        if (!miss.contains(p)){
            hit.add(p);
        }
        checkRep();
    }

    /**
     * Marks the given Point as miss
     *
     * @spec.requires the Point p has not been marked as a hit
     * @param p the Point to be marked as miss
     * @spec.modifies this
     * @spec.effects marks the given Point as miss
     * @throws IllegalArgumentException if the point is null or does not exist on the Board
     */
    public void addMiss(Point p) throws IllegalArgumentException{
        checkRep();
        if (p == null || p.getX < 0 || p.getX >= size || p.getY < 0 || p.getY >= size){
            throw new IllegalArgumentException();
        }
        if (!hit.contains(p)) {
            miss.add(p);
        }
        checkRep();
    }

    /**
     * Returns the Points that have been hit
     *
     * @returns the Points that have been hit
     */
    public Set<Point> getHits(){
        checkRep();
        return Collections.unmodifiableSet(hit);
    }

    /**
     * Returns the Points that have been missed
     *
     * @returns the Points that have been missed
     */
    public Set<Point> getMisses(){
        checkRep();
        return Collections.unmodifiableSet(miss);
    }

    /**
     * Return if the given Point has been guessed
     *
     * @param p the Point to be checked
     * @throws IllegalArgumentException if p is null
     * @return true if the given Point has been guessed on the Board
     */
    public boolean hasAlreadyGuessed(Point p) {
        checkRep();
        if (p == null){
            return IllegalArgumentException();
        }
        checkRep();
        return hit.contains(p) || miss.contains(p);
    }

}