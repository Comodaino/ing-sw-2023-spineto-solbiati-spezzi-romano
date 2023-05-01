package Distributed;

import Model.Player;
//TODO MIGHT BE A GOOD IDEA TO MAKE IT ABSTRACT
public abstract class RemotePlayer {
    protected RemoteHandler remoteHandler;
    private Player modelPlayer;
    private String nickname;
    private boolean chair;

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
        return remoteHandler;
    }

    public boolean isChair() {
        return chair;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setModelPlayer(Player modelPlayer) {
        this.modelPlayer = modelPlayer;
    }

}
