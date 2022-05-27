package utils;

import battleship.Point;

import java.util.ArrayList;
import java.util.List;

public class PointUtils {
    // helper to get all points in between two points in a line, vertically or horizontally
    // inclusively
    public static List<Point> getPointsBetween(Point p1, Point p2) {
        List<Point> out = new ArrayList<>();
        int x1 = p1.getX();
        int x2 = p2.getX();
        int dx = x2 - x1;
        if (dx == 0) { // ship is vertical
            int y1 = p1.getY();
            int y2 = p2.getY();
            int dy = y2 - y1;
            int dir = (dy < 0) ? -1 : 1;
            for (int i = 0; i <= Math.abs(dy); i++) {
                out.add(new Point(p2.getX(), y1 + i*dir));
            }
        } else { // ship must be horizontal
            int dir = (dx < 0) ? -1 : 1;
            for (int i = 0; i <= Math.abs(dx); i++) {
                out.add(new Point(x1 + i*dir, p2.getY() ));
            }
        }
        return out;
    }
}
