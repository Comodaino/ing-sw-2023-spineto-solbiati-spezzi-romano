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
        GameController testController = new GameController(false, new LobbyTest(null));
        testController.addPlayer(new Player("player1"));
        testController.addPlayer(new Player("player2"));
        Board gameBoard = testController.getBoard();
        testController.startGame();
        File commands = new File("src/test/java/Controller/controllerTestConf2");
        Scanner commandScanner = new Scanner(commands);
        while(commandScanner.hasNextLine()){
            setChanged();
            //System.out.println("current player: " + testController.getCurrentPlayer().getNickname());
            try {


                String[] arrayS = commandScanner.nextLine().split(" ");
                String tmp = arrayS[0] + " " + gameBoard.getCurrentPlayer().getNickname();
                for(int i=1; i< arrayS.length; i++){
                    tmp = tmp + " " + arrayS[i];
                }

                testController.update(tmp);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println();
        }
    }
    @Test
    public void inputTestThree() throws IOException {
        List<Player> playerList=new ArrayList<Player>();
        GameController testController = new GameController(false, new LobbyTest(null));
        testController.addPlayer(new Player("player1"));
        testController.addPlayer(new Player("player2"));
        testController.addPlayer(new Player("player3"));

        Board gameBoard = testController.getBoard();
        testController.startGame();
        File commands = new File("src/test/java/Controller/controllerTestConf3");
        Scanner commandScanner = new Scanner(commands);
        while(commandScanner.hasNextLine()){
            System.out.println("ziopera");
            setChanged();
            //System.out.println("current player: " + testController.getCurrentPlayer().getNickname());
            try {
                String[] arrayS = commandScanner.nextLine().split(" ");
                String tmp = arrayS[0] + " " + gameBoard.getCurrentPlayer().getNickname();
                for(int i=1; i< arrayS.length; i++){
                    tmp = tmp + " " + arrayS[i];
                }

                testController.update(tmp);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println();
        }
    }

    @Test
    public void inputTestFour() throws IOException {
        List<Player> playerList=new ArrayList<Player>();
        GameController testController = new GameController(false, new LobbyTest(null));
        testController.addPlayer(new Player("player1"));
        testController.addPlayer(new Player("player2"));
        testController.addPlayer(new Player("player3"));
        testController.addPlayer(new Player("player4"));

        Board gameBoard = testController.getBoard();
        testController.startGame();
        File commands = new File("src/test/java/Controller/controllerTestConf4");
        Scanner commandScanner = new Scanner(commands);
        while(commandScanner.hasNextLine()){
            setChanged();

            //System.out.println("current player: " + testController.getCurrentPlayer().getNickname());
            try {
                String[] arrayS = commandScanner.nextLine().split(" ");
                String tmp = arrayS[0] + " " + gameBoard.getCurrentPlayer().getNickname();
                for(int i=1; i< arrayS.length; i++){
                    tmp = tmp + " " + arrayS[i];
                }

                testController.update(tmp);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println();
        }
    }
}
