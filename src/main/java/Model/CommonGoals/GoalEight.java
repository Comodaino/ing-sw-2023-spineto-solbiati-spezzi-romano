package Model.CommonGoals;

import Model.Color;
import Model.Player;

public class GoalEight extends CommonGoal{
    private int count;
    public GoalEight(){
        super();
        this.count = 0;
    }
    @Override
    public int getScore(Player p){
        for(Color color : Color.values()){
            for(int r=0; r<6 && count<8; r++){
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
