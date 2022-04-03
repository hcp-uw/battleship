package battleship;

public class TwoPlayerGame extends Game {
    public TwoPlayerGame(int boardSize, int[] shipsInfo) {
        super(2, boardSize, shipsInfo);
    }

    public TwoPlayerGame(int boardSize) {
        super(2, boardSize);
    }

    /**
     * In a two player game, it is implicit which player is the target of the attack
     * @param p the Point to attack
     * @return a boolean representing whether the attack succeeded (point wasn't already guessed)
     */
    public boolean attack(Point p) {
        return false;
    }

    /**
     * In a two player game, it is implicit which player is the target of the attack
     * @param x the x-coord of the point to attack
     * @param y the y-coord of the point to attack
     * @return a boolean representing whether the attack succeeded (point wasn't already guessed)
     */
    public boolean attack(int x, int y) {
        return false;
    }
}
