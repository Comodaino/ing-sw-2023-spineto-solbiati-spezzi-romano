package Model;



public class Player {
    private String nickname;
    private boolean chair;
    private Shelf myShelf;
    private PersonalGoal myGoal;
    private int score;

    public Player(String n, boolean c){
        this.score=0;
        this.chair = c;
        this.nickname = n;
        this.myShelf = new Shelf();
        this.myGoal = new PersonalGoal(this.myShelf);
    }

    public String getNickname(){
        return this.nickname;
    }

    public boolean getChair(){
        return this.chair;
    }
    public Shelf getShelf(){ return this.myShelf;}
    public PersonalGoal getGoal(){ return this.myGoal;}

    public void addScore(int n) {
        this.score+=n;
    }

    public int getScore() {
        return score;
    }

    public void setShelf(Tile[][] m) {
        this.myShelf.setMatrix(m);
    }
}
