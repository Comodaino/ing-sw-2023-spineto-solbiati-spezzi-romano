package Model.CommonGoals;

import Model.Color;
import Model.Player;

public class GoalEight extends CommonGoal{
    public GoalEight(int numOfPlayer){
        super(numOfPlayer);
    }
    @Override
    public int getScore(Player p){
        int count = 0;

        for(Color color : Color.values()){
            for(int r=0; r<6; r++){
                for(int c=0; c<5; c++){
                    if(p.getShelf().getTile(r, c)!=null && p.getShelf().getTile(r, c).getColor().equals(color)){
                        count++;
                    }
                }
            }

            if(count==8 && !this.completed.contains(p)){
                return assignScore(p);
            } else {
                count = 0;
            }
        }
        return 0;
    }
}
