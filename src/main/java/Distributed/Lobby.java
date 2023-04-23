package Distributed;

import Distributed.ServerSocket.SocketPlayer;

import java.util.ArrayList;
import java.util.List;

public class Lobby {
    private List<RemotePlayer> lp;
    private boolean open;
    public Lobby(){
        lp = new ArrayList<RemotePlayer>();
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
}
