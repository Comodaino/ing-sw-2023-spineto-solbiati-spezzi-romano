package Model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class EndGoalTest {
    @Test
    void TestGetScoreIs0(){
        Player player = new Player("Ale",false);
        EndGoal endGoal = new EndGoal();
        Shelf playerShelf = new Shelf();
        Tile tile1 = new Tile(Color.LIGHTBLUE);
        Tile tile2 = new Tile(Color.GREEN);
        Tile tile3 = new Tile(Color.YELLOW);
        player.getShelf().addTile(0,tile1);
        player.getShelf().addTile(1,tile2);
        player.getShelf().addTile(0,tile3);
        Assertions.assertEquals(0,endGoal.getScore(player));
    }
    @Test
    void TestGetScoreIs1() throws FileNotFoundException {
        Player player = new Player("Ale",false);
        EndGoal endGoal = new EndGoal();
        Shelf shelf = new Shelf();
        File shelfTest = new File("src/test/java/Model/Test_confs/EndGoalShelfTest_conf");
        Scanner reader = new Scanner(shelfTest);
        String sh = reader.nextLine();
        for(int r=0; r<6;r++){
            String st[] = sh.split(",");
            for(int c=0; c<5;c++){
                Tile t = new Tile(Color.valueOf(st[c]));
                player.getShelf().addTile(c,t);
            }
            if (r!=5) sh = reader.nextLine();
        }
        Assertions.assertEquals(1,endGoal.getScore(player));
        Assertions.assertEquals(false,endGoal.getStatus());
        System.out.println("TEST PASSED");


    }
}