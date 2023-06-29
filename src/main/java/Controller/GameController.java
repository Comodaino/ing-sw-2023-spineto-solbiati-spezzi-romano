package Controller;

import Distributed.Lobby;
import Distributed.RemotePlayer;
import Model.*;
import Model.CommonGoals.CommonGoal;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
    private String firstPlayerToEnd;
    private int endGameCounter;

    /**
     * @param firstMatch setting that changes how the board is built
     * @param lobby      reference to the lobby of the game
     * @throws IOException
     * @author Alessio
     */
    public GameController(boolean firstMatch, Lobby lobby) throws IOException {
        this.gameBoard = new Board(firstMatch);
        pl = gameBoard.getListOfPlayer();
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
        this.firstPlayerToEnd = null;

        Thread th = new Thread() {
            @Override
            public void run() {
                try {
                    connectionChecker();
                } catch (InterruptedException | IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        th.start();

    }

    /**
     * Adds a player to the model
     *
     * @param player
     */
    public void addPlayer(Player player) {
        gameBoard.addPlayer(player);
    }

    private void connectionChecker() throws InterruptedException, IOException {
        while (true) {
            TimeUnit.MILLISECONDS.sleep(500);
            for (RemotePlayer p : lobby.getListOfPlayers()) {
                if (lobby.getPlay() && currentPlayer != null) {
                    if (p.getNickname().equals(currentPlayer.getNickname()) && !p.isConnected()) {
                        System.out.println("checking");
                        spinHandler();
                        serverUpdater();
                    }
                }
            }
        }
    }

    /**
     * Controls who the current player is by making the player next to the old current player the new current player
     *
     * @author Alessio
     */
    private void spinHandler() {

        if (lobby.getPlay()) {

            int disconnectedNumber = 0;
            for (RemotePlayer rp : lobby.getListOfPlayers()) {
                if (!rp.isConnected()) disconnectedNumber++;
            }

            if (gameBoard.getListOfPlayer().size() - donePlayers.size() - disconnectedNumber <= 1) {

                try {
                    TimeUnit.SECONDS.sleep(20);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                if (gameBoard.getListOfPlayer().size() - donePlayers.size() - disconnectedNumber <= 1){
                    System.out.println("TIMEOUT ENDED, CLOSING THE GAME");
                    forceEndGame();
                    try {
                        lobby.updateAll();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }


            }

            int i = gameBoard.getListOfPlayer().indexOf(currentPlayer) - 1;

            for (int j = gameBoard.getTileBuffer().size() - 1; j >= 0; j--) {
                gameBoard.getTileBuffer().remove(j);
            }

            boolean flag = false;
            do {
                i += 1;

                if(i + 2 < gameBoard.getListOfPlayer().size() && gameBoard.getListOfPlayer().get(i+2).getNickname().equals(firstPlayerToEnd)) forceEndGame();
                if(i + 1 > gameBoard.getListOfPlayer().size() && gameBoard.getListOfPlayer().get(0).getNickname().equals(firstPlayerToEnd)) forceEndGame();



                if (i == gameBoard.getListOfPlayer().size() - 1){
                    if(gameBoard.getListOfPlayer().get(0).getNickname().equals(firstPlayerToEnd)) forceEndGame();
                    setCurrentPlayer(gameBoard.getListOfPlayer().get(0));
                }
                else{
                    if(gameBoard.getListOfPlayer().get(i+1).getNickname().equals(firstPlayerToEnd)) forceEndGame();
                    setCurrentPlayer(gameBoard.getListOfPlayer().get(i + 1));
                }

                for (RemotePlayer p : lobby.getListOfPlayers()) {
                    if (p.getNickname().equals(currentPlayer.getNickname()) && !p.isConnected()) flag = true;
                }

            } while (donePlayers.contains(currentPlayer) || flag);
            gameBoard.setCurrentPlayer(currentPlayer);
            if (endGameCounter != 0) this.endGameCounter++;


        }
    }

    private void serverUpdater() throws IOException, InterruptedException {
        lobby.updateAll();
    }


    /**
     * Ends the game for a single player and makes him wait for the other players to finish
     */
    private void playEndGame() {

        System.out.println(currentPlayer.getNickname() + "ENDED THE GAME");
        firstPlayerToEnd = currentPlayer.getNickname();
    }


    private void forceEndGame() {
        for (int i = 0; i < gameBoard.getListOfPlayer().size(); i++) {

            gameBoard.getListOfPlayer().get(i).addScore(gameBoard.getListOfPlayer().get(i).getGoal().getScore(gameBoard.getListOfPlayer().get(i).getShelf()));
            gameBoard.getListOfPlayer().get(i).addScore(gameBoard.getListOfPlayer().get(i).getNearGoal().getScore(gameBoard.getListOfPlayer().get(i)));

            lobby.endMatch();
        }
    }

    private void playSwitch(String[] input) {

        if (!input[1].equals(currentPlayer.getNickname())) return;
        String[] tmp = new String[input.length - 1];
        for (int i = 1; i < input.length - 1; i++) {
            tmp[i] = input[i + 1];
        }
        input = tmp;

        if (gameBoard.getTileBuffer().size() < 2) return;
        int first = input[1].charAt(0) - 48;
        int second = input[2].charAt(0) - 48;
        System.out.println("LENGTH: " + gameBoard.getTileBuffer().size() + " " + first + second);
        if (gameBoard.getTileBuffer().size() < first || gameBoard.getTileBuffer().size() < second) return;
        Collections.swap(gameBoard.getTileBuffer(), first, second);
        System.out.println("swapped");
    }

    /**
     * Removes a tile from the board and checks for recharge
     *
     * @param input input[0] is the command, preceded by /, input[1], input[3] and input[5] are the row coordinates, input[2], input[4] and input[6] are the column coordinates
     * @author Alessio
     */
    private void playRemove(String[] input) {

        if (!input[1].equals(currentPlayer.getNickname())) return;
        String[] tmp = new String[input.length - 1];
        for (int i = 1; i < input.length - 1; i++) {
            tmp[i] = input[i + 1];
        }
        input = tmp;


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
        } else System.err.println("NOT FREE");
    }

    /**
     * Adds a tile to the current player shelf tanking it from the board buffer
     *
     * @param input input[0] is the command, preceded by /, input[1] is the column coordinate
     * @author Alessio
     */
    private void playAdd(String[] input) {

        if (!input[1].equals(currentPlayer.getNickname())) return;
        String[] tmp = new String[input.length - 1];
        String tmpNick = input[1];
        for (int i = 1; i < input.length - 1; i++) {
            tmp[i] = input[i + 1];
        }
        input = tmp;

        System.out.println("add");
        if (columnAvailable(input[1].charAt(0) - 48, tmpNick)) {
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
                        if (currentPlayer.getShelf().isFull() || endGameCounter != 0) {
                            this.endGameCounter++;
                            gameBoard.getTileBuffer().removeAll(gameBoard.getTileBuffer());
                            playEndGame();
                            checkEnd();
                        }
                    }
                }
            }
            spinHandler();
        }
    }


    private boolean adjacentFree(int r, int c) {
        if (gameBoard.getCell(r, c).isEmpty()) return false;
        if (r == 0 || c == 0) return true;
        if (r == 8 || c == 8) return true;
        return ((gameBoard.getCell(r + 1, c).isEmpty() || gameBoard.getCell(r, c + 1).isEmpty()) || (gameBoard.getCell(r - 1, c).isEmpty() || gameBoard.getCell(r, c - 1).isEmpty()));
    }

    private boolean inLine(String[] input) {
        if (removeSize == 1) return true;


        if (removeSize > 1 && (input[1].charAt(0) - 48 == input[3].charAt(0) - 48)) {
            if (removeSize > 2) {
                if ((input[1].charAt(0) - 48 == input[5].charAt(0) - 48)) return true;
            } else return true;

        }

        if (removeSize > 1 && (input[2].charAt(0) - 48 == input[4].charAt(0) - 48)) {
            if (removeSize > 2) {
                if ((input[2].charAt(0) - 48 == input[6].charAt(0) - 48)) return true;
            } else return true;
        }
        return false;
    }

    private boolean columnAvailable(int c, String name) {
        for (int i = 0; i < gameBoard.getListOfPlayer().size(); i++) {
            if (gameBoard.getListOfPlayer().get(i).getNickname().equals(name)) {
                if(gameBoard.getTileBuffer().size() <= 0 ) return false;
                return gameBoard.getListOfPlayer().get(i).getShelf().isEmpty(6 - gameBoard.getTileBuffer().size(), c);
            }
        }
        return false;
    }

    private void checkEnd() {
        if (donePlayers.size() == gameBoard.getListOfPlayer().size()) {
            forceEndGame();
        }
    }

    private void newMessage(String[] input) {

        String tmp = "[" + input[1] + "]";
        for (int i = 2; i < input.length; i++) {
            tmp = tmp + " " + input[i];
        }

        if (gameBoard.getChatBuffer().size() >= 3) gameBoard.getChatBuffer().remove(0);
        gameBoard.getChatBuffer().add(tmp);
    }

    private void newWhisper(String[] input) {
        String tmp = "[" + input[1] + "](to you)";
        for (int i = 3; i < input.length; i++) {
            tmp = tmp + " " + input[i];
        }

        int counter = 0;
        for (Whisper w : gameBoard.getPersonalChatBuffer()) {
            if (w.getRecipient().equals(input[2])) {
                counter += 1;
            }
        }
        if (counter >= 3) {
            for (Whisper w : gameBoard.getPersonalChatBuffer()) {
                if (w.getRecipient().equals(input[2])) {
                    gameBoard.getPersonalChatBuffer().remove(w);
                    break;
                }
            }
        }
        gameBoard.getPersonalChatBuffer().add(new Whisper(input[1], input[2], tmp));
    }


    /**
     * receives, processes and executes the command passed as parameter, does nothin is the move is invalid or the command is wrong
     *
     * @param arg an argument passed to the {@code notifyObservers}
     *            method. It is format is /command [par 0] [par 1] ...
     * @author Alessio
     */
    public void update(String arg) throws IOException, InterruptedException {
        String[] input = arg.split(" ");
        if (input[0].charAt(0) == '/') {
            switch (input[0]) {
                case "/remove":
                    if (lobby.getPlay() && input.length > 2 && input.length % 2 == 0 && gameBoard.getTileBuffer().size() == 0) {
                        playRemove(input);
                        serverUpdater();
                    }
                    break;
                case "/add":
                    if (lobby.getPlay() && input.length > 2 && gameBoard.getTileBuffer().size() > 0) {
                        playAdd(input);
                        serverUpdater();
                    }
                    break;
                case "/switch":
                    if (lobby.getPlay() && input.length > 2 && gameBoard.getTileBuffer().size() > 0) {
                        playSwitch(input);
                        serverUpdater();
                    }
                    break;
                case "/end":
                    if (lobby.getPlay()) {
                        playEndGame();
                        serverUpdater();
                    }
                    break;
                case "/message":
                    System.out.println("ziopera");
                    newMessage(input);
                    serverUpdater();
                    break;
                case "/whisper":
                    newWhisper(input);
                    serverUpdater();
                    break;
                default:
                    break;
            }
        } else System.err.println("NOT A COMMAND");
    }


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

    public void startGame() {
        this.endGameCounter = 0;
        gameBoard.init();
        for (Player p : gameBoard.getListOfPlayer()) {
            if (p.getChair()) {
                this.currentPlayer = p;
                gameBoard.setCurrentPlayer(p);
                break;
            }
        }
    }

    public void setFM(boolean fm) {
        this.gameBoard.setFm(fm);
    }

    /**
     * Removes the player with the nickname equal to the parameter from the match
     * @param nickname
     */
    public void removePlayer(String nickname) {
        this.gameBoard.removePlayer(nickname);
    }
}

