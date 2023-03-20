package Model.CommonGoals;

import Model.Player;
import Model.Shelf;

public class GoalAngles extends CommonGoal{
    public GoalAngles(){
        super();
    }
    @Override
    public int getScore(Player p){
        if(!this.completed.contains(p) && p.getShelf().getTile(0, 0) != null &&
                p.getShelf().getTile(0, 0).getColor().equals(p.getShelf().getTile(0, 4).getColor()) &&
                p.getShelf().getTile(5, 0).getColor().equals(p.getShelf().getTile(5, 4).getColor()) &&
                p.getShelf().getTile(0, 0).getColor().equals(p.getShelf().getTile(5, 0).getColor())) {

            return assignScore(p);
        }
        return 0;
    }
}
