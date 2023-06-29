package View;

public class Constant {
    private String persGoal;
    private String goal;
    private String tile;
    private String scoreCommonGoal;

    public Constant(){

        this.persGoal = "images/personal%20goal%20cards/Personal_Goals";
        this.goal = "images/common%20goal%20cards/";
        this.tile = "images/item%20tiles/";
        this.scoreCommonGoal = "images/scoring&20tokens/scoring_";
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
    public String getConstantScoreCommonGoal(){
        return scoreCommonGoal;
    }
}
