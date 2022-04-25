package battleship;

public class ComputerPlayer extends Player {

    private static final int DEFAULT_DIFFICULTY = 0;

    private final int difficulty;

    public ComputerPlayer(int id, Ship[] ships, int board_size) {
        this(id, ships, board_size, DEFAULT_DIFFICULTY);
    }

    public ComputerPlayer(int id, Ship[] ships, int board_size, int difficulty) {
        super(id, ships, board_size);
        this.difficulty = difficulty;
    }

    public Point[] generateShip(int length) {
        int boardSize = this.getBoard().size();
        Point[] points = new Point[2];
        points[0] = new Point((int) (Math.random() * boardSize), (int) (Math.random() * boardSize));
        int multiplier = Math.random() < 0.5 ? 1 : -1;
        if (Math.random() < 0.5) { // horizontal
            points[1] = new Point(points[0].getX() + (length - 1) * multiplier, points[0].getY());
        } else { // vertical
            points[1] = new Point(points[0].getX(), points[0].getY() + (length - 1) * multiplier);
        }
        return new Point[] {new Point(0, 0), new Point(0, 1)};
        // return points;
    }

    public Point getAttackPoint() {
        int boardSize = this.getBoard().size();
        return new Point((int) (Math.random() * boardSize), (int) (Math.random() * boardSize));
    }
}
