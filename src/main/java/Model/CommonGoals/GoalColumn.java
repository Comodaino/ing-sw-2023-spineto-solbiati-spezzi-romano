package Model.CommonGoals;

import Model.Color;
import Model.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * GoalColumn is a class which extends the class CommonGoal.
 * It represents the "column" common goal card: three columns each formed by 6 tiles of maximum three different types;
 * one column can show the same or a different combination of another column.
 * @author Nicolò
 */
public class GoalColumn extends CommonGoal{
    public GoalColumn(int numOfPlayer){
        super(numOfPlayer);
    }
    @Override
    public int getScore(Player p){
        List<Color> buffer = new ArrayList<Color>();
        int n = 0;
        int numOfCol = 0;

        for(int c=0; c<5; c++){
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
