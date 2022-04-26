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
        return this.attack(this.getCurrentPlayer(), getNextPlayer(), p);
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
        if (getPhase().equals("setup")) {
            int bufSize = super.pointBuffer.size();
            if (bufSize % 2 == 1) {
                result = super.addShip(this.getLastPoint(), p);
                if (isPlayerDoneWithSetup(getCurrentPlayer())) {
                    if (isSetupPhaseDone()) {
                        endPhase();
                    } else {
                        endTurn();
                    }
                }
            }
            super.pointBuffer.add(p);
        } else if (getPhase().equals("playing")) {
            result = this.attack(p);
            super.pointBuffer.add(p);
            if (super.playerLost(getNextPlayer())){
                super.endPhase(); // don't end the turn if the player has won - keep cur player as winner
            } else {
                endTurn();
            }
        }
        return result;
        // and do nothing if game phase is something else
    }
}
