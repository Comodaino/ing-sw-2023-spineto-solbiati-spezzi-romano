package Model;
public class EndGoal {
    private boolean status;
    public final int value=1;

    public EndGoal() {
        this.status = true;
    }
    public void isComplited(){
        this.status=false;
    }
    public boolean getStatus(){
        return this.status;
    }

}
