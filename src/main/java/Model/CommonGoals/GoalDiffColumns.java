package Model.CommonGoals;

import Model.Player;
import Model.Shelf;

public class GoalDiffColumns extends CommonGoal{
    public GoalDiffColumns(){
        super();
    }
    @Override
    public int getScore(Player p) {
        int numOfCol = 0;

        for(int c=0; c<5 && numOfCol<2; c++){
            for(int r=0; r<5; r++){
                if(p.getShelf().getTile(r, c) != null && p.getShelf().getTile(r+1, c) != null &&
                p.getShelf().getTile(r, c).getColor().equals(p.getShelf().getTile(r+1, c).getColor())){
                    break;
                } else if(r==4 && p.getShelf().getTile(r, c)!= null && p.getShelf().getTile(r+1, c)!= null){
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
