package Model.CommonGoals;

import Model.Color;
import Model.Player;
import Model.Shelf;
import Model.Tile;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GoalAnglesTest {

    @Test
    void emptyShelf(){
        GoalAngles goal = new GoalAngles();
        Player p = new Player("Nico", true);
        Shelf s = new Shelf();

        assertEquals(0, goal.getScore(s, p));
        System.out.println("TEST PASSED");
    }

    @Test
    void goalCompletedByOnePlayer(){
        GoalAngles goal = new GoalAngles();
        Player p = new Player("Nico", true);
        Shelf s = new Shelf();

        for(int i=0; i<6; i++){
            if(i==0 || i==5){
                s.addTile(0, new Tile(Color.WHITE));
                s.addTile(4, new Tile(Color.WHITE));
            } else {
                s.addTile(0, new Tile(Color.BLUE));
                s.addTile(4, new Tile(Color.BLUE));
            }
        }
        assertEquals(8, goal.getScore(s, p));
        System.out.println("TEST PASSED"); //color: White
    }

    @Test
    void theSamePlayerCannotCompleteTheSameGoalTwoTimes(){
        GoalAngles goal = new GoalAngles();
        Player p = new Player("Nico", true);
        Shelf s = new Shelf();

        for(int i=0; i<6; i++){
            if(i==0 || i==5){
                s.addTile(0, new Tile(Color.BLUE));
                s.addTile(4, new Tile(Color.BLUE));
            } else {
                s.addTile(0, new Tile(Color.GREEN));
                s.addTile(4, new Tile(Color.GREEN));
            }
        }
        assertEquals(8, goal.getScore(s, p)); //Nico completes the goal for the first time
        assertEquals(0, goal.getScore(s, p)); //Nico can't take other points from this goal
        System.out.println("TEST PASSED");
    }

    @Test
    void goalCompletedByFourPlayers(){
        GoalAngles goal = new GoalAngles();
        Player p1 = new Player("Nico", true);
        Player p2 = new Player("Alessio", false);
        Player p3 = new Player("Clara", false);
        Player p4 = new Player("Alessandra", false);
        Shelf s = new Shelf();

        for(int i=0; i<6; i++){
            if(i==0 || i==5){
                s.addTile(0, new Tile(Color.BLUE));
                s.addTile(4, new Tile(Color.BLUE));
            } else {
                s.addTile(0, new Tile(Color.GREEN));
                s.addTile(4, new Tile(Color.GREEN));
            }
        }
        assertEquals(8, goal.getScore(s, p1));
        assertEquals(6, goal.getScore(s, p2));
        assertEquals(4, goal.getScore(s, p3));
        assertEquals(2, goal.getScore(s, p4));
        //assertEquals(0, goal.getScore(s, p2)); the same player cannot complete the same goal two times
        System.out.println("TEST PASSED");
    }
}