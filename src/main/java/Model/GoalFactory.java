package Model;

import Model.CommonGoals.*;

public class GoalFactory {
    private int i;
    public GoalFactory(){
    }

    public CommonGoal getGoal(int i){
        switch(i){
            case 0: return new GoalAngles(2);
            case 1: return new GoalColumn(2);
            case 2: return new GoalCouples(2);
            case 3: return new GoalCross(2);
            case 4: return new GoalDiagonal(2);
            case 5: return new GoalDiffColumns(2);
            case 6: return new GoalDiffRows(2);
            case 7: return new GoalEight(2);
            case 8: return new GoalQuartets(2);
            case 9: return new GoalRow(2);
            case 10: return new GoalSquares(2);
            case 11: return new GoalStair(2);
        }
        return null;
    }
}
