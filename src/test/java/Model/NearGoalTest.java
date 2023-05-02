package Model;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NearGoalTest {

    private Player player;
    private NearGoal neargoal;
    @Test

    public void testNearGoal(){
        Tile[][] m = new Tile[6][5];
        Scanner scanner;
        String inputFile;
        try {
            File shelfFile = new File("src/test/java/Model/Test_confs/shelfTest1_conf_conf");
            scanner = new Scanner(shelfFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        for(int row=0; row<6; row++){
            inputFile = scanner.nextLine();
            for(int col=0; col<5; col++){
                switch(inputFile.charAt(col)){
                    case 'W': m[row][col] = new Tile(Color.WHITE);
                        break;
                    case 'Y': m[row][col] = new Tile(Color.YELLOW);
                        break;
                    case 'P': m[row][col] = new Tile(Color.PINK);
                        break;
                    case 'B': m[row][col] = new Tile(Color.BLUE);
                        break;
                    case 'L': m[row][col] = new Tile(Color.LIGHTBLUE);
                        break;
                    case 'G': m[row][col] = new Tile(Color.GREEN);
                    break;
                }

            }
        }
        for(int i=0; i<6; i++){
            for(int j=0; j<5; j++){
                System.out.print(m[i][j].getColor().toString().charAt(0) + "   ");
            }
            System.out.println();
        }

        this.player = new Player("player1", true, null);
        player.setShelf(m);
        this.neargoal = new NearGoal();
        int tmp = neargoal.getScore(player);
        assertEquals(30, tmp);
        System.out.println("Test Passed");
    }







}