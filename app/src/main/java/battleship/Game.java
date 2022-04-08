package battleship;

import java.util.*;

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
    // default board size is 10x10
    private static final int DEFAULT_SIZE = 10;
    // 3 phases of game
    private static final String[] GAME_PHASES = {"setup", "playing", "end"};

    private int gameBoardSize;
    private int currentGamePhase;
    private Map<Integer, Player> players;
    private List<Integer> PlayerIdList;
    private int currentPlayer; // current player represented by index in PID list
    private List<GameListener> listeners;
    private final int[] allowableShipSet;
    private final Map<Integer, String> playerNames; // this might be refactorable to the Player class

    // should only be written to
    protected final List<Point> pointBuffer; // list to store points or something I'm lazy

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
        this.players = new HashMap<>();
        generatePlayers(playerCount, boardSize);

        this.gameBoardSize = boardSize;

        this.currentGamePhase = 0;
        this.currentPlayer = -1;

        this.PlayerIdList = new ArrayList<>();
        this.players.keySet().iterator().forEachRemaining((e) -> this.PlayerIdList.add(e));
        this.allowableShipSet = shipsInfo;

        this.pointBuffer = new ArrayList<>();
        this.playerNames = new HashMap<>();
    }

    // constructor assuming default ships
    public Game(int playerCount, int boardSize) {
        this(playerCount, boardSize, DEFAULT_SHIPS);
    }

    // constructor assuming default board size and ships
    public Game(int playerCount) {
        this(playerCount, DEFAULT_SIZE, DEFAULT_SHIPS);
    }

    // constructor assuming default board size
    // on SO someone recommended using Builder pattern for default values since java doesn't have
    // default parameters
    public Game(int playerCount, int[] shipsInfo) {
        this(playerCount, DEFAULT_SIZE, shipsInfo);
    }

    /**
     * makes players and puts them in this.players, assigning an ID to each
     * @param count the number of players to generate
     */
    private void generatePlayers(int count, int boardSize) {
        int baseId = 1;
        for (int i = 0; i < count; i++) {
            int pid = baseId + 1;
            this.players.put(pid, new Player(pid, new Ship[0], boardSize));
        }
    }

    /**
     * Make a player attack another at a point
     * @param sourcePid the player attacking
     * @param targetPid the player receiving the attack
     * @param p the Point to attack
     * @return a boolean representing whether the attack succeeded (point wasn't already guessed)
     */
    public boolean attack(int sourcePid, int targetPid, Point p) {
        Player t = this.players.get(targetPid);
        if (t.getBoard().hasAlreadyGuessed(p)) {
            return false;
        }
        Player s = this.players.get(sourcePid);
        s.attack(t, p);
        return true;
    }

    /**
     * Make a player attack another at a point, where the attacking player is just the current player
     * @param targetPid the player receiving the attack
     * @param p the Point to attack
     * @return a boolean representing whether the attack succeeded (point wasn't already guessed)
     */
    public boolean attack(int targetPid, Point p) {
        return attack(this.getCurrentPlayer(), targetPid, p);
    }

    /**
     * Make a player attack another at a point, where the attacking player is just the current player
     * @param targetPid the player receiving the attack
     * @param x the x-coord of the point to attack
     * @param y the y-coord of the point to attack
     * @return a boolean representing whether the attack succeeded (point wasn't already guessed)
     */
    public boolean attack(int targetPid, int x, int y) {
        return attack(this.getCurrentPlayer(), targetPid, new Point(x, y));
    }

    /**
     * Getter method for the phase this Game is in
     * @return a String representing the state the game is in, either "setup", "playing", or "end"
     */
    public String getPhase() {
        return GAME_PHASES[this.currentGamePhase];
    }

    /**
     * Add a GameListener to be called every time this Game updates
     */
    public void addListener(GameListener g) {
        this.listeners.add(g);
    }

    private void update() {
        for (GameListener g : this.listeners) {
            g.onChange();
        }
    }

    /**
     * Knowing the current player and game phase, only certain things can happen, so given a point we can process
     * an entire step of the game being played (or half a step if in setup). This modifies the game state -
     * phase and current Player
     * @param p a point to process
     */
    public void processTurn(Point p) {
        if (this.getPhase().equals("setup")) {
            int bufSize = this.pointBuffer.size();
            if (bufSize % 2 == 1) {
                this.addShip(this.pointBuffer.get(bufSize - 1), p);
            }
            this.pointBuffer.add(p);
            // TODO: check if this completes the setup phase or not
        } else if (this.getPhase().equals("playing")) {
            // does nothing in this version of Game - check TwoPlayerGame where it is implicit which player to attack
            this.pointBuffer.add(p);
            endTurn();
        }
        // and do nothing if game phase is something else
    }

    /**
     * Returns a list of board views where the first in the list is the player’s view of their own
     * board, the rest are the player’s view of opponent boards
     * @param pid the ID of the player to get the viewpoint of
     * @return a List of BoardViews
     */
    public List<BoardView> getPlayerView(int pid) {
        Player p = this.players.get(pid);
        List<BoardView> out = p.getEnemyBoards();
        out.add(0, p.getBoard());
        return out;
    }

    /**
     * Gets the size of the board this Game is being played on
     * @return an int equal to the size of this Games board
     */
    public int size() {
        return this.gameBoardSize;
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
        if (!this.getPhase().equals("setup")) throw new RuntimeException("Ships may only be added during setup");

        Ship toAdd = new Ship(p1, p2);
        List<Ship> playerCurShips = this.players.get(this.currentPlayer).getShips();
        for (Ship s : playerCurShips) {
            if (shipsIntersect(toAdd, s)) {
                return false;
            }
        }
        this.players.get(this.currentPlayer).addShip(toAdd);
        return true;
    }

    private boolean shipsIntersect(Ship s1, Ship s2) {
        List<Point> pointsS1 = getPointsBetween(s1.startPoint(), s1.endPoint());
        List<Point> pointsS2 = getPointsBetween(s2.startPoint(), s2.endPoint());

        // not an efficient algorithm, shouldn't matter unless ships are large
        // there is definitely a mathematical way to check line intersections TODO look for that
        for (Point p1 : pointsS1) {
            for (Point p2 : pointsS2) {
                if (p1.equals(p2)) return true;
            }
        }
        return false;
    }

    // helper to get all points in between two points in a line, vertically or horizontally
    // inclusively
    private List<Point> getPointsBetween(Point p1, Point p2) {
        List<Point> out = new ArrayList<>();
        int x1 = p1.getX();
        int x2 = p2.getX();
        int dx = x2 - x1;
        if (dx == 0) { // ship is vertical
            int y1 = p1.getY();
            int y2 = p2.getY();
            int dy = y2 - y1;
            int dir = (dy < 0) ? -1 : 1;
            for (int i = 0; i <= Math.abs(dy); i++) {
                out.add(new Point(p2.getX(), y1 + i*dir));
            }
        } else { // ship must be horizontal
            int dir = (dx < 0) ? -1 : 1;
            for (int i = 0; i <= Math.abs(dx); i++) {
                out.add(new Point(p2.getX(), x1 + i*dir));
            }
        }
        return out;
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
        return addShip(new Point(x1, y1), new Point(x2, y2));
    }

    /**
     * Gets the Player ID (PID) of the player whose turn is next, the player whose input
     * should take the next turn
     * TODO: ensure that if the game state is "end" this returns the winner
     * @return the integer PID of the current player to go
     */
    public int getCurrentPlayer() {
        return this.PlayerIdList.get(this.currentPlayer);
    }

    /**
     * Gets the player to go by the string name that their PID is mapped to in this.playerNames
     * @return
     */
    public String getCurrentPlayerName() {
        int curPid = getCurrentPlayer();
        if (this.playerNames.containsKey(curPid)) {
            return this.playerNames.get(curPid);
        }
        return Integer.toString(curPid);
    }

    /**
     * sets a player's name in the game by mapping a PID to a name
     * @param pid the pid of the Player to name
     * @param name the name to associate the PID with
     */
    public void setPlayerName(int pid, String name) {
        this.playerNames.put(pid, name);
    }

    /**
     * Getter to tell what ships a player still has to place.
     * Can only be called while the game is in the setup phase
     * @param pid the Player ID (PID) of the player to get the remaining ships for
     * @return An map as an array of ship length (index) to number (value)
     */
    public int[] getShipsToBePlaced(int pid) {
        List<Ship> currentShips = this.players.get(pid).getShips();
        int[] shipsLeft = Arrays.copyOf(this.allowableShipSet, this.allowableShipSet.length);
        // calculate difference between allowed ships and ships they have
        for (Ship currentShip : currentShips) {
            shipsLeft[currentShip.length()]--;
        }
        for (int shipCount : shipsLeft) {
            if (shipCount < 0) throw new RuntimeException("Encountered bad ships on Player " + pid);
        }
        return shipsLeft;
    }

    /**
     * get a preview of the next player to go
     * @return the integer PID of the next player to go
     */
    public int getNextPlayer() {
        return this.PlayerIdList.get((this.currentPlayer + 1) % this.PlayerIdList.size());
    }

    /**
     * ends the current player's turn, and sets this.currentPlayer to the next player to go
     */
    private void endTurn() {
        // loop around the players
        this.currentPlayer = (this.currentPlayer + 1) % this.PlayerIdList.size();
    }

    /**
     * called to end phases
     */
    private void endPhase() {
        if (this.currentGamePhase < GAME_PHASES.length - 1) this.currentGamePhase++;
    }
}

interface GameListener {
    void onChange();
}
