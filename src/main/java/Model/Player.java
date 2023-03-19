package Model;



public class Player {
    private String nickname;
    private boolean chair;
    private Shelf myShelf;
    private PersonalGoal myGoal;

    public Player(String n, boolean c){
        this.chair = c;
        this.nickname = n;
        this.myShelf = new Shelf();
        this.myGoal = new PersonalGoal();
    }

    public String getNickname(){
        return nickname;
    }

    public boolean getChair(){
        return chair;
    }
}
