package Model.CommonGoals;

import Model.Board;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public abstract class CommonGoal {
    private int numberOfPlayer;
    private int numberOfCompleted = 0;
    private Board board;

    public CommonGoal CommonGoal(Board b, int numberOfPlayer){
        this.board = b;
        this.numberOfPlayer = numberOfPlayer;
        return this;
    }
    public int getScore(){
        throw new NotImplementedException();
    }
}
