package Model;

import Model.CommonGoals.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;


public class Board {
    private Cell[][] matrix;
    private List<Player> listOfPlayer;
    private boolean firstMatch;
    private Set<CommonGoal> setOfCommonGoal;
    private boolean firstToEnd;
    private List<Tile> tileBuffer;


    public Board Board(int np, boolean fm, List<Player> pl){

        matrix = new Cell[9][9];

        try {
            File boardConf = new File("board.conf");
            Scanner reader = new Scanner(boardConf);

            for(int i = 0; i<9 && reader.hasNextLine(); i++) {
                String data = reader.nextLine();
                for (int j = 0; j<9; j++){
                    this.matrix[j][i] = new Cell((int)data[j]);
                }
            }

            reader.close();
        } catch (FileNotFoundException e);

        this.listOfPlayer = pl;
        this.firstMatch = fm;
        this.firstToEnd = true;
        tileBuffer = new List<Tile>;

//GOALS ARE MISSING
        setOfCommonGoal = new Set<CommonGoal>;
        Random rand = new Random()
        if(fm = true){
            rand.nextInt(10);
            switch (rand.nextInt(10)){
                case 0: setOfCommonGoal.add(new GoalAngles);
                    break;
            }
        }else{
            rand.nextInt(10);
            switch (rand.nextInt(10)){
                case 0: setOfCommonGoal.add(new GoalAngles);
                    break;
            }
            rand.nextInt(10);
            switch (rand.nextInt(10)){
                case 0: setOfCommonGoal.add(new GoalAngles);
                    break;
            }
        }

        throw new NotImplementedException;
    }




    public void endMatch(){
        throw new NotImplementedException();
    }
    public void removeTile(Tile tile){
        throw new NotImplementedException();
    }
    public void checkRecharge(){
        throw new NotImplementedException();
    }
}
