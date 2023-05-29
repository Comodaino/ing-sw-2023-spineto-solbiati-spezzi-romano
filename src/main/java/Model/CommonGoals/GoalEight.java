package Model.CommonGoals;

import Model.Color;
import Model.Player;

/**
 * GoalEight is a class which extends the class CommonGoal.
 * It represents the eight tiles common goal card: eight tiles of the same type;
 * there’s no restriction about the position of these tiles.
 * @author Nicolò
 */
public class GoalEight extends CommonGoal{
    public GoalEight(int numOfPlayer){
        super(numOfPlayer);
    }
    private static final String name ="GoalEight";
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
    @Override
    public String getName() {
        return name;
    }
}
