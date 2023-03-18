package Model;

import Model.CommonGoals.*;

public class GoalFactory {
    private int i;
    public GoalFactory(){
    }

    public CommonGoal getGoal(int i){
        switch(i){modified vcs.xml
            case 0: return new GoalAngles(int n);
            case 1: return new GoalColumn(int n);
            case 2: return new GoalCouples(int n);
            case 3: return new GoalCross(int n);
            case 4: return new GoalDiagonal(int n);
            case 5: return new GoalDiffColumns(int n);
            case 6: return new GoalDiffRows(int n);
            case 7: return new GoalEight(int n);
            case 8: return new GoalQuartets(int n);
            case 9: return new GoalRow(int n);
            case 10: return new GoalSquares(int n);
            case 11: return new GoalStair(int n);
        }
    }
}
