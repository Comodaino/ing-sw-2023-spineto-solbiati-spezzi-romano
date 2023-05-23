package Distributed;

import Controller.GameController;
import Model.BoardView;
import Model.Player;

import java.io.IOException;
import java.io.Serializable;

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
        modelPlayer = new Player("Nico", true);
        this.nickname = "Ale";
        this.state = INIT;
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
    public void update(BoardView boardView) throws IOException, InterruptedException {}
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

    public void message(String arg) {}
}
