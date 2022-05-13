package battleship;

import java.util.Comparator;

public class PriorityPointPairComparator implements Comparator<Pair<Integer, Point>> {
    @Override
    public int compare(Pair<Integer, Point> p1, Pair<Integer, Point> p2) {
        return p2.getKey() - p1.getKey();
    }
}
