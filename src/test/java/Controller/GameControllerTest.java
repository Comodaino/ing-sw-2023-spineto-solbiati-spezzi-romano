package Controller;

import Model.Board;
import Model.Player;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Scanner;

class GameControllerTest extends Observable{
    @Test
    public void inputTestTwo() throws FileNotFoundException {
        List<Player> playerList=new ArrayList<Player>();
        playerList.add(new Player("player1", true));
        playerList.add(new Player("player2", false));
        GameController testController = new GameController(playerList);
        Board gameBoard = testController.getBoard();
        this.addObserver(testController);
        File commands = new File("src/test/java/Controller/controllerTestConf2");
        Scanner commandScanner = new Scanner(commands);
        while(commandScanner.hasNextLine()){
            setChanged();
            System.out.println("current player: " + testController.getCurrentPlayer().getNickname());
            notifyObservers(commandScanner.nextLine());
            System.out.println();
        }
    }
    @Test
    public void inputTestThree() throws FileNotFoundException {
        List<Player> playerList=new ArrayList<Player>();
        playerList.add(new Player("player1", false));
        playerList.add(new Player("player2", true));
        playerList.add(new Player("player3", false));
        GameController testController = new GameController(playerList);
        Board gameBoard = testController.getBoard();
        this.addObserver(testController);
        File commands = new File("src/test/java/Controller/controllerTestConf2");
        Scanner commandScanner = new Scanner(commands);
        while(commandScanner.hasNextLine()){
            setChanged();
            System.out.println("current player: " + testController.getCurrentPlayer().getNickname());
            notifyObservers(commandScanner.nextLine());
            System.out.println();
        }
    }
}