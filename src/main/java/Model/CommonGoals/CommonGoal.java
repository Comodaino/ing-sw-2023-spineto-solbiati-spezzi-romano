package Model.CommonGoals;

import Model.Player;
import Model.Shelf;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import java.util.ArrayList;
import java.util.List;

public abstract class CommonGoal {
    protected List<Player> completed;
    public CommonGoal() {
        this.completed = new ArrayList<Player>();
    }
    public int getScore(Shelf s, Player p){ throw new NotImplementedException();}
    public int assignScore(Player p){
        switch(this.completed.size()){
            case 0:
                this.completed.add(p);
                return 8;
            case 1:
                this.completed.add(p);
                return 6;
            case 2:
                this.completed.add(p);
                return 4;
            case 3:
                this.completed.add(p);
                return 2;
        }
        throw new NotImplementedException(); //throw a new NotAssignableScoreException
    }
}
