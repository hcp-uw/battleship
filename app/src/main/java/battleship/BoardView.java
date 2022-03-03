package battleship;

import battleship.Board;

// The BoardView class is an immutable object that represents a player's
// nxn-sized playing board
public class BoardView(){
    // Abstraction Function:
    //      b = the playing board to be viewed

    // Rep Invariant:
    //      b is not null

    private Board board;

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
        board = b;
        checkRep();
    }


    private void checkRep(){
        assert (b != null) : "The Board cannot be null";
    }

    /**
     * Returns the Points that have been hit
     *
     * @returns the Points that have been hit
     */
    public Set<Point> getHits(){
        checkRep();
        return b.getHits();
    }

    /**
     * Returns the Points that have been missed
     *
     * @returns the Points that have been missed
     */
    public Set<Point> getMisses(){
        checkRep();
        return b.getMisses();
    }
}