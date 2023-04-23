package Distributed;

import Model.Player;

import java.net.Socket;

public abstract class RemotePlayer {
    private Player modelPlayer;
    private String nickname;
    private boolean chair;

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
}
