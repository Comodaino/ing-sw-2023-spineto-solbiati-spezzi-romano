package Model.CommonGoals;

import Model.Color;
import Model.Player;
import Model.Shelf;
import Model.Tile;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GoalCouplesTest {

    @Test
    void emptyShelf(){
        GoalCouples goal = new GoalCouples();
        Player p = new Player("Nico", true);
        Shelf s = new Shelf();

        assertEquals(0, goal.getScore(s, p));
        System.out.println("TEST PASSED");
    }

    @Test
    void goalCompletedByOnePlayer(){
        GoalCouples goal = new GoalCouples();
        Player p = new Player("Nico", true);
        Shelf s = new Shelf();

        s.addTile(0, new Tile(Color.WHITE));
        s.addTile(0, new Tile(Color.WHITE));

        assertEquals(8, goal.getScore(s, p));
        System.out.println("TEST PASSED"); //color: White
    }
}