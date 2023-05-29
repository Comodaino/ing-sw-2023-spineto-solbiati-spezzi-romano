package Model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class EndGoalTest {
    @Test
    void TestGetSCore(){
        Player player = new Player("Ale",false);
        EndGoal endGoal = new EndGoal();
        endGoal.getStatus();
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


    }


}