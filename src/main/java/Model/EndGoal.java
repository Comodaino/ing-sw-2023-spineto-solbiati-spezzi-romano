package Model;
public class EndGoal {
    private boolean status;
    public final int value=1;

    public EndGoal() {
        this.status = true;
    }
    public int isCompleted(){ //NEEDS TO BE REVIEWED
        this.status=false;
        return 1;
    }
    public boolean getStatus(){
        return this.status;
    }

}
