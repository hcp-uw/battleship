package battleship;

public class TwoPlayerGame extends Game {
    public TwoPlayerGame(int boardSize, int[] shipsInfo) {
        super(2, boardSize, shipsInfo);
    }

    /**
     * In a two player game, it is implicit which player is the target of the attack
     * @param p the Point to attack
     * @return a boolean representing whether or not the attack succeeded (point wasn't already guessed)
     */
    public boolean attack(Point p) {
        return false;
    }

    public boolean attack(int x, int y) {
        return false;
    }
}
