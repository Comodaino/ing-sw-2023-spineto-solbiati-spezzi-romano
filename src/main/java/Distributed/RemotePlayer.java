package Distributed;

import Controller.GameController;
import Distributed.ClientRMI.Client;
import Model.BoardView;
import Model.Player;

import java.io.IOException;
import java.io.Serializable;

import static Distributed.States.INIT;

public abstract class RemotePlayer implements Serializable {
    private Player modelPlayer;
    private ConnectionType type;
    private String nickname;
    private States state;
    private GameController controller;
    private boolean owner;
    private boolean connected;

    public RemotePlayer(ConnectionType type){
        this.type = type;
        this.owner = false;
        this.connected = true;
        this.state = INIT;
    }

    public void update(BoardView boardView) throws IOException, InterruptedException { }
    public void message(String arg) {}
    public void endMatch() {}


    //SETTER AND GETTER METHODS
    public void setModelPlayer(Player modelPlayer) {
        this.modelPlayer = modelPlayer;
    }
    public Player getModelPlayer() {
        return modelPlayer;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public String getNickname() {
        return nickname;
    }
    public ConnectionType getConnectionType() { return this.type; }
    public ConnectionType getType() {
        return type;
    }
    public void setOwner() { owner = true;  }
    public boolean isOwner() {
        return owner;
    }
    public void setState(States state) {
        this.state = state;
    }
    public abstract States getState();
    public void setController(GameController controller) {
        this.controller = controller;
    }
    public GameController getController() {
        return controller;
    }
    public Client getClient() { return null; }
    public void setConnected(boolean status) {this.connected = status;}
    public boolean isConnected() { return connected; }
    public abstract void reconnect(Client client, int id);
}
