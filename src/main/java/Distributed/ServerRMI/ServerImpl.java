package Distributed.ServerRMI;

import Distributed.ClientRMI.Client;
import Distributed.Lobby;
import Distributed.RemotePlayer;
import Distributed.ServerApp;
import Distributed.States;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

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

    //Creates an instance of ServerImpl and starts the server
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
     * It gets the client state and, according to it, it invokes other ServerImpl methods to handle the client;
     * then it update all the players' views.
     * @param client the client who invokes the method
     * @param arg the input written by the client
     * @author Nicolò
     */
    @Override
    public void handler(Client client, String arg) throws RemoteException{
        serverApp.handler(client, arg);
    }

    /**
     * This method is invoked by the method handler(Client c, String s) if the client state is INIT.
     * It invokes the nickname checker and, if the return is positive, register the client to the server,
     * adding it to the list of player of the lobby; otherwise it notifies the client that the nickname is not available.
     * @param client the client who invokes the method
     * @param nickname the nickname chosen by the client
     * @author Nicolò
     */
    public void initCommand(Client client, String nickname) throws RemoteException {
        serverApp.initCommand(client, nickname);
    }

    /**
     * This method is invoked by the method handler(Client c, String s) if the client state is WAIT and if it is the owner.
     * It handles the command written by the client, calling other methods. If the command is not correct it notifies the client.
     * @param client the client who invokes the method
     * @param command the command chosen by the owner of the lobby
     * @author Nicolò
     */
    public void waitCommand(Client client, String command) throws RemoteException {
        serverApp.waitCommand(client, command);
    }

    /**
     * This method is invoked by the method handler(Client c, String s) if the client state is PLAY.
     * It calls the GameController update(String s) method, passing the command chosen by the player
     * @param client the client who invokes the method
     * @param command the command chosen by the player
     * @author Nicolò
     */
    public void playCommand(Client client, String command) throws RemoteException {
        serverApp.playCommand(client,command);
    }

    public void endCommand(Client client) throws RemoteException {
        serverApp.endCommand(client);
    }
}