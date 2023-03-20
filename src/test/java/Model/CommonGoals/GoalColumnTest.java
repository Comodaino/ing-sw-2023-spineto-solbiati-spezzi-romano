package Model.CommonGoals;

import Model.Color;
import Model.Player;
import Model.Shelf;
import Model.Tile;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GoalColumnTest {

    @Test
    void emptyShelf(){
        GoalColumn goal = new GoalColumn();
        Player p = new Player("Nico", true);
        Shelf s = new Shelf();

        assertEquals(0, goal.getScore(s, p));
        System.out.println("TEST PASSED");
    }

    @Test
    void goalCompletedByOnePlayer(){
        GoalColumn goal = new GoalColumn();
        Player p = new Player("Nico", true);
        Shelf s = new Shelf();

        s.addTile(0, new Tile(Color.WHITE));
        s.addTile(0, new Tile(Color.BLUE));
        s.addTile(0, new Tile(Color.LIGHTBLUE));
        s.addTile(0, new Tile(Color.WHITE));
        s.addTile(0, new Tile(Color.BLUE));
        s.addTile(0, new Tile(Color.BLUE));

        s.addTile(3, new Tile(Color.PINK));
        s.addTile(3, new Tile(Color.GREEN));
        s.addTile(3, new Tile(Color.GREEN));
        s.addTile(3, new Tile(Color.GREEN));
        s.addTile(3, new Tile(Color.PINK));
        s.addTile(3, new Tile(Color.PINK));

        s.addTile(4, new Tile(Color.GREEN));
        s.addTile(4, new Tile(Color.GREEN));
        s.addTile(4, new Tile(Color.GREEN));
        s.addTile(4, new Tile(Color.GREEN));
        s.addTile(4, new Tile(Color.GREEN));
        s.addTile(4, new Tile(Color.GREEN));

        assertEquals(8, goal.getScore(s, p));
        System.out.println("TEST PASSED");
    }

    @Test
    void theSamePlayerCannotCompleteTheSameGoalTwoTimes(){
        GoalColumn goal = new GoalColumn();
        Player p = new Player("Nico", true);
        Shelf s = new Shelf();

        s.addTile(0, new Tile(Color.WHITE));
        s.addTile(0, new Tile(Color.BLUE));
        s.addTile(0, new Tile(Color.LIGHTBLUE));
        s.addTile(0, new Tile(Color.WHITE));
        s.addTile(0, new Tile(Color.BLUE));
        s.addTile(0, new Tile(Color.BLUE));

        s.addTile(3, new Tile(Color.PINK));
        s.addTile(3, new Tile(Color.GREEN));
        s.addTile(3, new Tile(Color.GREEN));
        s.addTile(3, new Tile(Color.GREEN));
        s.addTile(3, new Tile(Color.PINK));
        s.addTile(3, new Tile(Color.PINK));

        s.addTile(4, new Tile(Color.GREEN));
        s.addTile(4, new Tile(Color.GREEN));
        s.addTile(4, new Tile(Color.GREEN));
        s.addTile(4, new Tile(Color.GREEN));
        s.addTile(4, new Tile(Color.GREEN));
        s.addTile(4, new Tile(Color.GREEN));

        assertEquals(8, goal.getScore(s, p));
        assertEquals(0, goal.getScore(s, p));
        System.out.println("TEST PASSED");
    }

    @Test
    void goalCompletedByFourPlayers(){
        GoalColumn goal = new GoalColumn();
        Player p1 = new Player("Nico", true);
        Player p2 = new Player("Alessio", false);
        Player p3 = new Player("Clara", false);
        Player p4 = new Player("Alessandra", false);
        Shelf s = new Shelf();

        s.addTile(0, new Tile(Color.WHITE));
        s.addTile(0, new Tile(Color.BLUE));
        s.addTile(0, new Tile(Color.LIGHTBLUE));
        s.addTile(0, new Tile(Color.LIGHTBLUE));
        s.addTile(0, new Tile(Color.LIGHTBLUE));
        s.addTile(0, new Tile(Color.BLUE));

        s.addTile(3, new Tile(Color.PINK));
        s.addTile(3, new Tile(Color.GREEN));
        s.addTile(3, new Tile(Color.GREEN));
        s.addTile(3, new Tile(Color.GREEN));
        s.addTile(3, new Tile(Color.PINK));
        s.addTile(3, new Tile(Color.PINK));

        s.addTile(4, new Tile(Color.GREEN));
        s.addTile(4, new Tile(Color.GREEN));
        s.addTile(4, new Tile(Color.GREEN));
        s.addTile(4, new Tile(Color.GREEN));
        s.addTile(4, new Tile(Color.GREEN));
        s.addTile(4, new Tile(Color.GREEN));

        assertEquals(8, goal.getScore(s, p1));
        assertEquals(6, goal.getScore(s, p2));
        assertEquals(4, goal.getScore(s, p3));
        assertEquals(2, goal.getScore(s, p4));
        System.out.println("TEST PASSED");
    }
}