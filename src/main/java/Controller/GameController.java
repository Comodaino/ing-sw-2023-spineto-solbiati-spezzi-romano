package Controller;

import Model.Board;
import Model.Player;
import View.ViewInterface;

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
            String input = arg.toString();
            if(input.charAt(0) == '/')
            switch(input.substring(0,input.indexOf(' ')-1)){
                case "/remove":
                    break;
            }else{
                System.out.("Commands must start with '/'");
            }
        }else{
            System.out.("Wrong Observable");
        }
    }

}
