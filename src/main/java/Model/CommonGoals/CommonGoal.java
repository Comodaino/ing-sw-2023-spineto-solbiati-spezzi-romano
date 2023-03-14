package Model.CommonGoals;

import Model.Board;
import Model.Shelf;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public abstract class CommonGoal {
    private int numberOfPlayer;
    private int numberOfCompleted = 0;
    private Shelf shelf;

    public CommonGoal CommonGoal(Shelf s, int numberOfPlayer){
        this.shelf = s;
        this.numberOfPlayer = numberOfPlayer;
        return this;
    }
    public int getScore(){
        throw new NotImplementedException();
    }
}
