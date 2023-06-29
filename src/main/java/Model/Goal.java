package Model;


import java.io.Serializable;

/**
 * Abstract class for the goals, contains method to get the score of a player
 */
public abstract class Goal implements Serializable {
    public abstract int getScore(Player p);
}
