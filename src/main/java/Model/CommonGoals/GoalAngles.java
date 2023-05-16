package Model.CommonGoals;

import Model.Player;

/**
 * GoalAngles is a class which extends the class CommonGoal.
 * It represents the "angles" common goal card: four tiles of the same type in the four corners of the bookshelf.
 * @author Nicolò
 */
public class GoalAngles extends CommonGoal{
    public GoalAngles(int numOfPlayer){
        super(numOfPlayer);
    }
    private static final String name ="GoalAngles";
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
