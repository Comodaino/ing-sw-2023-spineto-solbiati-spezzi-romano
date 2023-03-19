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
        return this.nickname;
    }

    public boolean getChair(){
        return this.chair;
    }
    public Shelf getShelf(){ return this.myShelf;}
    public PersonalGoal getGoal(){ return this.myGoal;}
}
