package Model.CommonGoals;

import Model.Shelf;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class GoalCouples extends CommonGoal{
    public GoalCouples GoalCouples(Shelf s, int numberOfPlayer){
        this.shelf = s;
        this.numberOfPlayer = numberOfPlayer;
        this.numberOfCompleted = 0;
        return this;
    }
    public int getScore(){
        throw new NotImplementedException();
    }
}
