package Distributed;

import Controller.GameController;
import Distributed.ServerSocket.ClientHandlerSocket;
import Model.Player;

import java.io.Serializable;
import java.net.Socket;

import static Distributed.States.INIT;

public class RemotePlayer implements Serializable {
    private Player modelPlayer;
    private ConnectionType type;
    private String nickname;
    private States state;
    private GameController controller;
    private boolean owner;

    public RemotePlayer(ConnectionType type){
        this.type = type;
        this.owner = false;
        modelPlayer = new Player("Nico", true, null);
        this.nickname = "Ale";
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

    public void setOwner() {
        owner = true;
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
    public void update(){}
    public void setState(States state) {
        this.state = state;
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

    public States getState() {
        return state;
    }
}
