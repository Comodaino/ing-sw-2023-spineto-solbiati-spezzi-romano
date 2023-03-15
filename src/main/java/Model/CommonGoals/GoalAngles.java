package Model.CommonGoals;

import Model.Player;
import Model.Shelf;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class GoalAngles extends CommonGoal{
    public GoalAngles(){
        super();
    }
    @Override
    public int getScore(Shelf s, Player p){
        if(!this.completed.contains(p) && s.getTile(0, 0) != null &&
                s.getTile(0, 0).getColor().equals(s.getTile(0, 5).getColor()) &&
                s.getTile(5, 0).getColor().equals(s.getTile(5, 4).getColor()) &&
                s.getTile(0, 0).getColor().equals(s.getTile(5, 0).getColor())) {

            return assignScore(p);
        }
        return 0;
    }
}
