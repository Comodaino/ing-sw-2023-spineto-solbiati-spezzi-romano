package Model.CommonGoals;

import Model.Player;
import Model.Shelf;

public class GoalDiffRows extends CommonGoal{
    private int numOfRows;
    public GoalDiffRows(){
        super();
        this.numOfRows = 0;
    }
    @Override
    public int getScore(Shelf s, Player p){
        for(int r=0; r<6 && numOfRows<2; r++){
            for(int c=0; c<4; c++){
                if(s.getTile(r, c) != null && s.getTile(r, c+1) != null &&
                        s.getTile(r, c).getColor().equals(s.getTile(r, c+1).getColor())){
                    break;
                } else if(c==3 && s.getTile(r, c) != null && s.getTile(r, c+1) != null){
                    numOfRows++;
                }
            }
        }

        if(numOfRows==2 && !this.completed.contains(p)){
            return assignScore(p);
        }
        return 0;
    }
}
