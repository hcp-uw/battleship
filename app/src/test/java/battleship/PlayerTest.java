package battleship;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    @Test
    public void testConstructorThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Player(0, null));
        assertThrows(IllegalArgumentException.class, () -> new Player(0, new Ship[]{}));
    }

    @Test
    public void testAttackThrowsIllegalArgumentException() {
        Player p1 = new Player(0, new Ship[] {new Ship(new Point(0,0), new Point(10,10))});
        Player p2 = new Player(1, new Ship[] {new Ship(new Point(0,1), new Point(1,10))});

        assertThrows(IllegalArgumentException.class, () -> p1.attack(null, new Point(0,1)));
        assertThrows(IllegalArgumentException.class, () -> p1.attack(p2, null));
    }

    @Test
    public void testReceiveNullThrowsException() {
        Player p1 = new Player(0, new Ship[] {new Ship(new Point(0,0), new Point(10,10))});
        assertThrows(IllegalArgumentException.class, () -> p1.receive(null));
    }

    @Test
    public void testPlayerEquality() {
        Player p1 = new Player(0, new Ship[] {new Ship(new Point(0,0), new Point(10,10))});
        Player p1o = p1;
        assertEquals(p1, p1);
        assertEquals(p1, p1o);
        assertEquals(p1o, p1);
        Player p2 = new Player(1, new Ship[] {new Ship(new Point(0,1), new Point(1,10))});
        assertNotEquals(p2, p1);
        assertNotEquals(p1, p2);
    }
}
