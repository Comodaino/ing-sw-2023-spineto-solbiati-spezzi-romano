package Model.CommonGoals;

import Model.Player;
import Model.Shelf;

public class GoalDiagonal extends CommonGoal{
    public GoalDiagonal(){
        super();
    }
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
}
