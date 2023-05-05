package Distributed;

import Controller.GameControllerSocket;
import Distributed.ServerSocket.States;
import Model.BoardView;
import Model.Player;

import java.util.ArrayList;
import java.util.List;

import static Distributed.ServerSocket.States.CLOSE;
import static Distributed.ServerSocket.States.END;

public class Lobby {
    private final List<RemotePlayer> lp;
    private Integer ID;
    private boolean open;
    private boolean firstMatch;
    private BoardView boardView;
    public Lobby(){
        this.lp = new ArrayList<RemotePlayer>();
        this.firstMatch = false;
        this.ID = null;
        this.open = true;
    }

    /**
     *
     * @param p
     * @return returns true if there are 4 player in the lobby
     */
    public void addPlayer(RemotePlayer p){
        if(lp.isEmpty()) p.setAsChair();
        lp.add(p);
        if(lp.size()==4) this.open = false;
    }

    public void closeLobby() {
        if(lp.size()>=2){
            this.open = false;
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
    public void startGame(){
        List<Player> modelPlayerList = new ArrayList<Player>();
        //TODO NEED TO DECIDE BETWEEN TWO CONTROLLERS OR ONE
        for(RemotePlayer p: lp){
            Player tmpPlayer = new Player(p.getNickname(),p.isChair(), p);
            modelPlayerList.add(tmpPlayer);
            p.setModelPlayer(tmpPlayer);
            p.getHandler().setState(States.PLAY);
        }
        GameControllerSocket tmpControllerSocket = new GameControllerSocket(modelPlayerList, firstMatch);
        //TODO GameControllerRMI tmpControllerRMI = new GameControllerRMI(modelPlayerList, firstMatch);
        for(RemotePlayer p: lp) {
            if (p.getHandler().getType().equals(ConnectionType.Socket))
                p.getHandler().setGameController(tmpControllerSocket);
            //TODO else p.getHandler().setGameController(tmpControllerRMI);
        }
        boardView = tmpControllerSocket.getBoardView();
    }
    public void endMatch() {
        for(RemotePlayer p: lp){
            p.getHandler().setState(END);
        }
    }
    public void close() {
        for(RemotePlayer p: lp){
            p.getHandler().setState(CLOSE);
        }
    }

    public Object getBoardView() {
        return boardView;
    }
    public void setID(Integer i) { this.ID = i; }
    public Integer getID() { return this.ID; }
}
