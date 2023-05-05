package Distributed;

import Model.Player;

//TODO MIGHT BE A GOOD IDEA TO MAKE IT ABSTRACT
public class RemotePlayer {
    private Player modelPlayer;
    private ConnectionType type;
    private String nickname;
    private boolean chair;

    public RemotePlayer(ConnectionType type){
        this.type = type
        modelPlayer = new Player("Nico", true, null);
        this.nickname = "Ale";
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

    public boolean isChair() {
        return chair;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setModelPlayer(Player modelPlayer) {
        this.modelPlayer = modelPlayer;
    }
    public void update(){}

    public ConnectionType getType() {
        return type;
    }
}
