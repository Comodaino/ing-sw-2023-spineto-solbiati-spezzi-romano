package Distributed.ServerRMI;

import Distributed.ClientRMI.Client;
import Distributed.ServerApp;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.*;
import java.rmi.server.UnicastRemoteObject;

/**
 * ServerImpl is a class which extends UnicastRemoteObject and implements Server
 * It implements the RMI Server and handles all the RMI Clients which connects to it
 * @author Nicolò
 */
public class ServerImpl extends UnicastRemoteObject implements Server {
    private ServerApp serverApp;
    private Registry registry;

    public ServerImpl(ServerApp serverApp) throws RemoteException {
        this.registry = null;
        this.serverApp = serverApp;
    }

    /**
     * This method creates an instance of ServerImpl (RMI server) and starts the server through the method start().
     * @param serverApp the main server
     * @author Nicolò
     */
    public void execute(ServerApp serverApp) {
        ServerImpl server = null;
        try {
            server = new ServerImpl(serverApp);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        server.start();
    }


    /**
     * This method starts the RMI Server, creating an RMI Registry, binding the name ("ServerRMI") through which the clients can
     * connect to the server with the instance of this object (ServerImpls).
     * @author Nicolò
     */
    public void start() {
        try {
            registry = LocateRegistry.createRegistry(1099); //Creates RMI Registry
            registry.bind("ServerRMI", this); //Binds the name of the server with this object
        } catch (RemoteException | AlreadyBoundException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Server bound and ready");
    }

    /**
     * This method is invoked by the Client method println(String arg).
     * It invokes the ServerApp handler() method.
     * @param client the client who invokes the method
     * @param arg the input written by the client
     * @author Nicolò
     */
    @Override
    public void handler(Client client, String arg) throws RemoteException{
        serverApp.handler(client, arg);
    }
    @Override
    public boolean beat() throws RemoteException { return true; }
}