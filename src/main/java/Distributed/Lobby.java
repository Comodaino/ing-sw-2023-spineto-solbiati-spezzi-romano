package Distributed;

import Controller.GameControllerSocket;
import Distributed.ServerSocket.SocketPlayer;
import Distributed.ServerSocket.States;
import Model.Player;

import java.util.ArrayList;
import java.util.List;

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
    private void addPlayer(SocketPlayer p){
        if(lp.isEmpty()) p.setAsChair();
        lp.add(p);
        open = lp.size()==4;
    }

    public boolean isOpen() {
        return open;
    }

    public List<RemotePlayer> getListOfPlayers() {
        return lp;
    }
    public GameControllerSocket startGame(){
        List<Player> modelPlayerList = new ArrayList<Player>();
        for(RemotePlayer p: lp){
            Player tmpPlayer = new Player(p.getNickname(),p.isChair(), p);
            modelPlayerList.add(tmpPlayer);
            p.setModelPlayer(tmpPlayer);
            p.getHandler().setState(States.PLAY);
        }
        return new GameControllerSocket(modelPlayerList, firstMatch);
    }
}
