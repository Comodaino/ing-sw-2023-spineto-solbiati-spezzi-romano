package Controller;

import Distributed.Lobby;
import Distributed.ServerApp;
import Model.Board;
import Model.Player;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Scanner;

class GameControllerTest extends Observable{
    @Test
    public void inputTestTwo() throws IOException {
        List<Player> playerList=new ArrayList<Player>();
        playerList.add(new Player("player1", false));
        playerList.add(new Player("player2", true));
        GameController testController = new GameController(playerList, false, new LobbyTest(null));
        Board gameBoard = testController.getBoard();
        File commands = new File("src/test/java/Controller/controllerTestConf2");
        Scanner commandScanner = new Scanner(commands);
        while(commandScanner.hasNextLine()){
            setChanged();
            System.out.println("current player: " + testController.getCurrentPlayer().getNickname());
            try {
                testController.update(commandScanner.nextLine());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println();
        }
    }
    @Test
    public void inputTestThree() throws IOException {
        List<Player> playerList=new ArrayList<Player>();
        playerList.add(new Player("player1", false));
        playerList.add(new Player("player2", true));
        playerList.add(new Player("player3", false));
        GameController testController = new GameController(playerList, false, new LobbyTest(null));
        Board gameBoard = testController.getBoard();
        File commands = new File("src/test/java/Controller/controllerTestConf2");
        Scanner commandScanner = new Scanner(commands);
        while(commandScanner.hasNextLine()){
            setChanged();
            System.out.println("current player: " + testController.getCurrentPlayer().getNickname());
            try {
                testController.update(commandScanner.nextLine());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println();
        }
    }

    @Test
    public void inputTestFour() throws IOException {
        List<Player> playerList=new ArrayList<Player>();
        playerList.add(new Player("player1", true));
        playerList.add(new Player("player2", false));
        playerList.add(new Player("player3", false));
        playerList.add(new Player("player4", false));
        GameController testController = new GameController(playerList, false, new LobbyTest(null));
        Board gameBoard = testController.getBoard();
        File commands = new File("src/test/java/Controller/controllerTestConf2");
        Scanner commandScanner = new Scanner(commands);
        while(commandScanner.hasNextLine()){
            setChanged();
            System.out.println("current player: " + testController.getCurrentPlayer().getNickname());
            try {
                testController.update(commandScanner.nextLine());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println();
        }
    }
}
