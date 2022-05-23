package battleship;

public class SmartAttackGenerator extends AttackGenerator {

    private final int[][] boardPriorities;
    private int numAboveZero;
    private int spacesRemaining;
    private Point prev;

    public SmartAttackGenerator(int boardSize) {
        this.boardPriorities = new int[boardSize][boardSize];
        this.numAboveZero = 0;
        this.spacesRemaining = boardSize * boardSize;
        this.prev = null;
    }

    @Override
    public Point getAttackPoint() {
        if (spacesRemaining == 0) {
            throw new IllegalStateException("No more unguessed spaces on the board");
        }
        if (numAboveZero > 0) {
            prev = hitPhase();
            numAboveZero--;
        } else {
            prev = searchingPhase();
        }
        spacesRemaining--;
        boardPriorities[prev.getY()][prev.getX()] = Integer.MIN_VALUE;
        return prev;
    }

    private Point searchingPhase() {
        for (int row = 0; row < boardPriorities.length; row++) {
            for (int col = row % 2; col < boardPriorities.length; col += 2) {
                if (boardPriorities[row][col] == 0) {
                    return new Point(col, row);
                }
            }
        }
        return null;
    }

    private Point hitPhase() {
        int bestRow = 0;
        int bestCol = 0;
        for (int row = 0; row < boardPriorities.length; row++) {
            for (int col = 0; col < boardPriorities[row].length; col++) {
                if (boardPriorities[row][col] > boardPriorities[bestRow][bestCol]) {
                    bestRow = row;
                    bestCol = col;
                }
            }
        }
        return new Point(bestCol, bestRow);
    }

    @Override
    public void notifyHit() {
        if (prev == null) {
            return;
        }
        int x = prev.getX();
        int y = prev.getY();
        incrementPoint(y - 1, x);
        incrementPoint(y + 1, x);
        incrementPoint(y, x - 1);
        incrementPoint(y, x + 1);
    }

    private void incrementPoint(int row, int col) {
        if (row < 0 || row >= boardPriorities.length
            || col < 0 || col >= boardPriorities.length) {
            return;
        }
        if (boardPriorities[row][col] == 0) {
            numAboveZero++;
        }
        boardPriorities[row][col]++;
    }
}
