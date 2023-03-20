package Controller;

import Model.Board;
import View.ViewInterface;

import java.util.Observable;
import java.util.Observer;
public class GameController implements Observer {
    private Board gameBoard;
    private ViewInterface gameTui;
    //The following constructor needs to be reviewed and modified after the lesson about sockets and view
    public GameController(){
        //this.gameTui = new View()
        this.gameBoard = new Board();
    }

    @Override
    public void update(Observable o, Object arg) {

    }

}
