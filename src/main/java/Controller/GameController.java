package Controller;

import Model.Board;
import Model.CommonGoals.CommonGoal;
import Model.Player;
import View.ViewInterface;
import jdk.jshell.spi.ExecutionControl;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
public class GameController implements Observer {
    private Board gameBoard;
    private ViewInterface gameTui;
    //TODO The following constructor needs to be reviewed and modified after the lesson about sockets and view
    public GameController(List<Player> pl){
        //this.gameTui = new View()
        this.gameBoard = new Board(false, pl);
    }

    @Override
    public void update(Observable o, Object arg) {
        System.out.println(arg);
        //TODO CHANGE CONDITION FOR TESTING
        if(true){
            System.out.println(arg);
            String input[] = arg.toString().split(" ");
            if(input[0].charAt(0) == '/'){
            switch(input[0]) {
                case "/remove": playRemove(input);
                    break;
                case "/add": playAdd(input);
                    break;
                case "/endgame":
                    try {
                        playEndGame(input);
                    } catch (ExecutionControl.NotImplementedException e) {
                        throw new RuntimeException(e);
                    }
            }
            }else{
                System.out.println("Commands must start with '/'");
            }
        }else{
            System.out.println("Wrong Observable");
        }
    }

    private void playEndGame(String[] input) throws ExecutionControl.NotImplementedException {
        //TODO NEEDS TO DECIDE IF TO END FOR EVERYONE OR PLAYER PER PLAYER
        for(int i=0; i<gameBoard.getListOfPlayer().size(); i++) {
            if (gameBoard.getListOfPlayer().get(i).getNickname().equals(input[1])) {
                gameBoard.getListOfPlayer().get(i).addScore(gameBoard.getListOfPlayer().get(i).getGoal().getScore(gameBoard.getListOfPlayer().get(i).getShelf()));
                gameBoard.getListOfPlayer().get(i).addScore(gameBoard.getListOfPlayer().get(i).getNearGoal().getScore(gameBoard.getListOfPlayer().get(i)));
            }
        }
    }

    //NOTE: INPUT LAYOUT SHOULD BE "\COMMAND PLAYERNAME PAR1 PAR2 ...
    private void playRemove(String[] input) {
        System.out.println("remove");
        gameBoard.removeTile(input[2].charAt(0) - 48, input[3].charAt(0) - 48);
        gameBoard.checkRecharge();
    }

    private void playAdd(String[] input) {
        System.out.println("add");
        for(int i=0; i<gameBoard.getListOfPlayer().size(); i++){
            if(gameBoard.getListOfPlayer().get(i).getNickname().equals(input[1])){
                gameBoard.getListOfPlayer().get(i).getShelf().addTile(input[2].charAt(0) - 48, gameBoard.getTileBuffer().remove(0));

                for(CommonGoal cg: gameBoard.getSetOfCommonGoal()) {
                    gameBoard.getListOfPlayer().get(i).addScore(cg.getScore(gameBoard.getListOfPlayer().get(i)));
                }
                if(gameBoard.getEndGoal().getStatus()){
                    gameBoard.getListOfPlayer().get(i).addScore(gameBoard.getEndGoal().getScore(gameBoard.getListOfPlayer().get(i)));
                }
            }
        }
    }

    public Board getBoard() {
        return gameBoard;
    }
}
