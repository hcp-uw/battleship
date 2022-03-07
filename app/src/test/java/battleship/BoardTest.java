package battleship;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


class BoardTest {
    private final Point w = new Point(0,0);
    private final Point x = new Point(1,1);
    private final Point y = new Point(2,3);
    private final Point z = new Point(3,2);


    // tests negative size input for Board
    @Test
    public void testConstructor(){
        assertThrows(IllegalArgumentException.class, () -> new Board(-1));
    }

    // tests addHit
    @Test
    public void testAddHit(){
        //illegal inputs
        Board empty = new Board(10);
        assertThrows(IllegalArgumentException.class, () -> empty.addHit(null));
        assertThrows(IllegalArgumentException.class, () -> empty.addHit(new Point(-1, 3)));
        assertThrows(IllegalArgumentException.class, () -> empty.addHit(new Point (1, -3)));
        assertThrows(IllegalArgumentException.class, () -> empty.addHit(new Point(1, 10)));
        assertThrows(IllegalArgumentException.class, () -> empty.addHit(new Point (10, 1)));

        Board b = new Board(10);
        assertTrue(b.addHit(x));
        assertTrue(b.addHit(y));
        assertFalse(b.addHit(x));
        b.addMiss(z);
        assertFalse(b.addHit(z));
    }

    // tests addMiss
    @Test
    public void testAddMiss(){
        //illegal inputs
        Board empty = new Board(10);
        assertThrows(IllegalArgumentException.class, () -> empty.addMiss(null));
        assertThrows(IllegalArgumentException.class, () -> empty.addMiss(new Point(-1, 3)));
        assertThrows(IllegalArgumentException.class, () -> empty.addMiss(new Point (1, -3)));
        assertThrows(IllegalArgumentException.class, () -> empty.addMiss(new Point(1, 10)));
        assertThrows(IllegalArgumentException.class, () -> empty.addMiss(new Point (10, 1)));

        Board b = new Board(10);
        assertTrue(b.addMiss(x));
        assertTrue(b.addMiss(y));
        assertFalse(b.addMiss(x));
        b.addHit(z);
        assertFalse(b.addMiss(z));
    }

    // tests getHits
    @Test
    public void testGetHits(){
        Board b = new Board(10);
        assertEquals(b.getHits(), new HashSet<>());
        b.addHit(x);
        assertEquals(b.getHits(), Set.of(x));
        b.addMiss(y);
        assertEquals(b.getHits(), Set.of(x));
        b.addHit(y);
        assertEquals(b.getHits(), Set.of(x));
        b.addMiss(x);
        assertEquals(b.getHits(), Set.of(x));
        b.addHit(z);
        assertEquals(b.getHits(), Set.of(x, z));
    }

    // tests getMisses
    @Test
    public void testGetMisses(){
        Board b = new Board(10);
        assertEquals(b.getMisses(), new HashSet<>());
        b.addHit(x);
        assertEquals(b.getMisses(), new HashSet<>());
        b.addMiss(y);
        assertEquals(b.getMisses(), Set.of(y));
        b.addHit(y);
        assertEquals(b.getMisses(), Set.of(y));
        b.addMiss(x);
        assertEquals(b.getMisses(), Set.of(y));
        b.addMiss(z);
        assertEquals(b.getMisses(), Set.of(y, z));
    }

    // tests hasAlreadyGuessed
    @Test
    public void testHasAlreadyGuessed(){
        Board b = new Board(10);
        assertThrows(IllegalArgumentException.class, () -> b.hasAlreadyGuessed(null));

        assertFalse(b.hasAlreadyGuessed(w));
        b.addHit(w);
        assertTrue(b.hasAlreadyGuessed(w));

        assertFalse(b.hasAlreadyGuessed(x));
        b.addMiss(x);
        assertTrue(b.hasAlreadyGuessed(x));

        assertFalse(b.hasAlreadyGuessed(y));
        b.addHit(y);
        b.addMiss(y);
        assertTrue(b.hasAlreadyGuessed(y));

        assertFalse(b.hasAlreadyGuessed(z));
        b.addMiss(z);
        b.addHit(z);
        assertTrue(b.hasAlreadyGuessed(z));
    }

}