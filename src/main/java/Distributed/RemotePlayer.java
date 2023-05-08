package Distributed;

import Model.Player;

public abstract class RemotePlayer {
    protected RemoteHandler remoteHandler;
    private Player modelPlayer;
    private String nickname;
    private boolean owner;

    public RemotePlayer(){
        modelPlayer = new Player("Nico", true, null);
        this.nickname = "Ale";
    }

    public Player getModelPlayer() {
        return modelPlayer;
    }

    public String getNickname() {
        return nickname;
    }

    public RemoteHandler getHandler() {
        return remoteHandler;
    }

    public boolean isOwner() {
        return owner;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setModelPlayer(Player modelPlayer) {
        this.modelPlayer = modelPlayer;
    }

    public void setOwner(boolean owner) { this.owner = owner; }
    public abstract void setClientID(Integer ID);
    public abstract Integer getClientID();
}
