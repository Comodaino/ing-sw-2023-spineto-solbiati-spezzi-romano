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
    //The following constructor needs to be reviewed and modified after the lesson about sockets and view
    public GameController(){
        //this.gameTui = new View()
        List<Player> playerList=new ArrayList<Player>();
        playerList.add(new Player("player1", true));
        playerList.add(new Player("player2", false));
        this.gameBoard = new Board(false, playerList);
    }

    @Override
    public void update(Observable o, Object arg) {
        if(o==gameTui){
            String input[] = arg.toString().split(" ");
            if(input[0].charAt(0) == '/'){
            switch(input[0]) {
                case "/remove": playRemove(input);
                    break;
                case "/add": playAdd(input);
                    break;
                case "/endGame":
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

    }


    //NOTE: INPUT LAYOUT SHOULD BE "\COMMAND PLAYERNAME PAR1 PAR2 ...
    private void playRemove(String[] input) {
        gameBoard.removeTile(input[2].charAt(0) - 48, input[3].charAt(0) - 48);
        gameBoard.checkRecharge();
    }

    private void playAdd(String[] input) {
        for(int i=0; i<gameBoard.getListOfPlayer().size(); i++){
            if(gameBoard.getListOfPlayer().get(i).getNickname().equals(input[1])){
                gameBoard.getListOfPlayer().get(i).getShelf().addTile(input[2].charAt(0) - 48, gameBoard.getTileBuffer().remove(0));
                gameBoard.getListOfPlayer().get(i).addScore(gameBoard.getListOfPlayer().get(i).getGoal().getScore(gameBoard.getListOfPlayer().get(i).getShelf()));
                for(CommonGoal cg: gameBoard.getSetOfCommonGoal()) {
                    gameBoard.getListOfPlayer().get(i).addScore(cg.getScore(gameBoard.getListOfPlayer().get(i)));
                }
                if(gameBoard.getEndGoal().getStatus()){
                    gameBoard.getListOfPlayer().get(i).addScore(gameBoard.getEndGoal().isCompleted());
                }
            }
        }
    }

}
