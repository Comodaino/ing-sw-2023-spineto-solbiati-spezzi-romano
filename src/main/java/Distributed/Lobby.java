package Distributed;

import Controller.GameController;
import Distributed.ServerSocket.SocketPlayer;
import Model.BoardView;
import Model.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static Distributed.States.CLOSE;
import static Distributed.States.END;

public class Lobby {
    private final List<RemotePlayer> lp;
    private Integer ID;
    private boolean open;
    private boolean firstMatch;
    private BoardView boardView;
    private ServerApp server;
    public Lobby(ServerApp server){
        this.lp = new ArrayList<RemotePlayer>();
        this.firstMatch = false;
        this.ID = null;
        this.open = true;
        this.server = server;
    }

    /**
     * adds a RemotePlayer to the lobby
     * @param p
     * @return returns true if there are 4 player in the lobby
     */
    public void addPlayer(RemotePlayer p){
        if(lp.isEmpty()) p.setAsChair();
        lp.add(p);
        open = !(lp.size()==4);
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

    /**
     * Creates a controller and starts the game
     */
    public void startGame(){
        List<Player> modelPlayerList = new ArrayList<Player>();
        for(RemotePlayer p: lp){
            Player tmpPlayer = new Player(p.getNickname(),p.isChair(), p);
            modelPlayerList.add(tmpPlayer);
            p.setModelPlayer(tmpPlayer);
            p.getHandler().setState(States.PLAY);
        }
        GameController tmpControllerSocket = new GameController(modelPlayerList, firstMatch);
        for(RemotePlayer p: lp) {
            if (p.getHandler().getType().equals(HandlersType.Socket))
                p.getHandler().setGameController(tmpControllerSocket);
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
        server.removeLobby(this);
    }
    public Object getBoardView() {
        return boardView;
    }
    public void setID(Integer i) { this.ID = i; }
    public Integer getID() { return this.ID; }
}
