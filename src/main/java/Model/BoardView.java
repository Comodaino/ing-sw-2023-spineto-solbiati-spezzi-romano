package Model;

import Model.CommonGoals.CommonGoal;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public class BoardView implements Serializable {
    static final long serialVersionUID = 1L;
    private final Board board;
    public BoardView(Board b){ this.board=b; }
    public Tile getTile(int r, int c){
        return board.getTile(r, c);
    }
    public Cell getCell(int r, int c){
        return board.getCell(r, c);
    }
    public List<Tile> getTileBuffer() {
        return board.getTileBuffer();
    }
    public List<Player> getListOfPlayer() {
        return board.getListOfPlayer();
    }
    public Set<CommonGoal> getSetOfCommonGoal() {
        return board.getSetOfCommonGoal();
    }
    public EndGoal getEndGoal() { return board.getEndGoal(); }
}
