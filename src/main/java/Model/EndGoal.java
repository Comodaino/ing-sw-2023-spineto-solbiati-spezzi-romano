package Model;
public class EndGoal {
    private boolean status;
    public final int value=1;

    public EndGoal() {
        this.status = true;
    }
    public boolean isCompleted(Player p){
        for(int r=0; r<6; r++){
            for(int c=0; c<5; c++){
                if(p.getShelf().getTile(r, c)==null) return false;
            }
        }
        return true;
    }
    public boolean getStatus(){
        return this.status;
    }

}
