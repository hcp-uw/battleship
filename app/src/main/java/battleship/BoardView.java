package battleship;

import java.util.Set;

// The BoardView class is an immutable object that represents a player's
// nxn-sized playing board
public class BoardView{
    // Abstraction Function:
    //      b = the playing board to be viewed

    // Rep Invariant:
    //      b is not null

    private final Board b;

    /**
     * Constructs a new Board
     *
     * @param b the Board to be viewed
     * @throws IllegalArgumentException if b is null
     */
    public BoardView(Board b) throws IllegalArgumentException{
        if (b == null){
            throw new IllegalArgumentException();
        }
        this.b = b;
        checkRep();
    }


    private void checkRep(){
        assert (b != null) : "The Board cannot be null";
    }

    /**
     * Returns the Points that have been hit
     *
     * @return the Points that have been hit
     */
    public Set<Point> getHits(){
        checkRep();
        return b.getHits();
    }

    /**
     * Returns the Points that have been missed
     *
     * @return the Points that have been missed
     */
    public Set<Point> getMisses(){
        checkRep();
        return b.getMisses();
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
        return b.hasAlreadyGuessed(p);
    }

    /**
     *
     * Returns String form of Board
     *
     * @return a String representation of the board, with axis labels, and the key 'x'=hit, 'o'=miss, '-'=untouched
     */
    public String toString(){
        checkRep();
        return b.toString();
    }

    public int size() {
        return b.size();
    }
}