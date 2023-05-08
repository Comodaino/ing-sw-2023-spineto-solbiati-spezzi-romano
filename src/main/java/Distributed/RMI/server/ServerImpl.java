package Distributed.RMI.server;

import Controller.GameController;
import Controller.GameControllerSocket;
import Distributed.Lobby;
import Distributed.RMI.client.Client;
import Distributed.RemoteHandler;
import Distributed.RemotePlayer;
import Distributed.ServerSocket.States;
import Model.Board;
import Model.Player;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.*;
import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.*;

//TODO check synchronization
public class ServerImpl extends UnicastRemoteObject implements Server {
    private List<Lobby> lobbies;

    public ServerImpl() throws RemoteException {
        this.lobbies = new ArrayList<>();
    }

    public void start() {
        Registry registry = null;
        try {
            registry = LocateRegistry.createRegistry(1099); //Creates RMI Registry
            registry.bind("Server", this); //Binds the name of the server with this server
        } catch (RemoteException | AlreadyBoundException e) {
            throw new RuntimeException(e);
        }

        lobbies.add(new Lobby());
        lobbies.get(0).setID(1);
        System.out.println("Server bound and ready");
    }

    @Override
    public void register(Client client) throws RemoteException {
        //Creates a new RemotePlayer and sets its nickname, asking it to the client
        PlayerRMI rp = new PlayerRMI(); //TODO: check the constructor
        String nickname = client.setNickname(this);
        rp.setNickname(nickname);

        synchronized (lobbies){
            //If the lobby is closed, creates a new lobby and sets its ID
            if (!lobbies.get(lobbies.size() - 1).isOpen()) {
                lobbies.add(new Lobby());
                lobbies.get(lobbies.size() - 1).setID(lobbies.size());
            }
            //Adds the player in the list of RemotePlayer of the Lobby the client joined
            lobbies.get(lobbies.size() - 1).addPlayer(rp);
            //Sets lobbyID and clientID in Client
            client.setIDs(2, lobbies.get(lobbies.size() - 1).getID()); //TODO: generate clientID
            rp.setClientID(2); //TODO: generate clientID
            System.out.println(rp.getNickname() + " has joined the " + (lobbies.size()) + " lobby");
            System.out.println("owner: " + rp.isOwner());
        }
    }

    @Override
    public void leave(Client client) throws RemoteException {
        synchronized (lobbies){
            for(Lobby l: lobbies){
                for(RemotePlayer rp: l.getListOfPlayers()){
                    if(client.getNickname().equals(rp.getNickname())){
                        l.getListOfPlayers().remove(rp);
                        System.out.println(client.getNickname() + " has left the server");
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void update(RemoteHandler o, Object arg) throws RemoteException { //TODO ClientHandlerRMI instead of RemoteHandler
        o.getGameController().update(o, arg);
    }

    @Override
    public String checkNickname(String nickname) throws RemoteException {
        synchronized (lobbies){
            //Iterates on each RemotePlayer in each Lobby and return null if the nickname is already taken
            for(Lobby l: lobbies) {
                for(RemotePlayer rp : l.getListOfPlayers()) {
                    if(rp.getNickname().equals(nickname)){
                        return null;
                    }
                }
            }
        }
        System.out.println("OK");
        return nickname;
    }

    @Override
    public boolean closeLobby(Client client) throws RemoteException {
        synchronized (lobbies){
            List<RemotePlayer> rps = lobbies.get(client.getLobbyID()-1).getListOfPlayers();
            for(RemotePlayer rp: rps){
                if(rp.isOwner() && rp.getNickname().equals(client.getNickname())){
                    //searches the client who is trying to close the lobby: if it is the owner of the lobby, then it closes the lobby
                        return lobbies.get(client.getLobbyID()-1).closeLobby();
                }
            }
        }
        client.printMsg("You cannot close the lobby because you're not the owner");
        return false;
    }

    @Override
    public void waitCommand(Client client) throws RemoteException {
    }

    @Override
    public void playCommand(Client client) throws RemoteException {
    }

    @Override
    public void endCommand(Client client) throws RemoteException {
    }

    public static void main(String args[]) throws Exception {
        ServerImpl server = new ServerImpl();
        server.start();
    }
}
