package Controller;

import Distributed.Lobby;
import Distributed.ServerRMI.Server;

public class LobbyTest extends Lobby {
    public LobbyTest(Server server) {
        super(server);
        if(server == null){
            return;
        }
    }
}
