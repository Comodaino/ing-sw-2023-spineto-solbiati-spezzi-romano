package Distributed.ServerRMI;

import Distributed.ClientRMI.Client;
import Distributed.ConnectionType;
import Distributed.RemotePlayer;
import Model.BoardView;

import java.io.IOException;

public class RMIPlayer extends RemotePlayer {
    Client client;

    public RMIPlayer(Client client){
        super(ConnectionType.RMI);
        this.client = client;
    }

    @Override
    public void endMatch() {
        super.endMatch();
    }

    @Override
    public void update(BoardView boardView) throws IOException, InterruptedException {
        client.update(boardView, "");
    }

    @Override
    public Client getClient() { return this.client; }
}
