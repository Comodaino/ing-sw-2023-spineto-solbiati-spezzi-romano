package Model.CommonGoals;

import Model.Player;
import Model.Shelf;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class GoalCouples extends CommonGoal{
    public GoalCouples (){
        super();
    }
    @Override
    public int getScore(Shelf s, Player p){
        throw new NotImplementedException();
    }
}
