package Distributed.ServerRMI;

import Distributed.ClientRMI.Client;
import Distributed.ConnectionType;
import Distributed.Lobby;
import Distributed.RemotePlayer;
import Distributed.States;
import Model.BoardView;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class ServerImpl extends UnicastRemoteObject implements Server {
    private List<Lobby> lobbies;
    private Registry registry;
    private Lobby openLobby;

    public ServerImpl() throws RemoteException {
        this.registry = null;
        this.lobbies = new ArrayList<>();
    }

    public static void main(String[] args) {
        ServerImpl server = null;
        try {
            server = new ServerImpl();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        server.start();
    }

    public void start() {
        try {
            registry = LocateRegistry.createRegistry(1099); //Creates RMI Registry
            registry.bind("Server", this); //Binds the name of the server with this object
        } catch (RemoteException | AlreadyBoundException e) {
            throw new RuntimeException(e);
        }

        this.openLobby = new Lobby(this);
        this.lobbies.add(openLobby);
        openLobby.setID(1);
        System.out.println("Server bound and ready");
    }

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

    public void initCommand(Client client, String nickname) throws RemoteException {
        if(checkNickname(nickname)!=null) {
            RMIPlayer rp = new RMIPlayer(client);
            rp.setNickname(nickname);
            client.setNickname(nickname);
            addPlayer(client, rp);
            client.setState(States.WAIT);

        } else client.update(null, "/nickname");
    }

    public void waitCommand(Client client, String arg) throws RemoteException {
        Lobby lobby = null;
        synchronized(lobbies) {
            lobby = lobbies.get(client.getLobbyID()-1);
        }

        switch(arg) {
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

    @Override
    public BoardView getBoardView(Client client) throws RemoteException {
        Lobby lobby = null;
        synchronized(lobbies) {
            lobby = lobbies.get(client.getLobbyID()-1);
        }
        return lobby.getBoardView();
    }

    //LOCAL FUNCTIONS
    public String checkNickname(String input) {
        boolean found = true;
        synchronized(lobbies) {
            for(Lobby l : lobbies) {
                for(RemotePlayer rp : l.getListOfPlayers()) {
                    if(rp.getNickname().equals(input)) {
                        found = false;
                        break;
                    }
                }
                if(!found) break;
            }
        }
        if(found) return input;
        return null;
    }

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