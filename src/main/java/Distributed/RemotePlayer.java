package Distributed;

import Controller.GameController;
import Model.Player;

import static Distributed.States.INIT;

//TODO MIGHT BE A GOOD IDEA TO MAKE IT ABSTRACT
public class RemotePlayer {
    private Player modelPlayer;
    private ConnectionType type;
    private String nickname;
    private States state;
    private GameController controller;
    private boolean owner;

    public RemotePlayer(ConnectionType type){
        this.type = type;
        modelPlayer = new Player("Nico", true, null);
        this.nickname = "Ale";
        this.state = INIT;
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
}
