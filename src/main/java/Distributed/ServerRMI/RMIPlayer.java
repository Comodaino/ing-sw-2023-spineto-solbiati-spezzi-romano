package Distributed.ServerRMI;

import Distributed.ClientRMI.Client;
import Distributed.ConnectionType;
import Distributed.RemotePlayer;
import Model.BoardView;

import java.io.IOException;

/**
 * RMIPlayer is a class which extends RemotePlayer.
 * It represent the RMI remote player.
 * @author Nicolò
 */
public class RMIPlayer extends RemotePlayer {
    Client client;

    public RMIPlayer(Client client){
        super(ConnectionType.RMI);
        this.client = client;
    }

    @Override
    public void endMatch() {
        //TODO implements
    }

    @Override
    public void update(BoardView boardView) throws IOException, InterruptedException {
        client.update(boardView, "");
    }

    @Override
    public Client getClient() { return this.client; }
}