package Model;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class Board {
    private Cell matrix[8][8];
    private List<Player> listOfPlayer;
    private boolean firstMatch;
    private Set<CommonGoal> setOfCommonGoal;
    private boolean firstToEnd;
    private List<Tile> tileBuffer;


    public Board Board(int np, boolean fm, List<Player> pl){
        throw new NotImplementedException;
    }
    public void endMatch(){
        throw new NotImplementedException;
    }
    public void removeTile(Tile tile){
        throw new NotImplementedException;
    }
    public void checkRecharge(){
        throw new NotImplementedException;
    }
}
