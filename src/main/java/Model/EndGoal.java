package Model;
/**
 * EndGoal verify if a player is the first to finish a game
 * and if this is true, a point is awarded to the player
 * @author alessandra
 */
public class EndGoal extends Goal{
    private boolean status;
    public final int value=1;

    /**
     * constructor of EndGoal
     */
    public EndGoal() {
        this.status = true;
    }

    /**
     * this method check it out if player is the first that complete his personal shelf
     * @param p, player
     * @return 1 if the player is the first that complete his perosnal shelf, 0 if not
     */
    public int getScore(Player p){
        if(getStatus()==false) return 0;
        for(int r=0; r<6; r++){
            for(int c=0; c<5; c++){
                if(p.getShelf().getTile(r, c)==null) return 0;
            }
        }
        this.status = false;
        return value;
    }
    public boolean getStatus(){
        return this.status;
    }

}
