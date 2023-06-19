package Distributed.ServerRMI;

import Distributed.ClientRMI.Client;
import Distributed.ConnectionType;
import Distributed.RemoteClient;
import Distributed.RemotePlayer;
import Distributed.States;
import Model.BoardView;
import View.State;

import java.io.IOException;
import java.rmi.RemoteException;

/**
 * RMIPlayer is a class which extends RemotePlayer.
 * It represents the RMI remote player.
 * @author Nicolò
 */
public class RMIPlayer extends RemotePlayer {
    Client client;
    private States state;

    public RMIPlayer(Client client){
        super(ConnectionType.RMI);
        this.client = client;
    }

    @Override
    public void endMatch() {
        try {
            client.setState(States.END);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(BoardView boardView) throws IOException, InterruptedException {
        client.update(boardView, null);
    }

    @Override
    public Client getClient() { return this.client; }

    @Override
    public void reconnect(Client client, int id) {
        this.client=client;
        try {
            this.client.setState(this.state);
            this.client.setLobbyID(id);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void setState(States state) {
        try {
            this.state = state;
            this.client.setState(state);
            System.out.println("STATE CHANGED TO: " + state + this.state + getClient().getState());
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public States getState() {
        return this.state;
    }
}