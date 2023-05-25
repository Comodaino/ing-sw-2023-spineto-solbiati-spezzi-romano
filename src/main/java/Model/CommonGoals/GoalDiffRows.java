package Model.CommonGoals;

import Model.Player;

/**
 * GoalDiffRows is a class which extends the class CommonGoal.
 * It represents the "different rows" common goal card: two lines each formed by 5 different types of tiles;
 * one line can show the same or a different combination of the other line.
 * @author Nicolò
 */
public class GoalDiffRows extends CommonGoal{
    public GoalDiffRows(int numOfPlayer){
        super(numOfPlayer);
    }
    private static final String name ="GoalDiffRows";
    @Override
    public int getScore(Player p){
        int numOfRows = 0;

        for(int r=0; r<6; r++){
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
    @Override
    public String getName() {
        return name;
    }
}
