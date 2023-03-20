package Model.CommonGoals;

import Model.Player;
import Model.Shelf;

import java.util.Arrays;

public class GoalQuartets extends CommonGoal{
    public GoalQuartets(){
        super();
    }
    @Override
    public int getScore(Player p){
        int numOfQuartets = 0, n = 0;
        boolean[][] foundMatrix = new boolean[6][5];
        Arrays.fill(foundMatrix, false);

        for(int r=0; r<6 && numOfQuartets<4; r++){
            n = 0;
            for(int c=0; c<4; c++){
                if(p.getShelf().getTile(r, c)!=null && p.getShelf().getTile(r, c).getColor().equals(p.getShelf().getTile(r, c+1).getColor()) &&
                !foundMatrix[r][c] && !foundMatrix[r][c+1]){
                    n++;
                } else {
                    n = 0;
                }
                if(n==3){
                    numOfQuartets++;
                    for(int i=c+1; i>c-4; i--){
                        foundMatrix[r][i] = true;
                    }
                    break;
                }
            }
        } //search "horizontal quartets"

        for(int c=0; c<5 && numOfQuartets<4; c++){
            n = 0;
            for(int r=0; r<5; r++){
                if(p.getShelf().getTile(r, c)!=null && p.getShelf().getTile(r, c).getColor().equals(p.getShelf().getTile(r+1, c).getColor()) &&
                !foundMatrix[r][c] && !foundMatrix[r+1][c]){
                    n++;
                } else {
                    n = 0;
                }
                if(n==3){
                    numOfQuartets++;
                    for(int i=r+1; i>r-4; i--){
                        foundMatrix[i][c] = true;
                    }
                    break;
                }
            }
        } //search "vertical quartets"

        if(numOfQuartets==4 && !this.completed.contains(p)){
            return assignScore(p);
        }
        return 0;
    }
}
