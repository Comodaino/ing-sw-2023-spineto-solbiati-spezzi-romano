package Model;

import Model.CommonGoals.*;

import java.io.Serializable;

public class GoalFactory implements Serializable {
    public GoalFactory(){
    }

    public CommonGoal getGoal(int i, int size){
        switch(i){
            case 0: return new GoalAngles(size);
            case 1: return new GoalColumn(size);
            case 2: return new GoalCouples(size);
            case 3: return new GoalCross(size);
            case 4: return new GoalDiagonal(size);
            case 5: return new GoalDiffColumns(size);
            case 6: return new GoalDiffRows(size);
            case 7: return new GoalEight(size);
            case 8: return new GoalQuartets(size);
            case 9: return new GoalRow(size);
            case 10: return new GoalSquares(size);
            case 11: return new GoalStair(size);
        }
        return null;
    }
}
