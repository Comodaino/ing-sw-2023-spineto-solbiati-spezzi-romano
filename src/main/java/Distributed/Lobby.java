package Distributed;

import Controller.GameController;
import Distributed.ServerRMI.Server;
import Model.BoardView;
import Model.Player;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;


public class Lobby {
    private final List<RemotePlayer> lp;
    private Integer ID;
    private boolean open;
    private boolean firstMatch;
    private BoardView boardView;
    private GameController controller;
    private ServerApp serverApp;
    private Server server; //TODO delete after unification of ServerImpl and ServerApp
    private boolean playing;

    public Lobby(ServerApp serverApp) {
        this.lp = new ArrayList<RemotePlayer>();
        this.firstMatch = false;
        this.ID = null;
        this.serverApp = serverApp;

        try {
            controller = new GameController(false, this);
            System.out.println("Created lobby's controller");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.boardView = controller.getBoardView();
        System.out.println("BOARD: " + this.boardView);


        this.open = true;
    }


    /**
     * Adds a player to the lobby
     * @param p tha player that needs to be added
     */
    public void addPlayer(RemotePlayer p) throws IOException, InterruptedException {
        if (open) {
            if (lp.isEmpty()) {
                p.setOwner(); //the first player to join the lobby become the owner
            }
            lp.add(p);
            p.setState(States.WAIT);
            p.setController(controller);
            p.update(boardView);
            controller.addPlayer(new Player(p.getNickname()));
            if (lp.size() == 4) this.open = false;
        }
    }

    private boolean closeLobby() {
        if (lp.size() >= 2) {
            this.open = false;
            return true; //lobby closed
        }
        return false; //lobby not closed
    }


    public void startGame() throws RemoteException {
        this.open=false;
        controller.startGame();
        for(RemotePlayer p : lp) {

            p.setState(States.PLAY);
            if(p.getConnectionType().equals(ConnectionType.RMI)){
                p.getClient().setState(States.PLAY);
            }
        }
        this.open = false;
        this.playing = true;
    }

    public void endMatch() {
        this.playing = false;
        for (RemotePlayer p : lp) {
            p.setState(States.END);
            p.endMatch();
        }
    }

    public void close() {
        for (RemotePlayer p : lp) {
            p.setState(States.CLOSE);
        }
        serverApp.removeLobby(this);
    }

    public void updateAll() throws IOException, InterruptedException {
        for (RemotePlayer p : lp) {
            if(p.isConnected()) p.update(boardView);
        }
    }

    public void setFirstMatch(boolean firstMatch) {
        this.firstMatch = firstMatch;
    }

    public boolean isFirstMatch() {
        return firstMatch;
    }

    public boolean isOpen() {
        return open;
    }

    public List<RemotePlayer> getListOfPlayers() {
        return lp;
    }
    public BoardView getBoardView() {
        return boardView;
    }

    public void setID(Integer i) {
        this.ID = i;
    }

    public Integer getID() {
        return this.ID;
    }
    public GameController getController() {
        return controller;
    }

    /**
     * Checks and eventually updates if the lobby should be open
     */
    public void checkOpen() {
        if(this.lp.size() == 4) this.open= false;
    }

    public boolean getPlay() {
        return playing;
    }
}
