package Distributed;

import Controller.GameController;
import Model.BoardView;
import Model.Player;

import java.util.ArrayList;
import java.util.List;

import static Distributed.States.CLOSE;
import static Distributed.States.END;

public class Lobby {
    private final List<RemotePlayer> lp;
    private Integer serialNumber;
    private boolean open;
    private boolean firstMatch;
    private BoardView boardView;
    public Lobby(){
        this.lp = new ArrayList<RemotePlayer>();
        this.firstMatch = false;
        this.serialNumber = null;
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
    public void startGame(){
        List<Player> modelPlayerList = new ArrayList<Player>();
        //TODO NEED TO DECIDE BETWEEN TWO CONTROLLERS OR ONE
        for(RemotePlayer p: lp){
            Player tmpPlayer = new Player(p.getNickname(),p.isChair(), p);
            modelPlayerList.add(tmpPlayer);
            p.setModelPlayer(tmpPlayer);
            p.getHandler().setState(States.PLAY);
        }
        GameController tmpControllerSocket = new GameController(modelPlayerList, firstMatch);
        //TODO GameControllerRMI tmpControllerRMI = new GameControllerRMI(modelPlayerList, firstMatch);
        for(RemotePlayer p: lp) {
            if (p.getHandler().getType().equals(HandlersType.Socket))
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
    public void setSerialNumber(Integer i) { this.serialNumber = i; }
    public Integer getSerialNumber() { return this.serialNumber; }
}
