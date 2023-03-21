package Model.CommonGoals;

import Model.Color;
import Model.Player;

import java.util.ArrayList;
import java.util.List;

public class GoalColumn extends CommonGoal{
    public GoalColumn(){
        super();
    }
    @Override
    public int getScore(Player p){
        List<Color> buffer = new ArrayList<Color>();
        int n = 0;
        int numOfCol = 0;

        for(int c=0; c<5 && numOfCol<3; c++){
            for(int r=0; r<6; r++){
                if(p.getShelf().getTile(r, c) != null){
                    n++;
                    if(!buffer.contains(p.getShelf().getTile(r, c).getColor())){
                        buffer.add(p.getShelf().getTile(r, c).getColor());
                    }
                }
            }
            if(n==6 && buffer.size()<=3){
                numOfCol++;
            }
            buffer.clear();
            n=0;
        }

        if(numOfCol==3 && !this.completed.contains(p)){
            return assignScore(p);
        }
        return 0;
    }
}
