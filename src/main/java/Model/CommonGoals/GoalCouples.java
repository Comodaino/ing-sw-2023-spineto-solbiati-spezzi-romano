package Model.CommonGoals;

import Model.Player;
import Model.Shelf;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Arrays;

public class GoalCouples extends CommonGoal{
    public GoalCouples (){
        super();
    }
    @Override
    public int getScore(Shelf s, Player p){
        int counter = 0;
        boolean[][] foundMatrix = new boolean[6][5];
        Arrays.fill(foundMatrix, false);
        for(int i=0; i<6; i++) {
            for(int j = 0; j < 5; j++) {
                if(!foundMatrix[i][j]){
                    if(!foundMatrix[i+1][j]){
                        counter++;
                        foundMatrix[i][j]=true;
                        foundMatrix[i+1][j]=true;
                    }
                    if(!foundMatrix[i-1][j]){
                        counter++;
                        foundMatrix[i][j]=true;
                        foundMatrix[i-1][j]=true;
                    }
                    if(!foundMatrix[i][j+1]){
                        counter++;
                        foundMatrix[i][j]=true;
                        foundMatrix[i][j+1]=true;
                    }
                    if(!foundMatrix[i][j-1]){
                        counter++;
                        foundMatrix[i][j]=true;
                        foundMatrix[i][j-1]=true;
                    }
                }
            }
        }
        if(counter>=6) return assignScore(p);
        return 0;
    }
}
