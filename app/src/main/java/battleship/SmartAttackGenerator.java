package battleship;

import java.util.PriorityQueue;
import java.util.Queue;

public class SmartAttackGenerator extends AttackGenerator {

    private final int boardSize;
    private final Queue<Pair<Integer, Point>> pointQueue;

    public SmartAttackGenerator(int boardSize) {
        this.boardSize = boardSize;
        this.pointQueue = new PriorityQueue<>(new PriorityPointPairComparator());
    }

    @Override
    public Point getAttackPoint() {
        // TODO
        return null;
    }
}
