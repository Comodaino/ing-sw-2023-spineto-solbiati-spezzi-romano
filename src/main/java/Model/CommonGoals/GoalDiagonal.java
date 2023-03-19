package Model.CommonGoals;

import Model.Player;
import Model.Shelf;

public class GoalDiagonal extends CommonGoal{
    public GoalDiagonal(){
        super();
    }
    @Override
    public int getScore(Shelf s, Player p){
        boolean found = false;

        if(s.getTile(0, 0).getColor().equals(s.getTile(1, 1).getColor()) &&
                s.getTile(1, 1).getColor().equals(s.getTile(2, 2).getColor()) &&
                s.getTile(2, 2).getColor().equals(s.getTile(3, 3).getColor()) &&
                s.getTile(3, 3).getColor().equals(s.getTile(4, 4).getColor()) &&
                s.getTile(0,0) != null){
            found = true;
        } else if(s.getTile(1, 0).getColor().equals(s.getTile(2, 1).getColor()) &&
                s.getTile(2, 1).getColor().equals(s.getTile(3, 2).getColor()) &&
                s.getTile(3, 2).getColor().equals(s.getTile(4, 3).getColor()) &&
                s.getTile(4, 3).getColor().equals(s.getTile(5, 4).getColor()) &&
                s.getTile(1,0) != null){
            found = true;
        } else if(s.getTile(0, 4).getColor().equals(s.getTile(1, 3).getColor()) &&
                s.getTile(1, 3).getColor().equals(s.getTile(2, 2).getColor()) &&
                s.getTile(2, 2).getColor().equals(s.getTile(3, 1).getColor()) &&
                s.getTile(3, 1).getColor().equals(s.getTile(4, 0).getColor()) &&
                s.getTile(0,4) != null){
            found = true;
        } else if(s.getTile(1, 4).getColor().equals(s.getTile(2, 3).getColor()) &&
                s.getTile(2, 3).getColor().equals(s.getTile(3, 2).getColor()) &&
                s.getTile(3, 2).getColor().equals(s.getTile(4, 1).getColor()) &&
                s.getTile(4, 1).getColor().equals(s.getTile(5, 0).getColor()) &&
                s.getTile(1,4) != null){
            found = true;
        }

        if(found && !this.completed.contains(p)){
            return assignScore(p);
        }
        return 0;
    }
}
