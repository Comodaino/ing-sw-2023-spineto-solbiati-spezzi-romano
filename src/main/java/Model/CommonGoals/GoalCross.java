package Model.CommonGoals;

import Model.Player;

public class GoalCross extends CommonGoal{
    public GoalCross(){
        super();
    }
    @Override
    public int getScore(Player p){
        boolean found = false;

        for(int r=1; r<5 && !found; r++){
            for(int c=1; c<4 && !found; c++){
                if(p.getShelf().getTile(r, c)!=null && p.getShelf().getTile(r-1, c-1)!=null && p.getShelf().getTile(r-1, c+1)!=null &&
                p.getShelf().getTile(r+1, c-1)!=null && p.getShelf().getTile(r+1, c+1)!=null){
                    if(p.getShelf().getTile(r, c).getColor().equals(p.getShelf().getTile(r-1, c-1).getColor()) &&
                    p.getShelf().getTile(r, c).getColor().equals(p.getShelf().getTile(r-1, c+1).getColor()) &&
                    p.getShelf().getTile(r, c).getColor().equals(p.getShelf().getTile(r+1, c-1).getColor()) &&
                    p.getShelf().getTile(r, c).getColor().equals(p.getShelf().getTile(r+1, c+1).getColor())){
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
