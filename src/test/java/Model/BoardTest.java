package Model;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    private List<Player> playerList;
    public void testRemoveTile() {
        playerList=new ArrayList<Player>();
        Board testBoard = new Board(false, playerList);
        testBoard.removeTile(5,5);
        assertEquals(testBoard.getTile(5,5),null);
    }
}