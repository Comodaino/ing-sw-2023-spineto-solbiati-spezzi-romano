package Controller;

import Distributed.ConnectionType;
import Distributed.ServerSocket.ClientHandlerSocket;
import Model.Board;
import Model.BoardView;
import Model.CommonGoals.CommonGoal;
import Model.Player;

import java.io.IOException;
import java.util.*;

/**
 * Game controller controls the flow of the match, taking command using the update() method of the observable-observer pattern,
 * it represents the entirety of the controller in the MVC patter
 * @author Alessio
 */
public class GameController implements Observer {

    private Board gameBoard;
    private BoardView boardView;
    private Player currentPlayer;
    private List<Player> pl;
    private List<Player> donePlayers;
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
    private void serverUpdater() throws IOException {
        for(Player p: pl){
            if(p.getRemotePlayer().getType().equals(ConnectionType.SOCKET)) p.getRemotePlayer().update();
            //if(p.getRemotePlayer().getType().equals(ConnectionType.Socket)) //TODO IMPLEMENT ONCE RMI IS DONE
        }
    }
    //TODO The following constructor needs to be reviewed and modified after the lesson about sockets and view

    /**
     * GameController constructor
     * @param pl list of players
     * @author Alessio
     */
    public GameController(List<Player> pl, boolean firstMatch) {
        this.gameBoard = new Board(firstMatch, pl);
        this.donePlayers = new ArrayList<Player>();
        for(int i=0; i< pl.size(); i++){
            if(pl.get(i).getChair()){
                this.currentPlayer = pl.get(i);
                break;
            }
        }
    }

    /**
     *
     * @param o     the observable object.
     * @param arg   an argument passed to the {@code notifyObservers}
     *                 method. It is format is /command [par 0] [par 1] ...
     * @author Alessio
     */
    @Override
    public void update(Observable o, Object arg) {
        //TODO CHECK WITH LAB ASSISTANT
        //TODO ERRORS
        if (true) {
            String input[] = arg.toString().split(" ");
            if (input[0].charAt(0) == '/') {
                switch (input[0]) {
                    case "/remove":
                        playRemove(input);
                        break;
                    case "/add":
                        playAdd(input);
                        break;
                }
            } else {
                System.out.println("Commands must start with '/'");
            }
        } else {
            System.out.println("Wrong Observable");
        }
    }

    public void update(ClientHandlerSocket o, Object arg) {
        //TODO CHANGE CONDITION FOR TESTING
        if (o.getPlayer().getNickname().equals(currentPlayer.getNickname())) {
            String input[] = arg.toString().split(" ");
            if (input[0].charAt(0) == '/') {
                switch (input[0]) {
                    case "/remove":
                        playRemove(input);
                        break;
                    case "/add":
                        playAdd(input);
                        break;
                }
            } else {
                System.out.println("Commands must start with '/'");
            }
        } else {
            System.out.println("Player " + o.getPlayer().getNickname() + " tried to play during another player's turn");
        }
    }

    /**
     * Ends the game for a single player and makes him wait for the other players to finish
     */
    private void playEndGame() {
        for (int i = 0; i < gameBoard.getListOfPlayer().size(); i++) {
            if (gameBoard.getListOfPlayer().get(i).equals(currentPlayer)) {
                gameBoard.getListOfPlayer().get(i).addScore(gameBoard.getListOfPlayer().get(i).getGoal().getScore(gameBoard.getListOfPlayer().get(i).getShelf()));
                gameBoard.getListOfPlayer().get(i).addScore(gameBoard.getListOfPlayer().get(i).getNearGoal().getScore(gameBoard.getListOfPlayer().get(i)));
                donePlayers.add(currentPlayer);
            }
        }
    }

    /**
     * Removes a tile from the board and checks for recharge
     * @param input input[0] is the command, preceded by /, input[1] is the row coordinate, input[2] is the column coordinate
     * @author Alessio
     */
    private void playRemove(String[] input) {
        System.out.println("remove " + Arrays.toString(input));
        gameBoard.removeTile(input[1].charAt(0) - 48, input[2].charAt(0) - 48);
        gameBoard.checkRecharge();
    }
    /**
     * Adds a tile to the current player shelf tanking it from the board buffer
     * @param input input[0] is the command, preceded by /, input[1] is the column coordinate
     * @author Alessio
     */
    private void playAdd(String[] input) {
        System.out.println("add");
        for (int i = 0; i < gameBoard.getListOfPlayer().size(); i++) {
            if (gameBoard.getListOfPlayer().get(i).equals(currentPlayer)) {
                while (gameBoard.getTileBuffer().size() > 0) {
                    gameBoard.getListOfPlayer().get(i).getShelf().addTile(input[1].charAt(0) - 48, gameBoard.getTileBuffer().remove(0));

                    for (CommonGoal cg : gameBoard.getSetOfCommonGoal()) {
                        gameBoard.getListOfPlayer().get(i).addScore(cg.getScore(gameBoard.getListOfPlayer().get(i)));
                    }
                    if (gameBoard.getEndGoal().getStatus()) {
                        gameBoard.getListOfPlayer().get(i).addScore(gameBoard.getEndGoal().getScore(gameBoard.getListOfPlayer().get(i)));
                    }
                    if (currentPlayer.getShelf().isFull()){
                        while(gameBoard.getTileBuffer().size()!=0) gameBoard.getTileBuffer().remove(0);
                    }
                }
            }
        }
        spinHandler();
    }
}
