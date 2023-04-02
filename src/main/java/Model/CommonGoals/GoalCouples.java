package Model.CommonGoals;

import Model.Player;
import java.util.Arrays;

public class GoalCouples extends CommonGoal{
    public GoalCouples (int numOfPlayer){ super(numOfPlayer); }
    @Override
    public int getScore(Player p){
        int numOfCouples = 0;
        boolean[][] foundMatrix = new boolean[6][5];
        for(int i=0; i<6; i++){
            for(int j=0; j<5; j++){
                foundMatrix[i][j] = false;
            }
        }

        for(int r=0; r<6 && numOfCouples<6; r++){
            for(int c=0; c<4 && numOfCouples<6; c++){
                if(p.getShelf().getTile(r, c)!=null && p.getShelf().getTile(r, c+1)!=null &&
                        p.getShelf().getTile(r, c).getColor().equals(p.getShelf().getTile(r, c+1).getColor()) &&
                        !foundMatrix[r][c] && !foundMatrix[r][c+1]){
                    numOfCouples++;
                    foundMatrix[r][c] = true;
                    foundMatrix[r][c+1] = true;
                }
            }
        } //search "horizontal couples"

        for(int c=0; c<5 && numOfCouples<6; c++){
            for(int r=0; r<5 && numOfCouples<6; r++){
                if(p.getShelf().getTile(r, c)!=null && p.getShelf().getTile(r+1, c)!=null &&
                        p.getShelf().getTile(r, c).getColor().equals(p.getShelf().getTile(r+1, c).getColor()) &&
                        !foundMatrix[r][c] && !foundMatrix[r+1][c]){
                    numOfCouples++;
                    foundMatrix[r][c] = true;
                    foundMatrix[r+1][c] = true;
                }
            }
        } //search "vertical couples"

        if(numOfCouples==6 && !this.completed.contains(p)){
            return assignScore(p);
        }

        return 0;
    }
}