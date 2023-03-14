package Model.CommonGoals;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class GoalAngles extends CommonGoal{
    public int getScore(){
        if(shelf.getTile(0, 0).getColor.equals(shelf.getTile(0, 5).getColor) &&
                shelf.getTile(5, 0).getColor.equals(shelf.getTile(5, 4).getColor) &&
                shelf.getTile(0, 0).getColor.equals(shelf.getTile(5, 0).getColor)) {

            switch(numberOfCompleted){ //the same player cannot complete the same goal 2 times
                case 0:
                    numberOfCompleted++;
                    return 8;
                case 1:
                    numberOfCompleted++;
                    return 6;
                case 2:
                    numberOfCompleted++;
                    return 4;
                case 3:
                    numberOfCompleted++;
                    return 2;
            }
        } else {
            throw new NotCompletedException(); //implement the exception!!
        }
        throw new NotImplementedException();
    }
}
