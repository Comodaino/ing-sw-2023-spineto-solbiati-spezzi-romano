package Model.CommonGoals;

import Model.Shelf;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class GoalColumn extends CommonGoal{
    public GoalColumn GoalColumn(Shelf s, int numberOfPlayer){
        this.shelf = s;
        this.numberOfPlayer = numberOfPlayer;
        this.numberOfCompleted = 0;
        return this;
    }
    public int getScore(){
        throw new NotImplementedException();
    }
}
