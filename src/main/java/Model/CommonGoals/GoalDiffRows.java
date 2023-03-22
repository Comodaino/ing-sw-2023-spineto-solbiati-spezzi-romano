package Model.CommonGoals;

import Model.Player;

public class GoalDiffRows extends CommonGoal{
    public GoalDiffRows(){
        super();
    }
    @Override
    public int getScore(Player p){
        int numOfRows = 0;

        for(int r=0; r<6 && numOfRows<2; r++){
            for(int c=0; c<4; c++){
                if(p.getShelf().getTile(r, c) != null && p.getShelf().getTile(r, c+1) != null &&
                        p.getShelf().getTile(r, c).getColor().equals(p.getShelf().getTile(r, c+1).getColor())){
                    break;
                } else if(c==3 && p.getShelf().getTile(r, c) != null && p.getShelf().getTile(r, c+1) != null){
                    numOfRows++;
                }
            }
        }

        if(numOfRows==2 && !this.completed.contains(p)){
            return assignScore(p);
        }
        return 0;
    }
}
