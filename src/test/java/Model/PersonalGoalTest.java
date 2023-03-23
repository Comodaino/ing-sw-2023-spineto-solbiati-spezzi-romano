package Model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

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
    public void testGetScoreOneMatchingTile() {
        Shelf  playerShelf = new Shelf();
        PersonalGoal personalGoal = new PersonalGoal(playerShelf);
        Tile t1 = new Tile(Color.GREEN);
        playerShelf.addTile(0,t1);
        personalGoal.getPersGoal(1);
        int expectedScore = 1;
        int actualScore = personalGoal.getScore(playerShelf);
        Assertions.assertEquals(expectedScore, actualScore);
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
}