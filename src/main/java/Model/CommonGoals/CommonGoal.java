package Model.CommonGoals;

import Model.Goal;
import Model.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * CommonGoal is an abstract class which extends the class Goal.
 * It represents the 12 different common goal cards; for each game there are one or two CommonGoal cards.
 * @author Nicolò
 */
public abstract class CommonGoal extends Goal implements Serializable {
    protected List<Player> completed;
    protected int numOfPlayer;
    protected String name;
    public CommonGoal(int numOfPlayer) {
        this.completed = new ArrayList<Player>();
        this.numOfPlayer = numOfPlayer;
    }

    /**
     * Check if the player has completed the common goal and returns the score he got.
     * If it is not completed, it returns 0. This method will be overrided in the subclasses.
     * @param p player whose shelf will be checked
     * @return the score
     * @author Nicolò
     */
    public abstract int getScore(Player p);

    /**
     * Assigns the score to the player depending on how many players are playing the game and
     * on how many players had already completed the goal.
     * @param p player
     * @return the score
     * @author Nicolò
     */
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

    public String getName() { return this.name; }

    public List<Player> getCompleted() {
        return this.completed;
    }

}
