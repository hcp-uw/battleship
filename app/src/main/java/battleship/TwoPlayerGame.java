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
        return this.attack(this.getCurrentPlayer(),
                this.PlayerIdList.get((this.currentPlayer + 1) % this.PlayerIdList.size()), p);
    }

    /**
     * In a two player game, it is implicit which player is the target of the attack
     * @param x the x-coord of the point to attack
     * @param y the y-coord of the point to attack
     * @return a boolean representing whether the attack succeeded (point wasn't already guessed)
     */
    public boolean attack(int x, int y) {
        return attack(new Point(x, y));
    }

    @Override
    public boolean processTurn(Point p) {
        boolean result = false;
        if (this.currentGamePhase == 0) {
            int bufSize = this.pointBuffer.size();
            if (bufSize % 2 == 1) {
                result = this.addShip(this.pointBuffer.get(bufSize - 1), p);
            }
            this.pointBuffer.add(p);
        } else if (currentGamePhase == 1) {
            result = this.attack(p);
            this.pointBuffer.add(p);
        }
        return result;
        // and do nothing if game phase is something else
    }
}
