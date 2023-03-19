package Model.CommonGoals;

import Model.Player;
import Model.Shelf;

public class GoalCross extends CommonGoal{
    public GoalCross(){
        super();
    }
    @Override
    public int getScore(Shelf s, Player p){
        boolean found = false;

        for(int r=1; r<5 && !found; r++){
            for(int c=1; c<4 && !found; c++){
                if(s.getTile(r, c) != null){
                    if(s.getTile(r, c).getColor().equals(s.getTile(r-1, c-1).getColor()) &&
                    s.getTile(r, c).getColor().equals(s.getTile(r-1, c+1).getColor()) &&
                    s.getTile(r, c).getColor().equals(s.getTile(r+1, c-1).getColor()) &&
                    s.getTile(r, c).getColor().equals(s.getTile(r+1, c+1).getColor())){
                        found = true;
                    }
                }
            }
        }

        if(found && !this.completed.contains(p)){
            return assignScore(p);
        }
        return 0;
    }
}
