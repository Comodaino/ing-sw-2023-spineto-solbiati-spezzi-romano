package Model;

import Model.CommonGoals.*;

public class GoalFactory {
    private int i;
    public GoalFactory(){
    }

    public CommonGoal getGoal(int i){
        switch(i){
            case 0: return new GoalAngles();
            case 1: return new GoalColumn();
            case 2: return new GoalCouples();
            case 3: return new GoalCross();
            case 4: return new GoalDiagonal();
            case 5: return new GoalDiffColumns();
            case 6: return new GoalDiffRows();
            case 7: return new GoalEight();
            case 8: return new GoalQuartets();
            case 9: return new GoalRow();
            case 10: return new GoalSquares();
            case 11: return new GoalStair();
        }
        return null;
    }
}
