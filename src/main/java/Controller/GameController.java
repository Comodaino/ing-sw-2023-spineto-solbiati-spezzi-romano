package Controller;

import Distributed.RMI.client.Client;
import Distributed.RMI.server.ClientHandlerRMI;
import Distributed.RemoteHandler;
import Model.Board;
import Model.BoardView;
import Model.Player;

import java.util.List;

public abstract class GameController {
    protected Board gameBoard;
    protected BoardView boardView;
    protected Player currentPlayer;
    protected List<Player> pl;
    protected List<Player> donePlayers;
    public Board getBoard() {
        return gameBoard;
    }
    public BoardView getBoardView() { return boardView; }
    public void setBoardView(BoardView boardView){
        this.boardView=gameBoard.boardView;
    }
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player p){
        this.currentPlayer= p;
    }
    /**
     * Controls who the current player is by making the player next to the old current player the new current player
     * @author Alessio
     */
    protected void spinHandler() {
        int i = gameBoard.getListOfPlayer().indexOf(currentPlayer);
        do {
            if (i == gameBoard.getListOfPlayer().size() - 1) setCurrentPlayer(gameBoard.getListOfPlayer().get(0));
            else setCurrentPlayer(gameBoard.getListOfPlayer().get(i + 1));
        } while (donePlayers.contains(currentPlayer));
        gameBoard.setCurrentPlayer(currentPlayer);
    }

    //TODO with ClientHandlerRMI instead of RemoteHandler
    public void update(RemoteHandler client, Object arg) {
    }
}
