package Model.CommonGoals;

import Model.Color;
import Model.Player;
import Model.Shelf;
import Model.Tile;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class GoalStairTest {
    public void configShelf(Shelf shelf, Scanner scanner){
        while(scanner.hasNextLine()){
            String confBuffer = scanner.nextLine();
            for(int c=0; c<5; c++){
                switch(confBuffer.charAt(c)){
                    case 'W': shelf.addTile(c, new Tile(Color.WHITE));
                        break;
                    case 'B': shelf.addTile(c, new Tile(Color.BLUE));
                        break;
                    case 'P': shelf.addTile(c, new Tile(Color.PINK));
                        break;
                    case 'G': shelf.addTile(c, new Tile(Color.GREEN));
                        break;
                    case 'Y': shelf.addTile(c, new Tile(Color.YELLOW));
                        break;
                    case 'L': shelf.addTile(c, new Tile(Color.LIGHTBLUE));
                        break;
                    case '-': shelf.addTile(c, null);
                }
            }
        }
    }

    @Test
    void emptyShelf(){
        GoalStair goal = new GoalStair(2);
        Player p = new Player("Nico", true);

        assertEquals(0, goal.getScore(p));
        System.out.println("TEST PASSED");
    }

    @Test
    void goalCompletedByOnePlayer() throws FileNotFoundException {
        GoalStair goal = new GoalStair(2);
        Player p = new Player("Nico", true);

        File shelfConf = new File("src/test/java/Model/CommonGoals/ShelfConfigs/stair_conf");
        Scanner reader = new Scanner(shelfConf);
        configShelf(p.getShelf(), reader);

        assertEquals(8, goal.getScore(p));
        System.out.println("TEST PASSED");
    }

    @Test
    void theSamePlayerCannotCompleteTheSameGoalTwoTimes() throws FileNotFoundException {
        GoalStair goal = new GoalStair(2);
        Player p = new Player("Nico", true);

        File shelfConf = new File("src/test/java/Model/CommonGoals/ShelfConfigs/stair_conf");
        Scanner reader = new Scanner(shelfConf);
        configShelf(p.getShelf(), reader);

        assertEquals(8, goal.getScore(p)); //Nico completes the goal for the first time
        assertEquals(0, goal.getScore(p)); //Nico can't take other points from this goal
        System.out.println("TEST PASSED");
    }

    @Test
    void goalCompletedByTwoPlayers() throws FileNotFoundException {
        GoalStair goal = new GoalStair(2);
        Player p1 = new Player("Nico", true);
        Player p2 = new Player("Alessio", false);

        File shelfConf = new File("src/test/java/Model/CommonGoals/ShelfConfigs/stair_conf");
        Scanner reader1 = new Scanner(shelfConf);
        Scanner reader2 = new Scanner(shelfConf);
        configShelf(p1.getShelf(), reader1);
        configShelf(p2.getShelf(), reader2);

        assertEquals(8, goal.getScore(p1));
        assertEquals(4, goal.getScore(p2));
        //assertEquals(0, goal.getScore(p2)); the same player cannot complete the same goal two times
        System.out.println("TEST PASSED");
    }

    @Test
    void goalCompletedByFourPlayers() throws FileNotFoundException {
        GoalStair goal = new GoalStair(4);
        Player p1 = new Player("Nico", true);
        Player p2 = new Player("Alessio", false);
        Player p3 = new Player("Clara", false);
        Player p4 = new Player("Alessandra", false);

        File shelfConf = new File("src/test/java/Model/CommonGoals/ShelfConfigs/stair_conf");
        Scanner reader1 = new Scanner(shelfConf);
        Scanner reader2 = new Scanner(shelfConf);
        Scanner reader3 = new Scanner(shelfConf);
        Scanner reader4 = new Scanner(shelfConf);
        configShelf(p1.getShelf(), reader1);
        configShelf(p2.getShelf(), reader2);
        configShelf(p3.getShelf(), reader3);
        configShelf(p4.getShelf(), reader4);

        assertEquals(8, goal.getScore(p1));
        assertEquals(6, goal.getScore(p2));
        assertEquals(4, goal.getScore(p3));
        assertEquals(2, goal.getScore(p4));
        //assertEquals(0, goal.getScore(p2)); the same player cannot complete the same goal two times
        System.out.println("TEST PASSED");
    }
}