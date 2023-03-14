package Model.CommonGoals;

import Model.Shelf;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class GoalDiffColumns extends CommonGoal{
    int numOfCol;
    public GoalDiffColumns GoalDiffColumns(Shelf s, int numberOfPlayer){
        this.shelf = s;
        this.numberOfPlayer = numberOfPlayer;
        this.numberOfCompleted = 0;
        this.numOfCol = 0;
        return this;
    }
    public int getScore(){
        for(int c=0; c<4 && numOfCol<2; c++){
            for(int r=0; r<5; r++){
                if(shelf.getTile(r, c).equals(shelf.getTile(r+1, c))){
                    c++;
                } else if(r==4){
                    numOfCol++;
                }
            }
        }

        if(numOfCol==2){
            switch(numberOfCompleted){ //the same player cannot complete the same goals 2 times!!
                case 0:
                    numberOfCompleted++;
                    return 8;
                case 1:
                    numberOfCompleted++;
                    return 6;
                case 2:
                    numberOfCompleted++;
                    return 4;
                case 3:
                    numberOfCompleted++;
                    return 2;
            }
        } else {
            throw new NotCompletedException(); //implement the exception!!
        }
        throw new NotImplementedException();
    }
}
