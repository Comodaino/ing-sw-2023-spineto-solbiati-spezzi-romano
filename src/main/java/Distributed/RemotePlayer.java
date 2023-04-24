package Distributed;

import Distributed.ServerSocket.ClientHandlerSocket;
import Model.Player;

import java.net.Socket;
import java.util.logging.Handler;

public abstract class RemotePlayer {
    private Player modelPlayer;
    private String nickname;
    private boolean chair;
    private RemoteHandler personalHandler;

    public RemotePlayer(){
        this.modelPlayer=null;
    }
    public Player getModelPlayer() {
        return modelPlayer;
    }

    public String getNickname() {
        return nickname;
    }

    public void setAsChair() {
        chair = true;
    }

    public RemoteHandler getHandler() {
        return personalHandler;
    }

    public boolean isChair() {
        return chair;
    }

    public void setModelPlayer(Player modelPlayer) {
        this.modelPlayer = modelPlayer;
    }
}
