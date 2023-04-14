package Model.CommonGoals;

import Model.Player;

/**
 * GoalStair is a class which extends the class CommonGoal.
 * It represents the "stair" common goal card: five columns of increasing or decreasing height:
 * starting from the first column on the left or on the right, each next column must be made of exactly one more tile.
 * Tiles can be of any type.
 * @author Nicolò
 */
public class GoalStair extends CommonGoal{
    public GoalStair(int numOfPlayer){
        super(numOfPlayer);
    }
    @Override
    public int getScore(Player p){
        int n = 0;
        int m = 0;
        int numOfCol = 0;

        for(int r=0; r<6; r++){
            if(p.getShelf().getTile(r, 0) != null){
                n++;
            }
        }
        for(int c=1; c<5; c++){
            for(int r=0; r<6; r++){
                if(p.getShelf().getTile(r, c) != null){
                    m++;
                }
            }
            if(m == n-1){
                numOfCol++;
                n = m;
                m = 0;
            } else {
                break;
            }
        } //checks a "left stair" (i.e. a stair with the top at left)
        if(numOfCol==4 && !this.completed.contains(p)){
            return assignScore(p);
        }

        n = 0;
        m = 0;
        numOfCol = 0;
        for(int r=0; r<6; r++){
            if(p.getShelf().getTile(r, 0) != null){
                n++;
            }
        }
        for(int c=1; c<5; c++){
            for(int r=0; r<6; r++){
                if(p.getShelf().getTile(r, c) != null){
                    m++;
                }
            }
            if(m == n+1){
                numOfCol++;
                n = m;
                m = 0;
            } else {
                break;
            }
        } //checks a "right stair" (i.e. a stair with the top at right)
        if(numOfCol==4 && !this.completed.contains(p)){
            return assignScore(p);
        }
        return 0;
    }
}
