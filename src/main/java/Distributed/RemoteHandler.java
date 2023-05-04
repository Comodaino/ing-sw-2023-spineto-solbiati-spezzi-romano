package Distributed;

import Controller.GameController;
import Distributed.ServerSocket.States;

public abstract class RemoteHandler {
    protected Lobby lobby;
    protected States state;
    protected GameController gameController;
    protected HandlersType type;
    /**
     *
     * @param input
     * @return return true if the input nickname is available
     */
    protected boolean nicknameChecker(String input) {
        for(RemotePlayer p: lobby.getListOfPlayers()){
            if(p.getNickname().equals(input)) return false;
        }
        return true;
    }
    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }
    public GameController getGameController() { return this.gameController; }
    public void setState(States state) {
        this.state = state;
    }
    public Lobby getLobby() { return this.lobby; }
    public HandlersType getType() {
        return type;
    }
    public void endCommand(){ state=States.WAIT; }
}
