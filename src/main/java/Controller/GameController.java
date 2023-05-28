package Controller;

import Distributed.Lobby;
import Model.Board;
import Model.BoardView;
import Model.CommonGoals.CommonGoal;
import Model.Player;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Game controller controls the flow of the match, taking command using the update() method of the observable-observer pattern,
 * it represents the entirety of the controller in the MVC patter
 *
 * @author Alessio
 */
public class GameController implements Serializable {

    private Board gameBoard;
    private BoardView boardView;
    private Player currentPlayer;
    private List<Player> pl;
    private List<Player> donePlayers;
    private Lobby lobby;
    private int removeSize;

    public Board getBoard() {
        return gameBoard;
    }

    public BoardView getBoardView() {
        return boardView;
    }

    public void setBoardView(BoardView boardView) {
        this.boardView = boardView;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player p) {
        this.currentPlayer = p;
    }

    /**
     * Controls who the current player is by making the player next to the old current player the new current player
     *
     * @author Alessio
     */
    protected void spinHandler() {
        int i = gameBoard.getListOfPlayer().indexOf(currentPlayer) - 1;
        do {
            i += 1;
            if (i == gameBoard.getListOfPlayer().size() - 1) setCurrentPlayer(gameBoard.getListOfPlayer().get(0));
            else setCurrentPlayer(gameBoard.getListOfPlayer().get(i + 1));
        } while (donePlayers.contains(currentPlayer));
        gameBoard.setCurrentPlayer(currentPlayer);
    }

    private void serverUpdater() throws IOException, InterruptedException {
        lobby.updateAll();
    }

    /**
     * GameController constructor
     *
     * @param pl list of players
     * @author Alessio
     */
    public GameController(List<Player> pl, boolean firstMatch, Lobby lobby) throws IOException {
        this.gameBoard = new Board(firstMatch, pl);
        this.pl = pl;
        this.lobby = lobby;
        this.donePlayers = new ArrayList<Player>();
        this.boardView = new BoardView(gameBoard);
        for (Player player : pl) {
            if (player.getChair()) {
                this.currentPlayer = player;
                break;
            }
        }
        this.gameBoard.setCurrentPlayer(this.currentPlayer);
    }

    /**
     * @param arg an argument passed to the {@code notifyObservers}
     *            method. It is format is /command [par 0] [par 1] ...
     * @author Alessio
     */
    public void update(String arg) throws IOException, InterruptedException {
        String[] input = arg.split(" ");
        if (input[0].charAt(0) == '/') {
            switch (input[0]) {
                case "/remove":
                    playRemove(input);
                    serverUpdater();
                    break;
                case "/add":
                    playAdd(input);
                    serverUpdater();
                    break;
                case "/end":
                    playEndGame();
                    serverUpdater();
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Ends the game for a single player and makes him wait for the other players to finish
     */
    private void playEndGame() {
        System.out.println("ENDING THE GAME");
        for (int i = 0; i < gameBoard.getListOfPlayer().size(); i++) {
            if (gameBoard.getListOfPlayer().get(i).equals(currentPlayer)) {
                gameBoard.getListOfPlayer().get(i).addScore(gameBoard.getListOfPlayer().get(i).getGoal().getScore(gameBoard.getListOfPlayer().get(i).getShelf()));
                gameBoard.getListOfPlayer().get(i).addScore(gameBoard.getListOfPlayer().get(i).getNearGoal().getScore(gameBoard.getListOfPlayer().get(i)));
                donePlayers.add(currentPlayer);
                gameBoard.addToDone(currentPlayer);
                currentPlayer.setAsEnded();
                if(gameBoard.getDonePlayers().size() == gameBoard.getListOfPlayer().size()){
                    lobby.endMatch();
                }
            }
        }
    }

    /**
     * Removes a tile from the board and checks for recharge
     *
     * @param input input[0] is the command, preceded by /, input[1], input[3] and input[5] are the row coordinates, input[2], input[4] and input[6] are the column coordinates
     * @author Alessio
     */
    private void playRemove(String[] input) {
        switch (input.length) {
            case 3:
                removeSize = 1;
                break;
            case 5:
                removeSize = 2;
                break;
            case 7:
                removeSize = 3;
                break;
        }


        System.out.println("remove " + Arrays.toString(input));

        //A triple AND condition was not used to improve readability
        if (adjacentFree(input[1].charAt(0) - 48, input[2].charAt(0) - 48)) {
            if (removeSize < 2 || adjacentFree(input[3].charAt(0) - 48, input[4].charAt(0) - 48)) {
                if (removeSize < 3 || adjacentFree(input[5].charAt(0) - 48, input[6].charAt(0) - 48)) {
                    if (inLine(input)) {
                        gameBoard.removeTile(input[1].charAt(0) - 48, input[2].charAt(0) - 48);
                        if (removeSize > 1) gameBoard.removeTile(input[3].charAt(0) - 48, input[4].charAt(0) - 48);
                        if (removeSize > 2) gameBoard.removeTile(input[5].charAt(0) - 48, input[6].charAt(0) - 48);
                        gameBoard.checkRecharge();
                    }
                }
            }
        }

    }

    /**
     * Adds a tile to the current player shelf tanking it from the board buffer
     *
     * @param input input[0] is the command, preceded by /, input[1] is the column coordinate
     * @author Alessio
     */
    private void playAdd(String[] input) {
        System.out.println("add");
        if (columnAvailable(gameBoard.getTileBuffer().size(), input[1].charAt(0) - 48)) {
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
                        if (currentPlayer.getShelf().isFull()) {
                            while (gameBoard.getTileBuffer().size() != 0) gameBoard.getTileBuffer().remove(0);
                            playEndGame();
                            checkEnd();
                        }
                    }
                }
            }
            spinHandler();
        }
    }


    public boolean adjacentFree(int r, int c) {
        if (gameBoard.getCell(r, c).isEmpty()) return false;
        if (r == 0 || c == 0) return true;
        if (r == 8 || c == 8) return true;
        return ((gameBoard.getCell(r + 1, c).isEmpty() || gameBoard.getCell(r, c + 1).isEmpty()) || (gameBoard.getCell(r - 1, c).isEmpty() || gameBoard.getCell(r, c - 1).isEmpty()));
    }

    public boolean inLine(String[] input) {
        if(removeSize == 1) return true;


        if (removeSize > 1  && (input[1].charAt(0) - 48 == input[3].charAt(0) - 48)){
            if(removeSize > 2){
                if((input[1].charAt(0) - 48 == input[5].charAt(0) - 48)) return true;
            }else return true;

        }

        if (removeSize > 1  && (input[2].charAt(0) - 48 == input[4].charAt(0) - 48)){
            if(removeSize > 2){
                if((input[2].charAt(0) - 48 == input[6].charAt(0) - 48)) return true;
            }else return true;
        }

        return false;
    }

    public boolean columnAvailable(int c, int size) {
        for (int i = 0; i < gameBoard.getListOfPlayer().size(); i++) {
            if (gameBoard.getListOfPlayer().get(i).equals(currentPlayer)) {
                return gameBoard.getListOfPlayer().get(i).getShelf().isEmpty(5 - size, c);
            }
        }
        return false;
    }

    private void checkEnd() {
        if (donePlayers.size() == gameBoard.getListOfPlayer().size()) {
            lobby.endMatch();
        }
    }
}

/* possible commands:
 *  /add playerName column
 *  /remove playerName row column (can repeat row column maximum 3 times)
 *
 * */
