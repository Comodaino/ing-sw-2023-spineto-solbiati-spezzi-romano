package Model.CommonGoals;

import Model.Player;

/**
 * GoalDiagonal is a class which extends the class CommonGoal.
 * It represents the "diagonal" common goal card: five tiles of the same type forming a diagonal.
 * @author Nicolò
 */
public class GoalDiagonal extends CommonGoal{
    public GoalDiagonal(int numOfPlayer){
        super(numOfPlayer);
    }
    private static final String name ="GoalDiagonal";
    @Override
    public int getScore(Player p){
        boolean found = false;

        if(p.getShelf().getTile(0,0) != null && p.getShelf().getTile(0, 0).getColor().equals(p.getShelf().getTile(1, 1).getColor()) &&
                p.getShelf().getTile(1, 1).getColor().equals(p.getShelf().getTile(2, 2).getColor()) &&
                p.getShelf().getTile(2, 2).getColor().equals(p.getShelf().getTile(3, 3).getColor()) &&
                p.getShelf().getTile(3, 3).getColor().equals(p.getShelf().getTile(4, 4).getColor())){
            found = true;
        } else if(p.getShelf().getTile(1,0) != null && p.getShelf().getTile(1, 0).getColor().equals(p.getShelf().getTile(2, 1).getColor()) &&
                p.getShelf().getTile(2, 1).getColor().equals(p.getShelf().getTile(3, 2).getColor()) &&
                p.getShelf().getTile(3, 2).getColor().equals(p.getShelf().getTile(4, 3).getColor()) &&
                p.getShelf().getTile(4, 3).getColor().equals(p.getShelf().getTile(5, 4).getColor())){
            found = true;
        } else if(p.getShelf().getTile(0,4) != null && p.getShelf().getTile(0, 4).getColor().equals(p.getShelf().getTile(1, 3).getColor()) &&
                p.getShelf().getTile(1, 3).getColor().equals(p.getShelf().getTile(2, 2).getColor()) &&
                p.getShelf().getTile(2, 2).getColor().equals(p.getShelf().getTile(3, 1).getColor()) &&
                p.getShelf().getTile(3, 1).getColor().equals(p.getShelf().getTile(4, 0).getColor())){
            found = true;
        } else if(p.getShelf().getTile(1,4) != null && p.getShelf().getTile(1, 4).getColor().equals(p.getShelf().getTile(2, 3).getColor()) &&
                p.getShelf().getTile(2, 3).getColor().equals(p.getShelf().getTile(3, 2).getColor()) &&
                p.getShelf().getTile(3, 2).getColor().equals(p.getShelf().getTile(4, 1).getColor()) &&
                p.getShelf().getTile(4, 1).getColor().equals(p.getShelf().getTile(5, 0).getColor())){
            found = true;
        }

        if(found && !this.completed.contains(p)){
            return assignScore(p);
        }
        return 0;
    }
    @Override
    public String getName() {
        return name;
    }
}
