package Distributed.ServerRMI;

import Distributed.ClientRMI.Client;
import Distributed.Lobby;
import Distributed.RemotePlayer;
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
    private List<Lobby> lobbies;
    private Registry registry;
    private Lobby openLobby;

    public ServerImpl() throws RemoteException {
        this.registry = null;
        this.lobbies = new ArrayList<>();
    }

    //Creates an instance of ServerImpl and starts the server
    public static void execute() {
        ServerImpl server = null;
        try {
            server = new ServerImpl();
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

        this.openLobby = new Lobby(this);
        this.lobbies.add(openLobby);
        openLobby.setID(1);
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
        States clientState = client.getState();
        Lobby lobby = null;

        switch(clientState){
            case INIT:
                initCommand(client, arg);
                break;
            case WAIT:
                if(client.isOwner()) {
                    waitCommand(client, arg);
                }
                break;
            case PLAY:
                playCommand(client, arg);
                break;
            case END:
                endCommand(client);
                break;
        }

        synchronized(lobbies) {
            lobby = lobbies.get(client.getLobbyID()-1);
        }
        try {
            lobby.updateAll();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
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
        if(checkNickname(nickname)!=null) {
            RMIPlayer rp = new RMIPlayer(client);
            rp.setNickname(nickname);
            client.setNickname(nickname);
            addPlayer(client, rp);
            client.setState(States.WAIT);

        } else client.update(null, "/nickname");
    }


    /**
     * This method is invoked by the method handler(Client c, String s) if the client state is WAIT and if it is the owner.
     * It handles the command written by the client, calling other methods. If the command is not correct it notifies the client.
     * @param client the client who invokes the method
     * @param command the command chosen by the owner of the lobby
     * @author Nicolò
     */
    public void waitCommand(Client client, String command) throws RemoteException {
        Lobby lobby = null;
        synchronized(lobbies) {
            lobby = lobbies.get(client.getLobbyID()-1);
        }

        switch(command) {
            case "/start":
                lobby.startGame();
                break;
            case "/firstMatch":
                lobby.setFirstMatch(true);
                System.out.println("First match: " + lobby.isFirstMatch());
                break;
            case "/notFirstMatch":
                lobby.setFirstMatch(false);
                System.out.println("First match: " + lobby.isFirstMatch());
                break;
            case "/closeLobby":
                lobby.close();
                break;
            default:
                client.update(null, "/command");
                break;
        }
    }


    /**
     * This method is invoked by the method handler(Client c, String s) if the client state is PLAY.
     * It calls the GameController update(String s) method, passing the command chosen by the player
     * @param client the client who invokes the method
     * @param command the command chosen by the player
     * @author Nicolò
     */
    public void playCommand(Client client, String command) throws RemoteException {
        Lobby lobby = null;
        synchronized(lobbies) {
            lobby = lobbies.get(client.getLobbyID()-1);
        }

        System.out.println("Received command: " + command);
        try {
            lobby.getController().update(command);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void endCommand(Client client) throws RemoteException {
        //TODO implements
    }


    //LOCAL FUNCTIONS
    /**
     * This method checks if the nickname chosen by the client is available, searching if there are other players
     * connected to the server with the same nickname.
     * @param nickname the nickname chosen by the client
     * @return null if the nickname is not available, the nickname itself otherwise
     * @author Nicolò
     */
    public String checkNickname(String nickname) {
        boolean found = true;
        synchronized(lobbies) {
            for(Lobby l : lobbies) {
                for(RemotePlayer rp : l.getListOfPlayers()) {
                    if(rp.getNickname().equals(nickname)) {
                        found = false;
                        break;
                    }
                }
                if(!found) break;
            }
        }
        if(found) return nickname;
        return null;
    }

    /**
     * This method add the client and his associated remote player to the lobby. If there are no lobby opened, it opens a new lobby.
     * @param client client to be added to a lobby
     * @param rp the RemotePlayer associated to the client
     * @author Nicolò
     */
    public void addPlayer(Client client, RemotePlayer rp) throws RemoteException {
        synchronized (lobbies) {
            //If the lobby is closed, creates a new lobby and sets its ID
            if (!lobbies.get(lobbies.size() - 1).isOpen()) {
                lobbies.add(new Lobby(this));
                lobbies.get(lobbies.size() - 1).setID(lobbies.size());
            }

            //Adds the player in the list of RemotePlayer of the Lobby the client joined
            lobbies.get(lobbies.size() - 1).addPlayer(rp);

            //Sets lobbyID in Client and if it is owner of the lobby or not
            client.setOwner(rp.isOwner());
            client.setLobbyID(lobbies.get(lobbies.size() - 1).getID());
            System.out.println(rp.getNickname() + " has joined the " + (lobbies.size()) + " lobby");
            System.out.println("owner: " + rp.isOwner());
        }
    }
}