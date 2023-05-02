package Distributed;

import Model.Player;
public abstract class RemotePlayer {
    protected RemoteHandler remoteHandler;
    private Player modelPlayer;
    private String nickname;
    private boolean chair;

    public RemotePlayer(){
        modelPlayer = new Player("Nico", true, null);
        this.nickname = "Nick ;)";
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
