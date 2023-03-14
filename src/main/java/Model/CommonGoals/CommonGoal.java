package Model.CommonGoals;

import Model.Board;
import Model.Shelf;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public abstract class CommonGoal {
    protected int numberOfPlayer;
    protected int numberOfCompleted;
    protected Shelf shelf;

    public CommonGoal CommonGoal(Shelf s, int numberOfPlayer){
        this.shelf = s;
        this.numberOfPlayer = numberOfPlayer;
        this.numberOfCompleted = 0;
        return this;
    }
    public int getScore(){
        throw new NotImplementedException();
    }
}
