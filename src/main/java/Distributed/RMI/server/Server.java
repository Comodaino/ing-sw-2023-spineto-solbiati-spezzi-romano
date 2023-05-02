package Distributed.RMI.server;

import Distributed.RMI.client.Client;
import Distributed.RemoteHandler;

import java.rmi.*;

public interface Server extends Remote {
    /**
     * Registers a client to the server
     * Adds a client to a new lobby (if there is not any lobby open) and creates a model and a controller for that lobby
     * @param client the client to register
     */
    public void register(Client client) throws RemoteException;

    /**
     * Deletes a client from the server
     * @param client the client to delete
     */
    public void leave(Client client) throws RemoteException;

    /**
     * Notifies the server that a client has made his move
     * @param o the ClientHandler which handle the client who generated the event
     * @param arg the move made by the client
     */
    //TODO: IMPLEMENTS THE SAME METHOD IN CONTROLLER with ClientHandlerRMI instead of RemoteHandler
    public void update(RemoteHandler o, Object arg) throws RemoteException;
}