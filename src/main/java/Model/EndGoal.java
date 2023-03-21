package Model;
public class EndGoal {
    private boolean status;
    public final int value=1;

    public EndGoal() {
        this.status = true;
    }
    public int getScore(Player p){
        if(getStatus()==false) return 0;
        for(int r=0; r<6; r++){
            for(int c=0; c<5; c++){
                if(p.getShelf().getTile(r, c)==null) return 0;
            }
        }
        this.status = false;
        return 1;
    }
    public boolean getStatus(){
        return this.status;
    }

}
