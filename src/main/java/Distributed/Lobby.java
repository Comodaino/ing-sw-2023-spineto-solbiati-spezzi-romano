package Distributed;

import Controller.GameController;
import Model.BoardView;
import Model.Player;

import java.io.IOException;
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
    public Lobby(ServerApp serverApp){
        this.lp = new ArrayList<RemotePlayer>();
        this.firstMatch = false;
        this.ID = null;
        this.serverApp = serverApp;
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
    public void startGame() throws IOException {
        List<Player> modelPlayerList = new ArrayList<Player>();
        for(RemotePlayer p: lp){
            Player tmpPlayer = new Player(p.getNickname(),p.isOwner());
            modelPlayerList.add(tmpPlayer);
            p.setModelPlayer(tmpPlayer);
            p.setState(States.PLAY);
        }
        controller = new GameController(modelPlayerList, firstMatch, this);
        for(RemotePlayer p: lp) {
                p.setController(controller);
        }
        boardView = controller.getBoardView();
    }
    public void endMatch() {
        for(RemotePlayer p: lp){
            p.setState(States.END);
        }
    }
    public void close() {
        for(RemotePlayer p: lp){
            p.setState(States.CLOSE);
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
