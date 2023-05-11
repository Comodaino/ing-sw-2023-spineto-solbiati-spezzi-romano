package Model;


import java.io.Serializable;

public abstract class Goal implements Serializable {
    public abstract int getScore(Player p);
}
