package Model.CommonGoals;

import Model.Player;

/**
 * GoalDiffColumns_conf is a class which extends the class CommonGoal.
 * It represents the "different columns" common goal card: two columns each formed by 6 different types of tiles.
 * @author Nicolò
 */
public class GoalDiffColumns extends CommonGoal{
    public GoalDiffColumns(int numOfPlayer){
        super(numOfPlayer);
    }
    private static final String name ="GoalDiffColumns_conf";
    @Override
    public int getScore(Player p) {
        int numOfCol = 0;

        for(int c=0; c<5; c++){
            for(int r=0; r<5; r++){
                if(p.getShelf().getTile(r, c) != null && p.getShelf().getTile(r+1, c) != null &&
                p.getShelf().getTile(r, c).getColor().equals(p.getShelf().getTile(r+1, c).getColor())){
                    break;
                } else if(r==4 && p.getShelf().getTile(r, c)!= null && p.getShelf().getTile(r+1, c)!= null){
                    numOfCol++;
                }
            }
        }

        if(numOfCol==2 && !this.completed.contains(p)){
            return assignScore(p);
        }
        return 0;
    }
    @Override
    public String getName() {
        return name;
    }
}
