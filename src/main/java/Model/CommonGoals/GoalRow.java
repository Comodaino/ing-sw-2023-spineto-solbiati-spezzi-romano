package Model.CommonGoals;

import Model.Color;
import Model.Player;
import Model.Shelf;

import java.util.ArrayList;
import java.util.List;

public class GoalRow extends CommonGoal{
    public GoalRow(){
        super();
    }
    @Override
    public int getScore(Shelf s, Player p){
        List<Color> buffer = new ArrayList<Color>();
        int n = 0;
        int numOfRows = 0;

        for(int r=0; r<6 && numOfRows<4; r++){
            for(int c=0; c<5; c++){
                if(s.getTile(r, c) != null){
                    n++;
                    if(!buffer.contains(s.getTile(r, c).getColor())){
                        buffer.add(s.getTile(r, c).getColor());
                    }
                }
            }
            if(n==5 && buffer.size()<=3){
                numOfRows++;
            }
            buffer.clear();
            n=0;
        }

        if(numOfRows==4 && !this.completed.contains(p)){
            return assignScore(p);
        }
        return 0;
    }
}
