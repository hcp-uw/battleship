package battleship;

import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;

public class RandomAttackGenerator extends AttackGenerator {

    private final int boardSize;
    private final Queue<Pair<Integer, Point>> pointQueue;

    public RandomAttackGenerator(int boardSize) {
        this.boardSize = boardSize;
        this.pointQueue = new PriorityQueue<>(new PriorityPointPairComparator());
        populatePointQueue();
    }

    private void populatePointQueue() {
        Random random = new Random();
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                int priority = random.nextInt();
                Point point = new Point(i, j);
                pointQueue.add(new Pair<>(priority, point));
            }
        }
    }

    @Override
    public Point getAttackPoint() {
        if (pointQueue.isEmpty()) {
            throw new IllegalStateException("No more unguessed spaces on the board");
        }
        return pointQueue.remove().getValue();
    }
}
