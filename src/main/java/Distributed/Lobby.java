package Distributed;

import Controller.GameControllerSocket;
import Distributed.ServerSocket.SocketPlayer;
import Distributed.ServerSocket.States;
import Model.Player;

import java.util.ArrayList;
import java.util.List;

import static Distributed.ServerSocket.States.CLOSE;
import static Distributed.ServerSocket.States.END;

public class Lobby {
    private final List<RemotePlayer> lp;
    private boolean open;
    private boolean firstMatch;
    public Lobby(){
        this.lp = new ArrayList<RemotePlayer>();
        this.firstMatch = false;
    }

    /**
     *
     * @param p
     * @return returns true if there are 4 player in the lobby
     */
    public void addPlayer(SocketPlayer p){
        if(lp.isEmpty()) p.setAsChair();
        lp.add(p);
        open = lp.size()==4;

    }

    public void setFirstMatch(boolean firstMatch) {
        this.firstMatch = firstMatch;
    }

    public boolean isOpen() {
        return open;
    }

    public List<RemotePlayer> getListOfPlayers() {
        return lp;
    }
    public void startGame(){
        List<Player> modelPlayerList = new ArrayList<Player>();
        GameControllerSocket tmpControllerSocket = new GameControllerSocket(modelPlayerList, firstMatch);
        //TODO GameControllerRMI tmpControllerRMI = new GameControllerRMI(modelPlayerList, firstMatch);
        for(RemotePlayer p: lp){
            Player tmpPlayer = new Player(p.getNickname(),p.isChair(), p);
            modelPlayerList.add(tmpPlayer);
            p.setModelPlayer(tmpPlayer);
            p.getHandler().setState(States.PLAY);
            if(p.getHandler().getType().equals(HandlersType.Socket)) p.getHandler().setGameController(tmpControllerSocket);
               //TODO else p.getHandler().setGameController(tmpControllerRMI);
        }
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
}
