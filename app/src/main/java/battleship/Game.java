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

    // Representation Invariant:
    // nothing is null

    // Abstraction Function:
    //

    /**
     * Constructor to create an instance of a Game
     * @param playerCount the number of players in the Game
     * @param boardSize the size of the boards for the game
     * @param shipsInfo a mapping of ship sizes to counts represented by an array where indices are the sizes
     */
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

    /**
     * Make a player attack another at a point
     * @param sourcePid the player attacking
     * @param targetPid the player receiving the attack
     * @param p the Point to attack
     * @return a boolean representing whether the attack succeeded (point wasn't already guessed)
     */
    public boolean attack(int sourcePid, int targetPid, Point p) {
        return false;
    }

    /**
     * Make a player attack another at a point, where the attacking player is just the current player
     * @param targetPid the player receiving the attack
     * @param p the Point to attack
     * @return a boolean representing whether the attack succeeded (point wasn't already guessed)
     */
    public boolean attack(int targetPid, Point p) {
        return false;
    }

    /**
     * Make a player attack another at a point, where the attacking player is just the current player
     * @param targetPid the player receiving the attack
     * @param x the x-coord of the point to attack
     * @param y the y-coord of the point to attack
     * @return a boolean representing whether the attack succeeded (point wasn't already guessed)
     */
    public boolean attack(int targetPid, int x, int y) {
        return false;
    }

    /**
     * Getter method for the phase this Game is in
     * @return a String representing the state the game is in, either "setup", "playing", or "end"
     */
    public String getPhase() {
        return "";
    }

    /**
     * Add a GameListener to be called everything this Game updates
     */
    public void addListener() {

    }

    /**
     * Knowing the current player and game phase, only certain things can happen, so given a point we can process
     * an entire step of the game being played (or half a step if in setup). This modifies the game state -
     * phase and current Player
     * @param p a point to process
     */
    public void processTurn(Point p) {

    }

    /**
     * Returns a list of board views where the first in the list is the player’s view of their own
     * board, the rest are the player’s view of opponent boards
     * @param pid the ID of the player to get the viewpoint of
     * @return a List of BoardViews
     */
    public List<BoardView> getPlayerView(int pid) {
        return new ArrayList<>();
    }

    /**
     * Gets the size of the board this Game is being played on
     * @return an int equal to the size of this Games board
     */
    public int size() {
        return 0;
    }

    /**
     * Adds a ships to this game as defined by two Points. Can only be called if the game is in
     * the setup phase and if there are players with ships that still need to be added.
     * Ships must have valid length defined by the shipInfo this Game was constructed with,
     * and be valid for the current player to add or else this method will return false.
     * This method may modify the game state - current player and game phase
     * @param p1 the first point (head) of the ship to add
     * @param p2 the second point (tail) of the ship to add
     * @return a boolean indicating whether the ship was successfully added
     */
    public boolean addShip(Point p1, Point p2) {
        return false;
    }

    /**
     * Adds a ships to this game as defined by two Points. Can only be called if the game is in
     * the setup phase and if there are players with ships that still need to be added.
     * Ships must have valid length defined by the shipInfo this Game was constructed with,
     * and be valid for the current player to add or else this method will return false.
     * This method may modify the game state - current player and game phase
     * @param x1 x-coord of the first point (head) of the ship to add
     * @param y1 y-coord of the first point (tail) of the ship to add
     * @param x2 x-coord of the second point (head) of the ship to add
     * @param y2 y-coord of the second point (tail) of the ship to add
     * @return a boolean indicating whether the ship was successfully added
     */
    public boolean addShip(int x1, int y1, int x2, int y2) {
        return false;
    }
}
