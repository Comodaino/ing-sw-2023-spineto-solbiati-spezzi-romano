package Distributed.ServerRMI;

import Distributed.ClientRMI.Client;
import Distributed.ConnectionType;
import Distributed.RemotePlayer;
import Distributed.States;
import Model.BoardView;

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

    /**
     * This method sets the client state to END when the match is finished.
     * @author Nicolò
     */
    @Override
    public void endMatch() {
        try {
            client.setState(States.END);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method invokes the Client update() method which invokes the View update.
     * It is used to update the Board through the serializable BoardView.
     * @param boardView the serializable BoardView
     * @author Nicolò
     */
    @Override
    public void update(BoardView boardView) throws IOException, InterruptedException {
        client.update(boardView, null);
    }

    /**
     * This method reconnects a Client to the game it was playing.
     * It creates a link between the "new" Client and the old one through their common RMIPlayer: the new client is set as
     * a parameter of the RMIPlayer and the state and the lobby id of the new client are setted taking the values from the RMIPlayer.
     * @param client the "new" Client
     * @param id the Lobby ID
     * @author Nicolò
     */
    @Override
    public void reconnect(Client client, int id) {
        this.client = client;
        try {
            this.client.setState(this.state);
            this.client.setLobbyID(id);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

    }

    //SETTER AND GETTER METHODS
    @Override
    public Client getClient() { return this.client; }

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
    public States getState() { return this.state; }
}