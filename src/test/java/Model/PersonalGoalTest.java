package Model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PersonalGoalTest {

    @Test
    public void TestGetScoreNOMatchingTile(){
        Shelf playerShelf = new Shelf();
        PersonalGoal personalGoal= new PersonalGoal(playerShelf);
        personalGoal.getPersGoal(0);
        int expectedScore=0;
        int actualScore=personalGoal.getScore(playerShelf);
        Assertions.assertEquals(expectedScore,actualScore);
    }
    @Test
    public void TestGetScoreOneMatchingTile(){
        Shelf playerShelf = new Shelf();
        PersonalGoal personalGoal= new PersonalGoal(playerShelf);
        Tile t= new Tile(Color.GREEN);
        playerShelf.addTile(1,t);
        personalGoal.getPersGoal(0);
        Assertions.assertEquals(0,personalGoal.getScore(playerShelf));

    }
    @Test
    public void TestCreatePersGoal(){
        Shelf playerShelf = new Shelf();
        ArrayList<Pgtype> pGoal = new ArrayList<>();
        PersonalGoal personalGoal= new PersonalGoal(playerShelf);
        personalGoal.getPersGoal(0);
        for(int i=0; i<personalGoal.getPGoal().size();i++){
            System.out.print(personalGoal.getPGoal().get(i).getColor().toString()+" ");
            System.out.print(personalGoal.getPGoal().get(i).getLine()+" ");
            System.out.println(personalGoal.getPGoal().get(i).getCol()+" ");

        }
    }
}