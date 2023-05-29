package View;

public class Constant {
    private String persGoal;
    private String goal;

    public Constant(){
        this.persGoal = persGoal;
        this.goal = goal;
        persGoal="images/personal goal cards/Personal_Goals";
        goal="images/common goal cards/";
    }
    public String getCostantPersGoal(){
        return persGoal;
    }
    public String getConstantGoal(){
        return goal;
    }
}

