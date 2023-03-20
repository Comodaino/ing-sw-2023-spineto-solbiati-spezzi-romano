package Model.CommonGoals;

import Model.Color;
import Model.Player;
import Model.Shelf;
import Model.Tile;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GoalCrossTest {

    @Test
    void emptyShelf(){
        GoalCross goal = new GoalCross();
        Player p = new Player("Nico", true);
        Shelf s = new Shelf();

        assertEquals(0, goal.getScore(s, p));
        System.out.println("TEST PASSED");
    }

    @Test
    void goalCompletedByOnePlayer(){
        GoalCross goal = new GoalCross();
        Player p = new Player("Nico", true);
        Shelf s = new Shelf();

        for(int r=0; r<5; r++){
            switch (r){
                case 0: s.addTile(2, new Tile(Color.BLUE));
                case 1: s.addTile(2, new Tile(Color.WHITE));
                case 2: s.addTile(2, new Tile(Color.WHITE));
                case 3: s.addTile(2, new Tile(Color.BLUE));
                case 4: s.addTile(2, new Tile(Color.WHITE));
            }
        }
        for(int r=0; r<4; r++){
            switch (r){
                case 0: s.addTile(3, new Tile(Color.PINK));
                case 1: s.addTile(3, new Tile(Color.WHITE));
                case 2: s.addTile(3, new Tile(Color.GREEN));
                case 3: s.addTile(3, new Tile(Color.WHITE));
            }
        }
        for(int r=0; r<5; r++){
            switch (r){
                case 0: s.addTile(4, new Tile(Color.BLUE));
                case 1: s.addTile(4, new Tile(Color.WHITE));
                case 2: s.addTile(4, new Tile(Color.WHITE));
                case 3: s.addTile(4, new Tile(Color.BLUE));
                case 4: s.addTile(4, new Tile(Color.WHITE));
            }
        }

        assertEquals(8, goal.getScore(s, p));
        System.out.println("TEST PASSED");
    }

    @Test
    void theSamePlayerCannotCompleteTheSameGoalTwoTimes(){
        GoalCross goal = new GoalCross();
        Player p = new Player("Nico", true);
        Shelf s = new Shelf();

        for(int r=0; r<5; r++){
            switch (r){
                case 0: s.addTile(2, new Tile(Color.BLUE));
                case 1: s.addTile(2, new Tile(Color.WHITE));
                case 2: s.addTile(2, new Tile(Color.WHITE));
                case 3: s.addTile(2, new Tile(Color.BLUE));
                case 4: s.addTile(2, new Tile(Color.WHITE));
            }
        }
        for(int r=0; r<4; r++){
            switch (r){
                case 0: s.addTile(3, new Tile(Color.PINK));
                case 1: s.addTile(3, new Tile(Color.WHITE));
                case 2: s.addTile(3, new Tile(Color.GREEN));
                case 3: s.addTile(3, new Tile(Color.WHITE));
            }
        }
        for(int r=0; r<5; r++){
            switch (r){
                case 0: s.addTile(4, new Tile(Color.BLUE));
                case 1: s.addTile(4, new Tile(Color.WHITE));
                case 2: s.addTile(4, new Tile(Color.WHITE));
                case 3: s.addTile(4, new Tile(Color.BLUE));
                case 4: s.addTile(4, new Tile(Color.WHITE));
            }
        }

        assertEquals(8, goal.getScore(s, p));
        assertEquals(0, goal.getScore(s, p));
        System.out.println("TEST PASSED");
    }

    @Test
    void goalCompletedByFourPlayers(){
        GoalCross goal = new GoalCross();
        Player p1 = new Player("Nico", true);
        Player p2 = new Player("Alessio", false);
        Player p3 = new Player("Clara", false);
        Player p4 = new Player("Alessandra", false);
        Shelf s = new Shelf();

        for(int r=0; r<5; r++){
            switch (r){
                case 0: s.addTile(2, new Tile(Color.BLUE));
                case 1: s.addTile(2, new Tile(Color.WHITE));
                case 2: s.addTile(2, new Tile(Color.WHITE));
                case 3: s.addTile(2, new Tile(Color.BLUE));
                case 4: s.addTile(2, new Tile(Color.WHITE));
            }
        }
        for(int r=0; r<4; r++){
            switch (r){
                case 0: s.addTile(3, new Tile(Color.PINK));
                case 1: s.addTile(3, new Tile(Color.WHITE));
                case 2: s.addTile(3, new Tile(Color.GREEN));
                case 3: s.addTile(3, new Tile(Color.WHITE));
            }
        }
        for(int r=0; r<5; r++){
            switch (r){
                case 0: s.addTile(4, new Tile(Color.BLUE));
                case 1: s.addTile(4, new Tile(Color.WHITE));
                case 2: s.addTile(4, new Tile(Color.WHITE));
                case 3: s.addTile(4, new Tile(Color.BLUE));
                case 4: s.addTile(4, new Tile(Color.WHITE));
            }
        }

        assertEquals(8, goal.getScore(s, p1));
        assertEquals(6, goal.getScore(s, p2));
        assertEquals(4, goal.getScore(s, p3));
        assertEquals(2, goal.getScore(s, p4));
        System.out.println("TEST PASSED");
    }
}