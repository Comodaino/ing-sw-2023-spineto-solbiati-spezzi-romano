package Distributed;

import Controller.GameController;
import Model.BoardView;
import Model.Player;

import java.util.ArrayList;
import java.util.List;


public class Lobby {
    private final List<RemotePlayer> lp;
    private Integer ID;
    private boolean open;
    private boolean firstMatch;
    private BoardView boardView;
    private GameController controller;
    public Lobby(){
        this.lp = new ArrayList<RemotePlayer>();
        this.firstMatch = false;
        this.ID = null;
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
    public void startGame(){
        List<Player> modelPlayerList = new ArrayList<Player>();
        //TODO NEED TO DECIDE BETWEEN TWO CONTROLLERS OR ONE
        for(RemotePlayer p: lp){
            Player tmpPlayer = new Player(p.getNickname(),p.isOwner(), p);
            modelPlayerList.add(tmpPlayer);
            p.setModelPlayer(tmpPlayer);
            p.setState(States.PLAY);
        }
        controller = new GameController(modelPlayerList, firstMatch);
        //TODO GameControllerRMI tmpControllerRMI = new GameControllerRMI(modelPlayerList, firstMatch);
        for(RemotePlayer p: lp) {
            if (p.getType().equals(ConnectionType.SOCKET))
                p.setController(controller);
            //TODO else p.getHandler().setGameController(tmpControllerRMI);
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
    }

    public Object getBoardView() {
        return boardView;
    }
    public void setID(Integer i) { this.ID = i; }
    public Integer getID() { return this.ID; }
    public void sendMessage(RemotePlayer player, String message){
        for(RemotePlayer p: lp){
            //TODO IMPLEMENT
        }
    }
}
