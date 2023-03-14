package Model.CommonGoals;

import Model.Shelf;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class GoalSquares extends CommonGoal{
    public GoalSquares GoalSquares(Shelf s, int numberOfPlayer){
        this.shelf = s;
        this.numberOfPlayer = numberOfPlayer;
        this.numberOfCompleted = 0;
        return this;
    }
    public int getScore(){ throw new NotImplementedException(); }
}
