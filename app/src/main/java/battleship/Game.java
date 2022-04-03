package battleship;

import java.util.ArrayList;
import java.util.List;

/**
 * A Game object is a mutable ADT representing one game being played, where a game consists of the following:
 * - Players
 * - Boards for the players
 * - Ships on player board
 * - Hits and misses for players, or their previous guesses
 * This ADT contains an interface to be called in order to update the state.
 */
public class Game {

    // represents ships of lengths [2, 3, 3, 4, 5]
    private static final int[] DEFAULT_SHIPS = {0, 0, 1, 2, 1, 1};

    public Game(int playerCount, int boardSize, int[] shipsInfo) {

    }

    // constructor assuming default ships
    public Game(int playerCount, int boardSize) {

    }

    // constructor assuming default board size and ships
    public Game(int playerCount) {

    }

    // constructor assuming default board size
    // on SO someone recommended using Builder pattern for default values since java doesn't have
    // default parameters
    public Game(int playerCount, int[] shipsInfo) {

    }

    public boolean attack(int pid1, int pid2, Point p) {
        return false;
    }

    public boolean attack(int targetPid, Point p) {
        return false;
    }

    public boolean attack(int targetPid, int x, int y) {
        return false;
    }

    public String getPhase() {
        return "";
    }

    public void addListener() {

    }

    public void processTurn(Point p) {

    }

    public List<BoardView> getPlayerView(int pid) {
        return new ArrayList<>();
    }

    public int size() {
        return 0;
    }

    public boolean addShip(Point p1, Point p2) {
        return false;
    }

    public boolean addShip(int x1, int y1, int x2, int y2) {
        return false;
    }
}
