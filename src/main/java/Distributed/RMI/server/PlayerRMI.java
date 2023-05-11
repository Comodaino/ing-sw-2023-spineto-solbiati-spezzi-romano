package Distributed.RMI.server;

import Distributed.RemotePlayer;

public class PlayerRMI extends RemotePlayer {
    private Integer clientID;

    @Override
    public void setClientID(Integer ID) {
        this.clientID = ID;
    }

    @Override
    public Integer getClientID() {
        return this.clientID;
    }
}