package View;

public class Constant {
    private String persGoal;
    private String goal;
    private String tile;

    public Constant(){

        this.persGoal = "images/personal goal cards/Personal_Goals";
        this.goal = "images/common goal cards/";
        this.tile = "images/item tiles/";
    }
    public String getCostantPersGoal(){
        return persGoal;
    }
    public String getConstantGoal(){
        return goal;
    }
    public String getConstantTile(){
        return tile;
    }
}
