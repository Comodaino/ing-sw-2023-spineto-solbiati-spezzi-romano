package Controller;

import Model.Board;
import Model.Player;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Scanner;

class GameControllerTest extends Observable{
    @Test
    public void inputTest() throws FileNotFoundException {
        List<Player> playerList=new ArrayList<Player>();
        playerList.add(new Player("player1", true));
        playerList.add(new Player("player2", false));
        GameController testController = new GameController(playerList);
        Board gameBoard = testController.getBoard();
        this.addObserver(testController);
        File commands = new File("src/test/java/Controller/controllerTestConf");
        Scanner commandScanner = new Scanner(commands);
        while(commandScanner.hasNextLine()){
            setChanged();
            notifyObservers(commandScanner.nextLine());
            for(int i=5; i>=0; i--){
                for(int j=0; j<5; j++){
                    if(gameBoard.getListOfPlayer().get(0).getShelf().getTile(i,j)!=null) System.out.print(gameBoard.getListOfPlayer().get(0).getShelf().getTile(i,j).getColor() + " ");
                    else System.out.print("null ");
                }
                System.out.println();
            }
            System.out.println();
        }
    }
}