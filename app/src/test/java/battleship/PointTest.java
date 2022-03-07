package battleship;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * The PointTest class includes unit tests for the Point class.
 */
public class PointTest {

    @Test
    public void testConstruction() { // Tests constructing various points
        Point p1 = new Point(0, 0);
        Point p2 = new Point(Integer.MAX_VALUE, 0);
        Point p3 = new Point(0, Integer.MIN_VALUE);
    }

    @Test
    public void testEquals() { // Tests equals method on equal points
        assertEquals(new Point(0, 0), new Point(0, 0));
        assertEquals(new Point(1, 0), new Point(1, 0));
        assertEquals(new Point(0, 1), new Point(0, 1));
    }

    @Test
    public void testNotEquals() { // Tests equals method on unequal points
        Point p = new Point(0, 0);
        assertNotEquals(p, new Point(0, 1));
        assertNotEquals(new Point(0, 1), p);
        assertNotEquals(null, p);
    }

    @Test
    public void testGetX() { // Tests getX method on various points
        Point p1 = new Point(0, 0);
        Point p2 = new Point(1, 0);
        Point p3 = new Point(Integer.MAX_VALUE, 0);
        Point p4 = new Point(Integer.MIN_VALUE, 0);
        assertEquals(p1.getX(), 0);
        assertEquals(p2.getX(), 1);
        assertEquals(p3.getX(), Integer.MAX_VALUE);
        assertEquals(p4.getX(), Integer.MIN_VALUE);
    }

    @Test
    public void testGetY() { // Tests getY method on various points
        Point p1 = new Point(0, 0);
        Point p2 = new Point(0, 1);
        Point p3 = new Point(0, Integer.MAX_VALUE);
        Point p4 = new Point(0, Integer.MIN_VALUE);
        assertEquals(p1.getY(), 0);
        assertEquals(p2.getY(), 1);
        assertEquals(p3.getY(), Integer.MAX_VALUE);
        assertEquals(p4.getY(), Integer.MIN_VALUE);
    }

    @Test
    public void testToString() {
        Point p1 = new Point(0, 0);
        Point p2 = new Point(1, 0);
        Point p3 = new Point(0, 1);
        assertEquals(p1.toString(), "(0, 0)");
        assertEquals(p2.toString(), "(1, 0)");
        assertEquals(p3.toString(), "(0, 1)");
    }
}
