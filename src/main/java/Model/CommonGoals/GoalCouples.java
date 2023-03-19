package Model.CommonGoals;

import Model.Player;
import Model.Shelf;
import java.util.Arrays;

public class GoalCouples extends CommonGoal{
    public GoalCouples (){ super(); }
    @Override
    public int getScore(Shelf s, Player p){
        int counter = 0;
        boolean[][] foundMatrix = new boolean[6][5];
        Arrays.fill(foundMatrix, false);

        for(int i=0; i<6; i++) {
            for(int j = 0; j < 5; j++) {
                if(  !((i-1<0 || j-1<0) || (i+1>=6 || j+1>=5))) {
                    if (!foundMatrix[i][j]) {
                        if (!foundMatrix[i + 1][j] && s.getTile(i,j).getColor().equals(s.getTile(i+1,j).getColor())) {
                            counter++;
                            foundMatrix[i][j] = true;
                            foundMatrix[i + 1][j] = true;
                        }
                        if (!foundMatrix[i - 1][j] && s.getTile(i,j).getColor().equals(s.getTile(i-1,j).getColor())) {
                            counter++;
                            foundMatrix[i][j] = true;
                            foundMatrix[i - 1][j] = true;
                        }
                        if (!foundMatrix[i][j + 1] && s.getTile(i,j).getColor().equals(s.getTile(i,j+1).getColor())) {
                            counter++;
                            foundMatrix[i][j] = true;
                            foundMatrix[i][j + 1] = true;
                        }
                        if (!foundMatrix[i][j - 1] && s.getTile(i,j).getColor().equals(s.getTile(i,j-1).getColor())) {
                            counter++;
                            foundMatrix[i][j] = true;
                            foundMatrix[i][j - 1] = true;
                        }
                    }
                }
            }
        }
        if(counter>=6 && !this.completed.contains(p)) return assignScore(p);
        return 0;
    }
}
