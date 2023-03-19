package Model.CommonGoals;

import Model.Player;
import Model.Shelf;

public class GoalDiffColumns extends CommonGoal{
    public GoalDiffColumns(){
        super();
    }
    @Override
    public int getScore(Shelf s, Player p) {
        int numOfCol = 0;

        for(int c=0; c<5 && numOfCol<2; c++){
            for(int r=0; r<5; r++){
                if(s.getTile(r, c).getColor().equals(s.getTile(r+1, c).getColor()) &&
                        s.getTile(r, c) != null && s.getTile(r+1, c) != null){
                    break;
                } else if(r==4 && s.getTile(r, c)!= null && s.getTile(r+1, c)!= null){
                    numOfCol++;
                }
            }
        }

        if(numOfCol==2 && !this.completed.contains(p)){
            return assignScore(p);
        }
        return 0;
    }
}
