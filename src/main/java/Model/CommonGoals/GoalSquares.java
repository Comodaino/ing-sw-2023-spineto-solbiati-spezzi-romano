package Model.CommonGoals;

import Model.Player;
import Model.Shelf;

import java.util.Arrays;

public class GoalSquares extends CommonGoal{
    public GoalSquares(){
        super();
    }
    @Override
    public int getScore(Shelf s, Player p) {
        int counter = 0;
        boolean[][] foundMatrix = new boolean[6][5];
        Arrays.fill(foundMatrix, false);
        for(int i=0; i<5; i++){
            for(int j=0; j<4; j++){
                if(((!foundMatrix[i][j] && !foundMatrix[i+1][j+1])&&(!foundMatrix[i+1][j] && !foundMatrix[i][j+1]))){
                    if(  ((s.getTile(i,j).getColor().equals(s.getTile(i+1,j+1).getColor())) && (s.getTile(i+1,j).getColor().equals(s.getTile(i,j+1).getColor()))) &&  s.getTile(i,j).getColor().equals(s.getTile(i,j+1).getColor()) ) {
                        counter++;
                        foundMatrix[i][j] = true;
                        foundMatrix[i + 1][j] = true;
                        foundMatrix[i + 1][j + 1] = true;
                        foundMatrix[i][j + 1] = true;
                    }
                }
            }
        }
        if(counter>=4) return assignScore(p);
        return 0;
    }
}
