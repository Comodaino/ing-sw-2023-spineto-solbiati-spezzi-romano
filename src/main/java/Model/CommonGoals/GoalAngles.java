package Model.CommonGoals;

import Model.Player;

public class GoalAngles extends CommonGoal{
    public GoalAngles(int numOfPlayer){
        super(numOfPlayer);
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
