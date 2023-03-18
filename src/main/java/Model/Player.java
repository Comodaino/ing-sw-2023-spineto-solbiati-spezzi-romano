package Model;


import jdk.jshell.spi.ExecutionControl;

public class Player {
    private String nickname;
    private boolean chair;
    private Shelf myShelf;
    private PersonalGoal myGoal;

    public Player Player(String n, boolean c){
        this.chair = c;
        this.nickname = n;
        this.myShelf = new Shelf();
        this.myGoal = new PersonalGoal();
        throw new ExecutionControl.NotImplementedException(); //to be reviewed
    }

    public String getNickname(){
        return nickname;
        throw new ExecutionControl.NotImplementedException(); //to be reviewed
    }

    public boolean getChair(){
        return chair;
        throw new ExecutionControl.NotImplementedException(); //to be reviewed
    }
}
