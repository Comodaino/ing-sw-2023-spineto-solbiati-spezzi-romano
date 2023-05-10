package Model;

import Distributed.RemotePlayer;

/**
 * represent one Player with attribute nickname, chair, myShelf (Shelf type),
 * myGoal (PersonalGoal type), score and nearGoal.
 * @author Clara
 */

public class Player {
    private String nickname;
    private boolean chair;
    private Shelf myShelf;
    private PersonalGoal myGoal;
    private int score;
    private NearGoal nearGoal;
    private RemotePlayer remotePlayer;

    public Player(String n, boolean c, RemotePlayer remotePlayer){
        this.remotePlayer = remotePlayer;
        this.score=0;
        this.chair = c;
        this.nickname = n;
        this.myShelf = new Shelf();
        this.myGoal = new PersonalGoal(this.myShelf);
        this.nearGoal = new NearGoal();
    }

    public String getNickname(){
        return this.nickname;
    }

    public boolean getChair(){
        return this.chair;
    }
    public Shelf getShelf(){
        return this.myShelf;
    }
    public PersonalGoal getGoal(){
        return this.myGoal;
    }

    /**
     * addScore takes an integer n and add it to the previous score
     * @param n how much the score is incremented
     */
    public void addScore(int n) {
        this.score+=n;
    }

    public int getScore() {
        return this.score;
    }

    public void setShelf(Tile[][] m) {
        this.myShelf.setMatrix(m);
    }

    public NearGoal getNearGoal() {
        return this.nearGoal;
    }

    public RemotePlayer getRemotePlayer() {
        return remotePlayer;
    }
}
