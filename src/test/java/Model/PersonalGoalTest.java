package Model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class PersonalGoalTest {

    @Test
    public void TestGetScoreNoMatchingTile(){
        Shelf shelf = new Shelf();
        ArrayList<Pgtype> pGoal = new ArrayList<>();
        PersonalGoal personalGoal = new PersonalGoal(shelf);
        personalGoal.getPGoal();
        int i=personalGoal.getScore(shelf);
        Assertions.assertEquals(0,i);
    }
    @Test
    public void testGetScoreShelfIs1() {
        Shelf  playerShelf = new Shelf();
        PersonalGoal personalGoal = new PersonalGoal(playerShelf);
        Tile t1 = new Tile(Color.GREEN);
        playerShelf.addTile(0,t1);
        personalGoal.getPersGoal(1);
        int expectedScore = 1;
        int actualScore = personalGoal.getScore(playerShelf);
        Assertions.assertEquals(expectedScore, actualScore);
        System.out.println("TEST PASSED");
    }
    @Test
    public void testGetScorePlayerIs1(){
        Shelf  playerShelf = new Shelf();
        PersonalGoal personalGoal = new PersonalGoal(playerShelf);
        Player player= new Player("Paolo",false);
        Tile t1 = new Tile(Color.GREEN);
        playerShelf.addTile(0,t1);
        personalGoal.getPersGoal(1);
        player.getShelf().addTile(0,t1);
        int expectedScore = 1;
        int actualScorePlayer = personalGoal.getScore(player);
        Assertions.assertEquals(expectedScore,actualScorePlayer);
        System.out.println("TEST PASSED");
    }

    @Test
    public void TestCreatePersGoal(){
        Shelf playerShelf = new Shelf();
        ArrayList<Pgtype> pGoal = new ArrayList<>();
        PersonalGoal personalGoal= new PersonalGoal(playerShelf);
        personalGoal.CreatePersonalGoal();
        for(int i=0; i<personalGoal.getPGoal().size();i++){
            System.out.print(personalGoal.getPGoal().get(i).getColor().toString()+" ");
            System.out.print(personalGoal.getPGoal().get(i).getLine()+" ");
            System.out.println(personalGoal.getPGoal().get(i).getCol()+" ");

        }

    }

    @Test
    public void TestAllPersonalGoal(){

        for (int i=0;i<12;i++){
            Shelf playerShelf = new Shelf();
            ArrayList<Pgtype> pGoal = new ArrayList<>();
            PersonalGoal personalGoal = new PersonalGoal(playerShelf);
            int j = (1+ (6*i));
            personalGoal.getPersGoal(j);
            System.out.println("This is the personal Goal number: "+(i+1));
            for(int k=0; k<6;k++){
                System.out.print(personalGoal.getPGoal().get(k).getColor().toString()+" ");
                System.out.print(personalGoal.getPGoal().get(k).getLine()+" ");
                System.out.println(personalGoal.getPGoal().get(k).getCol()+" ");

            }

        }
    }
    @Test
    public void testGetPersGoal() {
        Shelf shelf = new Shelf();
        PersonalGoal personalGoal = new PersonalGoal(shelf);
        ArrayList<Pgtype> pGoal =new ArrayList<>();
        personalGoal.CreatePersonalGoal();
        Assertions.assertEquals(6, personalGoal.getPGoal().size());
    }


    @Test
    public void testGetScoreShelAndPlayerfIs2() {
        Shelf  playerShelf = new Shelf();
        PersonalGoal personalGoal = new PersonalGoal(playerShelf);
        Player player= new Player("Paolo",false);
        Tile t1 = new Tile(Color.LIGHTBLUE);
        playerShelf.addTile(1,t1);
        player.getShelf().addTile(1,t1);
        Tile t2 = new Tile(Color.BLUE);
        playerShelf.addTile(1,t2);
        player.getShelf().addTile(1,t2);
        Tile t3 = new Tile(Color.YELLOW);
        playerShelf.addTile(1,t3);
        player.getShelf().addTile(1,t3);
        personalGoal.getPersGoal(7);
        int expectedScore = 2;
        int actualScoreShelf = personalGoal.getScore(playerShelf);
        int actualScorePlayer = personalGoal.getScore(player);
        Assertions.assertEquals(expectedScore, actualScoreShelf);
        Assertions.assertEquals(expectedScore,actualScorePlayer);
        System.out.println("TEST PASSED");
    }
    @Test
    public void testGetScoreShelAndPlayerfIs12() throws FileNotFoundException {
        Shelf  playerShelf = new Shelf();
        PersonalGoal personalGoal = new PersonalGoal(playerShelf);
        Player player= new Player("ALE",false);
        File personalGoalTest= new File("src/test/java/Model/Test_confs/PersonalGoalTest_conf");
        Scanner reader = new Scanner(personalGoalTest);
        String pg = reader.nextLine();
        for(int r=0; r<6;r++){
            String st[] = pg.split(",");
            for(int c=0; c<5;c++){
                Tile t = new Tile(Color.valueOf(st[c]));
                player.getShelf().addTile(c,t);
                playerShelf.addTile(c,t);
            }
            if(r!=5) pg = reader.nextLine();
        }
        personalGoal.getPersGoal(67);
        int expectedScore = 12;
        int actualScoreShelf = personalGoal.getScore(playerShelf);
        int actualScorePlayer = personalGoal.getScore(player);
         Assertions.assertEquals(expectedScore, actualScoreShelf);
         Assertions.assertEquals(expectedScore,actualScorePlayer);
        System.out.println("TEST PASSED");
    }

}