package Distributed;

import Controller.GameController;
import Distributed.ClientRMI.Client;
import Distributed.ServerSocket.ClientHandlerSocket;
import Model.Player;

import java.io.Serializable;
import java.net.Socket;

import static Distributed.States.INIT;

//TODO MIGHT BE A GOOD IDEA TO MAKE IT ABSTRACT
public class RemotePlayer implements Serializable {
    private Player modelPlayer;
    private ConnectionType type;
    private String nickname;
    private States state;
    private GameController controller;
    private boolean owner;

    public RemotePlayer(ConnectionType type){
        this.type = type;
        this.state = INIT;
    }

    public RemotePlayer(Socket socket, ClientHandlerSocket remoteHandler, ConnectionType type){
    }

    public Player getModelPlayer() {
        return modelPlayer;
    }

    public String getNickname() {
        return nickname;
    }

    public void setAsChair() {
        owner = true;
    }

    public boolean isOwner() {
        return owner;
    }

    public void setOwner(boolean owner) { this.owner = owner; }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setModelPlayer(Player modelPlayer) {
        this.modelPlayer = modelPlayer;
    }

    public void update(){}

    public void setState(States state) {
        this.state = state;
    }
    public States getState() {
        return state;
    }

    public void setController(GameController controller) {
        this.controller = controller;
    }

    public GameController getController() {
        return controller;
    }

    public ConnectionType getType() {
        return type;
    }
}
