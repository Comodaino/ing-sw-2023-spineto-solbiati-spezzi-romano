package Distributed;

import Controller.GameController;
import Distributed.ClientRMI.Client;
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
    private Server server;
    private List<Client> clientsRMI;
    public Lobby(ServerApp serverApp){
        this.lp = new ArrayList<RemotePlayer>();
        this.clientsRMI = new ArrayList<>();
        this.firstMatch = false;
        this.ID = null;
        this.serverApp = serverApp;
        this.open = true;
    }

    public Lobby(Server server){ //TODO delete after the unification of ServerImpl and ServerApp
        this.lp = new ArrayList<RemotePlayer>();
        this.clientsRMI = new ArrayList<>();
        this.firstMatch = false;
        this.ID = null;
        this.server = server;
        this.open = true;
    }

    public void addPlayer(RemotePlayer p){
        if(open){
            if(lp.isEmpty()){
                p.setOwner(); //the first player to join the lobby become the owner
            }
            lp.add(p);
            if(lp.size()==4) this.open = false;
        }
    }

    public void addClientRMI(Client client) {
        this.clientsRMI.add(client);
    }

    public boolean closeLobby() {
        if(lp.size()>=2){
            this.open = false;
            return true; //lobby closed
        }
        return false; //lobby not closed
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
    public List<Client> getListOfClients() { return clientsRMI; }
    public void startGame() {
        if(lp.size()>=2) {
            List<Player> modelPlayerList = new ArrayList<Player>();
            for(RemotePlayer p: lp){
                System.out.println("ué");
                Player tmpPlayer = new Player(p.getNickname(),p.isOwner(), p);
                modelPlayerList.add(tmpPlayer);
                p.setModelPlayer(tmpPlayer);
                p.setState(States.PLAY);
            }
            for(Client c: clientsRMI){
                try {
                    c.setState(States.PLAY);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }

            try {
                controller = new GameController(modelPlayerList, firstMatch);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            for(RemotePlayer p: lp) {
                p.setController(controller);
            }
            boardView = controller.getBoardView();
            this.open = false;
        }
    }
    public void endMatch() {
        for(RemotePlayer p: lp){
            p.setState(States.END);
        }
        for(Client c: clientsRMI){
            try {
                c.setState(States.END);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void close() {
        for(RemotePlayer p: lp){
            p.setState(States.CLOSE);
        }
        for(Client c: clientsRMI){
            try {
                c.setState(States.CLOSE);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
        serverApp.removeLobby(this);
    }

    public BoardView getBoardView() {
        return boardView;
    }
    public void setID(Integer i) { this.ID = i; }
    public Integer getID() { return this.ID; }
    public void sendMessage(RemotePlayer player, String message){
        System.out.println("sending: " + message);
        for(RemotePlayer p: lp){
            p.message("[" + player.getNickname() + "] : " + message);
        }
    }

    public GameController getController() { return controller; }
}
