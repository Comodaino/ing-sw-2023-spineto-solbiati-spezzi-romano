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

    public Lobby(ServerApp serverApp) {
        this.lp = new ArrayList<RemotePlayer>();
        this.firstMatch = false;
        this.ID = null;
        this.serverApp = serverApp;
        this.open = true;
    }

    public Lobby(Server server) { //TODO delete after unification of ServerImpl and ServerApp
        this.lp = new ArrayList<RemotePlayer>();
        this.firstMatch = false;
        this.ID = null;
        this.server = server;
        this.open = true;
    }

    /**
     * Adds a player to the lobby
     * @param p tha player that needs to be added
     */
    public void addPlayer(RemotePlayer p) {
        if (open) {
            if (lp.isEmpty()) {
                p.setOwner(); //the first player to join the lobby become the owner
            }
            lp.add(p);
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
        List<Player> modelPlayerList = new ArrayList<Player>();
        for(RemotePlayer p : lp) {
            Player tmpPlayer = new Player(p.getNickname(), p.isOwner());
            modelPlayerList.add(tmpPlayer);
            p.setModelPlayer(tmpPlayer);
            p.setState(States.PLAY);
        }
        try {
            controller = new GameController(modelPlayerList, firstMatch, this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for(RemotePlayer p : lp) {
            p.setController(controller);
            p.setState(States.PLAY);
            if(p.getConnectionType().equals(ConnectionType.RMI)){
                p.getClient().setState(States.PLAY);
            }
        }
        boardView = controller.getBoardView();
        this.open = false;
    }

    public void endMatch() {
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

}
