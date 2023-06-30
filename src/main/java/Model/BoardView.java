package Model;

import Model.CommonGoals.CommonGoal;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * This class is used to create a view of the board, so that the client can only see the information that he needs.
 * @author Alessio
 */
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
    public List<CommonGoal> getSetOfCommonGoal() {
        return board.getSetOfCommonGoal();
    }
    public EndGoal getEndGoal() { return board.getEndGoal(); }
    public String getWinner() { return board.getWinner(); }
    public List<Player> getDonePlayers() { return  board.getDonePlayers();}
    public Player getCurrentPlayer() { return board.getCurrentPlayer();}
    public List<Whisper> getPersonalChatBuffer(){ return board.getPersonalChatBuffer();}
    public List<String> getChatBuffer(){ return board.getChatBuffer();}
}
