package Model;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class NearGoalTest {

    private Player player;
    private NearGoal neargoal;
    @Test

    public void testNearGoal(){
        Tile m[][] = new Tile[6][5];

        Scanner scanner = null;
        try {
            File shelfFile = new File("src/test/java/Model/Test_confs/shelfTest1.json");
            scanner = new Scanner(shelfFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        for(int row=0; row<6; row++){
            String inputFile = scanner.nextLine();
            for(int col=0; col<5; col++){
                switch(inputFile.charAt(col)){
                    case 'w': m[row][col] = new Tile(Color.WHITE);
                    case 'Y': m[row][col] = new Tile(Color.YELLOW);
                    case 'P': m[row][col] = new Tile(Color.PINK);
                    case 'B': m[row][col] = new Tile(Color.BLUE);
                    case 'L': m[row][col] = new Tile(Color.LIGHTBLUE);
                    case 'G': m[row][col] = new Tile(Color.GREEN);
                    break;
                }

            }
        }
        this.player = new Player("player1", true);
        player.setShelf(m);
        this.neargoal = new NearGoal();
        int tmp = neargoal.getScore(player);
        assertEquals(30, tmp);
        System.out.println("Test Passed");
    }







}