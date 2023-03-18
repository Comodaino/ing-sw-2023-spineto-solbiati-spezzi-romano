package Model;

import junit.framework.TestCase;

public class BoardTest extends TestCase {

    public void testRemoveTile() {
        Board testBoard = new Board();
        testBoard.removeTile(5,5);
        assertEquals(testBoard.getTile(5,5),null);
    }
}