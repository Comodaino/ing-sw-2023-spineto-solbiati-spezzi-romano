package Distributed.RMI.server;
import Distributed.*;
import Distributed.ClientRMI.Client;
import Distributed.ServerRMI.Server;

import java.util.*;
import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.*;

//TODO check synchronization
public class ServerImpl extends UnicastRemoteObject implements Server {
    private List<Lobby> lobbies;
    private ServerApp serverApp;

    public ServerImpl(ServerApp serverApp) throws RemoteException {
        this.lobbies = new ArrayList<>();
        this.serverApp = serverApp;

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
        RemotePlayer rp = new RemotePlayer(ConnectionType.RMI); //TODO: check the constructor
        String nickname = client.setNickname(serverApp);
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
            client.setIDs(1, lobbies.get(lobbies.size() - 1).getID()); //TODO: generate clientID
            System.out.println(rp.getNickname() + " has joined the " + (lobbies.size()) + " lobby");
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
        //TODO o.getGameController().update(o, arg);
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
}
