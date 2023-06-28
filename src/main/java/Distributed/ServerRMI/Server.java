package Distributed.ServerRMI;

import Distributed.ClientRMI.Client;

import java.rmi.*;

/**
 * Server is an interface which extends Remote and represents a "local" (client side) proxy of the RMI Server, called stub.
 * It represents the server from the point of view of the client and offers a list of methods which can be called by the client.
 * @author Nicolò
 */
public interface Server extends Remote {
    public void handler(Client client, String arg) throws RemoteException;
    public boolean beat() throws RemoteException;
}