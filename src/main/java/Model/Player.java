package Model;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class Player {
    private String nickname;
    private boolean chair;
    private Shelf myShelf;
    private PersonalGoal myGoal;

    public Player Player(String n, boolean c){
        this.chair = c;
        this.nickname = n;
        throw new NotImplementedException;
    }

    public String getNickname(){
        return nickname;
        throw new NotImplementedException;
    }

    public boolean getChair(){
        return chair;
        throw new NotImplementedException;
    }
}
