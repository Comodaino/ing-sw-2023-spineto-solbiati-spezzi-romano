package Controller;

import Model.Board;
import Model.Player;

import java.util.List;

public abstract class GameController {
    protected Board gameBoard;
    protected Player currentPlayer;
    protected List<Player> pl;
    protected List<Player> donePlayers;
    public Board getBoard() {
        return gameBoard;
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
}
