package Distributed;

import Controller.GameController;

public abstract class RemoteHandler {
    protected Lobby lobby;
    protected States state;
    protected GameController gameController;
    protected ConnectionType type;
    protected ServerApp serverApp;
    /**
     *
     * @param input
     * @return return true if the input nickname is available
     */
    protected boolean nicknameChecker(String input) {
        for(Lobby l: serverApp.getLobbies()){
            for(RemotePlayer p: l.getListOfPlayers()){
                if(p.getNickname().equals(input)) return false;
            }
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
    public ConnectionType getType() {
        return type;
    }
    public void endCommand(){ state=States.WAIT_SETTING; }
    public void update(){}

    public void message(String arg) {
    }
}