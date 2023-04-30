package Distributed;

import Distributed.ServerSocket.States;

public abstract class RemoteHandler {
    protected Lobby lobby;
    protected States state;
    /**
     *
     * @param input
     * @return return true if the input nickname is avaible
     */
    protected boolean nicknameChecker(String input) {
        for(RemotePlayer p: lobby.getListOfPlayers()){
            if(p.getNickname().equals(input)) return false;
        }
        return true;
    }

    public void setState(States state) {
        this.state = state;
    }
}
