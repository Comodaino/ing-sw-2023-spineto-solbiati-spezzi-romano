package Model.CommonGoals;

import Model.Goal;
import Model.Player;
import java.util.ArrayList;
import java.util.List;

public abstract class CommonGoal extends Goal {
    protected List<Player> completed;
    protected int numOfPlayer;
    public CommonGoal(int numOfPlayer) {
        this.completed = new ArrayList<Player>();
        this.numOfPlayer = numOfPlayer;
    }
    public abstract int getScore(Player p);
    public int assignScore(Player p){
        if(this.numOfPlayer == 2){
            switch (this.completed.size()) {
                case 0:
                    this.completed.add(p);
                    return 8;
                case 1:
                    this.completed.add(p);
                    return 4;
            }
        } else {
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
        }
        return 0;
    }
}
