package Model.CommonGoals;

import Model.Color;
import Model.Player;
import Model.Shelf;

public class GoalEight extends CommonGoal{
    private int count;
    public GoalEight(){
        super();
        this.count = 0;
    }
    @Override
    public int getScore(Shelf s, Player p){
        for(Color color : Color.values()){
            for(int r=0; r<6 && count<8; r++){
                for(int c=0; c<5; c++){
                    if(s.getTile(r, c).getColor().equals(color)){
                        count++;
                    }
                }
            }

            if(count==8){
                return assignScore(p);
            } else {
                count = 0;
            }
        }
        return 0;
    }
}
