package Distributed;

import Controller.GameController;

import java.io.IOException;


/**
 * Abstract super class for client handlers
 */
public abstract class RemoteHandler {
    protected Lobby lobby;
    protected States state;
    protected GameController gameController;
    protected ConnectionType type;
    protected ServerApp serverApp;

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }
    public GameController getGameController() { return this.gameController; }
    public void setState(States state) {
        this.state = state;
    }
    public Lobby getLobby() { return this.lobby; }
    public ConnectionType getType() {
        return type;
    }
    /**
     * sends the player to the lobby
     */
    public void endCommand(){ state=States.WAIT; }

    /**
     * Writes the serializable ModelView to the client's socket
     * @throws IOException
     */
    public void update() throws IOException {}

    /**
     * Sends a chat message to the client
     * @param arg
     */
    public void message(String arg) {}
}