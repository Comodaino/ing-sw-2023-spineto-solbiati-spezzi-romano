package Controller;

import Model.Board;
import Model.CommonGoals.CommonGoal;
import Model.Player;
import View.ViewInterface;

import java.util.*;

public class GameController implements Observer {
    private final Board gameBoard;
    private ViewInterface gameTui;
    private Player currentPlayer;
    private List<Player> donePlayers;

    //TODO The following constructor needs to be reviewed and modified after the lesson about sockets and view
    public GameController(List<Player> pl) {
        //this.gameTui = new View()
        this.gameBoard = new Board(false, pl);
        this.donePlayers = new ArrayList<Player>();
        for(int i=0; i< pl.size(); i++){
            if(pl.get(i).getChair()){
                this.currentPlayer = pl.get(i);
                break;
            }
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        //TODO CHANGE CONDITION FOR TESTING
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

    private void playEndGame(String[] input) {
        //TODO NEEDS TO DECIDE IF TO END FOR EVERYONE OR PLAYER PER PLAYER
        for (int i = 0; i < gameBoard.getListOfPlayer().size(); i++) {
            if (gameBoard.getListOfPlayer().get(i).equals(currentPlayer)) {
                gameBoard.getListOfPlayer().get(i).addScore(gameBoard.getListOfPlayer().get(i).getGoal().getScore(gameBoard.getListOfPlayer().get(i).getShelf()));
                gameBoard.getListOfPlayer().get(i).addScore(gameBoard.getListOfPlayer().get(i).getNearGoal().getScore(gameBoard.getListOfPlayer().get(i)));
                donePlayers.add(currentPlayer);
            }

        }
    }

    //NOTE: INPUT LAYOUT SHOULD BE "\COMMAND PLAYERNAME PAR1 PAR2 ...
    private void playRemove(String[] input) {
        System.out.println("remove " + Arrays.toString(input));
        gameBoard.removeTile(input[1].charAt(0) - 48, input[2].charAt(0) - 48);
        gameBoard.checkRecharge();
    }

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
                    /* TODO add when isFull is implemented
                    if (currentPlayer.getShelf().isFull()){
                        while(gameBoard.getTileBuffer().size()!=0) gameBoard.getTileBuffer().remove(0);
                    }
                    */

                }
            }
        }
        spinHandler();
    }

    private void spinHandler() {
        int i = gameBoard.getListOfPlayer().indexOf(currentPlayer);
        do {
            if (i == gameBoard.getListOfPlayer().size() - 1) setCurrentPlayer(gameBoard.getListOfPlayer().get(0));
            else setCurrentPlayer(gameBoard.getListOfPlayer().get(i + 1));
        } while (donePlayers.contains(currentPlayer));
        gameBoard.setCurrentPlayer(currentPlayer);
    }

    public Board getBoard() {
        return gameBoard;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }
    public void setCurrentPlayer(Player p){
        this.currentPlayer= p;
    }
}
