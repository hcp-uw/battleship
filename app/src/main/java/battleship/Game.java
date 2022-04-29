package battleship;

import utils.PointUtils;

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
    private static final int[] DEFAULT_SHIPS = {0, 0, 1, 0, 0, 0};
    // default board size is 10x10
    private static final int DEFAULT_SIZE = 10;
    // 3 phases of game
    private static final String[] GAME_PHASES = {"setup", "playing", "end"};

    private final int gameBoardSize;
    private int currentGamePhase;
    private final Map<Integer, Player> players;
    private final List<Integer> playerIdList; // a list containing PIDs
    private int currentPlayerIndex; // current player represented by index in PID list
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
        this.currentPlayerIndex = 0;

        this.playerIdList = new ArrayList<>();
        this.players.keySet().iterator().forEachRemaining(this.playerIdList::add);
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
            int pid = baseId + i;
            this.players.put(pid, new Player(pid, new Ship[0], boardSize));
        }
        this.players.put(2, new ComputerPlayer(2, new Ship[0], boardSize));
    }

    /**
     * Knowing the current player and game phase, only certain things can happen, so given a point we can process
     * an entire step of the game being played (or half a step if in setup). This modifies the game state -
     * phase and current Player
     * @param p a point to process
     */
    public boolean processTurn(Point p) {
        boolean result = false;
        if (this.getPhase().equals("setup")) {
            int bufSize = this.pointBuffer.size();
            if (bufSize % 2 == 1) {
                result = this.addShip(this.getLastPoint(), p);
                if (isPlayerDoneWithSetup(getCurrentPlayer())) {
                    if (isSetupPhaseDone()) {
                        endPhase();
                    }
                    endTurn();
                }
            }
            this.pointBuffer.add(p);

        } else if (this.getPhase().equals("playing")) {
            // does nothing in this version of Game - check TwoPlayerGame where it is implicit which player to attack
            this.pointBuffer.add(p);
            endTurn();
        }
        return result;
        // and do nothing if game phase is something else
    }

    public void computerProcessTurn() {
        if (!(this.players.get(this.getCurrentPlayer()) instanceof ComputerPlayer)) {
            throw new IllegalStateException("Cannot process computer turn for non-computer players");
        }
        ComputerPlayer computerPlayer = (ComputerPlayer) this.players.get(this.getCurrentPlayer());
        if (this.getPhase().equals("setup")) {
            for (int length = allowableShipSet.length - 1; length >= 0; length--) {
                int index = allowableShipSet[length];
                while (index > 0) {
                    Point[] shipPoints = computerPlayer.generateShip(length);
                    if (this.addShip(shipPoints[0], shipPoints[1])) {
                        index--;
                    }
                }
            }
            if (this.isSetupPhaseDone()) {
                this.endPhase();
            }
            this.endTurn();
        } else if (this.getPhase().equals("playing")) {
            while (!this.attack(this.getNextPlayer(), computerPlayer.getAttackPoint())){}
            if (this.playerLost(this.getNextPlayer())){
                this.endPhase(); // don't end the turn if the player has won - keep cur player as winner
            } else {
                this.endTurn();
            }
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
     * Returns a list of board views where the first in the list is the player’s view of their own
     * board, the rest are the player’s view of opponent boards
     * @param pid the ID of the player to get the viewpoint of
     * @return a List of BoardViews
     */
    public List<BoardView> getPlayerView(int pid) {
        Player p = this.players.get(pid);
        List<BoardView> out = new ArrayList<>();
        out.add(p.getBoard());
        for (int otherPid : this.playerIdList) {
            if (otherPid != pid) {
                out.add(this.players.get(otherPid).getBoard());
            }
        }
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
        Set<Point> playerShipPoints = this.players.get(this.getCurrentPlayer()).getShipPoints();
        List<Point> toAddPoints = PointUtils.getPointsBetween(p1, p2);
        for (Point p : toAddPoints) {
            if (playerShipPoints.contains(p)) {
                return false;
            }
        }
        if (!shipInBounds(toAdd, this.gameBoardSize)) return false;
        this.players.get(this.getCurrentPlayer()).addShip(toAdd);
        return true;
    }

    private boolean shipInBounds(Ship s, int boardSize) {
        List<Point> shipPoints = PointUtils.getPointsBetween(s.startPoint(), s.endPoint());
        for (Point p : shipPoints) {
            if (p.getX() < 0 || p.getX() > boardSize - 1) return false;
            if (p.getY() < 0 || p.getY() > boardSize - 1) return false;
        }
        return true;
    }

    private boolean shipsIntersect(Ship s1, Ship s2) {
        List<Point> pointsS1 = PointUtils.getPointsBetween(s1.startPoint(), s1.endPoint());
        List<Point> pointsS2 = PointUtils.getPointsBetween(s2.startPoint(), s2.endPoint());

        // not an efficient algorithm, shouldn't matter unless ships are large
        // there is definitely a mathematical way to check line intersections TODO look for that
        for (Point p1 : pointsS1) {
            for (Point p2 : pointsS2) {
                if (p1.equals(p2)) return true;
            }
        }
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
        return addShip(new Point(x1, y1), new Point(x2, y2));
    }

    /**
     * Gets the Player ID (PID) of the player whose turn is next, the player whose input
     * should take the next turn
     * if the game phase is "end" this returns the winner of the game
     * @return the integer PID of the current player to go
     */
    public int getCurrentPlayer() {
        return this.playerIdList.get(this.currentPlayerIndex);
    }

    /**
     * Gets the player to go by the string name that their PID is mapped to in this.playerNames
     * @return returns the name of the player to go
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
     * checks if a player has set up all their ships or not
     * @param pid the PID of the player to check for
     * @return a boolean indicating if the player is done or not
     */
    public boolean isPlayerDoneWithSetup(int pid) {
        for (int i: getShipsToBePlaced(pid)) {
            if (i > 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * checks to see if the setup phase is effectively finished; all players have set up all their ships
     * does not check what the game's current phase is. If this returns true, the game phase should not be
     * "setup", or should be set to something else shortly after
     * @return a boolean indicating if the setup phase has finished or not
     */
    public boolean isSetupPhaseDone() {
        for (int pid: this.playerIdList) {
            if (!isPlayerDoneWithSetup(pid)) {
                return false;
            }
        }
        return true;
    }

    /**
     * get a preview of the next player to go
     * @return the integer PID of the next player to go
     */
    public int getNextPlayer() {
        return this.playerIdList.get((this.currentPlayerIndex + 1) % this.playerIdList.size());
    }

    /**
     * ends the current player's turn, and sets this.currentPlayer to the next player to go
     */
    public void endTurn() {
        // loop around the players
        this.currentPlayerIndex = (this.currentPlayerIndex + 1) % this.playerIdList.size();
        if (this.players.get(this.getCurrentPlayer()) instanceof ComputerPlayer) {
            computerProcessTurn();
        }
    }

    /**
     * called to end phases
     */
    public void endPhase() {
        if (this.currentGamePhase < GAME_PHASES.length - 1) this.currentGamePhase++;
    }

    /**
     * call to end the game
     * @param winnerPid the PID of the winning player
     */
    private void endGame(int winnerPid) {
        this.currentPlayerIndex = winnerPid;
        this.currentGamePhase = 3;
    }

    /**
     * returns a copy of the last point in the buffer
     * @return a Point
     */
    public Point getLastPoint() {
        Point last = this.pointBuffer.get(this.pointBuffer.size() - 1);
        return new Point(last.getX(), last.getY());
    }

    /**
     * gets a specific player's ships
     * @param pid the PID of the player whose ships to get
     * @return a List of Ships that are their ships
     */
    public List<Ship> getPlayerShips(int pid) {
        return this.players.get(pid).getShips();
    }

    /**
     * Get the ships of the current player to go, to be used in rendering the view for the current player to go
     * @return a List of Ships that are contained by the current player
     */
    public List<Ship> getCurrentPlayerShips() {
        return getPlayerShips(getCurrentPlayer());
    }

    /**
     * gets all the points occupied by the ships of the current player
     * the returned set should not be modified in any way
     * @return a set of Points
     */
    public Set<Point> getCurrentPlayerShipPoints() {
        return this.players.get(getCurrentPlayer()).getShipPoints();
    }

    /**
     * returns if the specified player has lost
     * @param pid the PID of the player whose ship to get
     * @return true if the given player has lost
     */
    public boolean playerLost(int pid){ return this.players.get(pid).hasLost(); }

    /**
     * returns if the specified player has won
     * @param pid the pid of the player to check for win
     * @return true if the given player as won
     */
    public boolean playerWon(int pid) {
        for (int otherPlayer : playerIdList) {
            if (otherPlayer != pid) {
                if (!playerLost(otherPlayer)) {
                    return false;
                }
            }
        }
        return true;
    }
}

interface GameListener {
    void onChange();
}
