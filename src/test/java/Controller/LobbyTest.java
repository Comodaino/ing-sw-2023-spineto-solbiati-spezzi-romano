package Controller;

import Distributed.Lobby;
import Distributed.ServerApp;
import Distributed.ServerRMI.Server;

public class LobbyTest extends Lobby {
    public LobbyTest(ServerApp server) {
        super(server);
        if(server == null){
            return;
        }
    }

    @Override
    public boolean getPlay() {
        return true;
    }
}
