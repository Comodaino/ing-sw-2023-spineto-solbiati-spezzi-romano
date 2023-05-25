package Model.CommonGoals;

import Model.Color;
import Model.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * GoalRow is a class which extends the class CommonGoal.
 * It represents the "row" common goal card: four lines each formed by 5 tiles of maximum three different types;
 * one line can show the same or a different combination of another line.
 * @author Nicolò
 */
public class GoalRow extends CommonGoal{
    public GoalRow(int numOfPlayer){
        super(numOfPlayer);
    }
    private static final String name ="GoalRows";
    @Override
    public int getScore(Player p){
        List<Color> buffer = new ArrayList<Color>();
        int n = 0;
        int numOfRows = 0;

        for(int r=0; r<6; r++){
            for(int c=0; c<5; c++){
                if(p.getShelf().getTile(r, c) != null){
                    n++;
                    if(!buffer.contains(p.getShelf().getTile(r, c).getColor())){
                        buffer.add(p.getShelf().getTile(r, c).getColor());
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

    @Override
    public String getName() {
        return name;
    }
}
