package battleship;

import java.util.*;

/**
 * Player is a mutable ADT representing a player in the game. Each Player must have a unique ID, no other
 * Player object can have the same ID without being the same player.
 */
public class Player {

    private static final int BOARD_SIZE = 10;
    private static final boolean DEBUG = true;

    private final int playerId; // a value that must uniquely identify a player
    private final Board playerBoard;
    private final Map<Player, Board> opponentBoards;
    private final List<Ship> playerShips;

    // Abstraction Function:
    // playerBoard is this player's board with hit and misses on it
    // opponentBoards is this player's perception of hits and misses on opponent boards
    // playerShips is this player's ships
    // playerShips and playerBoard combined represent this player's view of their game state
    // prevGuesses represents this player's guesses to other players in the game

    // Representation Invariant:
    // playerBoard != null &&
    // opponentBoards != null &&
    // playerShips != null &&
    // for each ship in playerShips: ship != null &&

    private void checkRep() {
        assert this.playerBoard != null : "Board cannot be null";
        assert this.opponentBoards != null : "Opponent boards cannot be null";
        assert this.playerShips != null : "Collection of ships cannot be null";
        assert this.playerShips.size() != 0 : "Collection of ships must be non-empty";

        if (DEBUG) {
            for (Ship s : this.playerShips) {
                assert s != null : "Ships cannot be null";
            }
        }
    }

    /**
     * Constructs a new player with ships. This is the only place the player can get ships.
     * Additionally, every Player must have a unique ID.
     * @param id the unique ID for this player
     * @param ships an array of ships for this player
     * @throws IllegalArgumentException if ships is empty or null
     */
    public Player(int id, Ship[] ships, int board_size) {
        if (ships == null) throw new IllegalArgumentException("Player's ships must exist!");
        this.playerShips = new ArrayList<>();
        this.playerShips.addAll(Arrays.asList(ships));

        this.playerBoard = new Board(board_size);
        this.opponentBoards = new HashMap<>();
        this.playerId = id;
        checkRep();
    }

    public Player(int id, Ship[] ships) {
        this(id, ships, BOARD_SIZE);
    }

    /**
     * To add ships after construction
     * @param p1 Point start of ship
     * @param p2 Point end of ship
     */
    public void addShip(Point p1, Point p2) {
        this.playerShips.add(new Ship(p1, p2));
    }

    /**
     * to add ships after construction
     * @param s Ship to add
     */
    public void addShip(Ship s) {
        this.playerShips.add(s);
    }

    /**
     * This player attacks another player and updates this player's board with the result.
     * @param other the other Player to target an attack on
     * @param p the point on the other Player's board to attack
     * @throws IllegalArgumentException if any args are null or if attacking position already attacked
     */
    public void attack(Player other, Point p) {
        if (other == null || p == null) throw new IllegalArgumentException("Null inputs to attack");
        checkRep();
        // TODO: this means that player only knows about opponents after attacking them...
        if (!this.opponentBoards.containsKey(other)) this.opponentBoards.put(other, new Board(BOARD_SIZE));

        boolean result = other.receive(p);
        boolean validResult;
        if (result) validResult = this.opponentBoards.get(other).addHit(p);
        else validResult = this.opponentBoards.get(other).addMiss(p);
        checkRep();
        if (!validResult) throw new IllegalArgumentException("Tried to attack position that was already guessed");
    }

    /**
     * Receives an attack from another player, updating this player's board and ships
     * @param p the point that is being attacked
     * @return a boolean: true if attack was a hit, false if not
     * @throws IllegalArgumentException if p is null
     */
    public boolean receive(Point p) {
        if (p == null) throw new IllegalArgumentException("Received an attack on no point");
        checkRep();
        boolean didHit = false;
        for (Ship s : this.playerShips) {
            didHit = s.hit(p);
            if (didHit) break;
        }
        if (didHit) this.playerBoard.addHit(p);
        else this.playerBoard.addMiss(p);
        checkRep();
        return didHit;
    }

    /**
     * Get a view of this player's board
     * @return a BoardView object that serves as a view to this player's boards
     */
    public BoardView getBoard() {
        checkRep();
        return new BoardView(this.playerBoard);
    }
    /**
     * Get views of this player's enemies
     * @return a collection of BoardViews of the opponents of this player
     */
    public List<BoardView> getEnemyBoards() {
        checkRep();
        List<BoardView> out = new ArrayList<>();
        for (Board b : this.opponentBoards.values()) out.add(new BoardView(b));
        checkRep();
        return out;
    }

    /**
     * Gets this player's ships
     * @return a list copy of this player's ships
     */
    public List<Ship> getShips() {
        checkRep();
        // CAUTION! not a deep copy so ships can be modified TODO fix this!
        return new ArrayList<>(this.playerShips);
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Player)) {
            return false;
        }
        return this.playerId == ((Player) other).playerId;
    }

    @Override
    public int hashCode() {
        // kind of lazy - I added a field just to avoid having to iterate everything for equals and hashCode
        return this.playerId; // already an int
    }

    @Override
    public String toString() {
        String id = "ID: " + this.playerId;
        StringBuilder ships = new StringBuilder("Ships: [");
        getShips().forEach((ship) -> ships.append(ship).append(", "));
        ships.append("]");

        String board = "Board: " + this.playerBoard;

        return id + "\n" + ships + "\n" + board;
    }

}
