package Distributed.ServerRMI;

import Controller.GameController;
import Distributed.Lobby;
import Distributed.ClientRMI.Client;
import Distributed.RemoteHandler;
import Distributed.RemotePlayer;
import Model.Board;
import Model.Player;

import java.util.*;
import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.*;

public class ServerImpl extends UnicastRemoteObject implements Server {
    //TODO CHIEDERE SE SERVE O MENO MISCHIARE DISTR E CONTROLLER
    private HashMap<Integer, Board> models; //each lobby has its own model
    private HashMap<Integer, GameController> controllers; //each lobby has its own controller
    private List<Lobby> lobbies;

    public ServerImpl() throws RemoteException {
        this.models = new HashMap<>() ;
        this.controllers = new HashMap<>();
        this.lobbies = new ArrayList<>();
    }

    public void start() {
        Registry registry = null;
        try {
            registry = LocateRegistry.createRegistry(1099); //Creates RMI Registry
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        try {
            registry.bind("Server", this); //Binds the name of the server with this server
        } catch (RemoteException | AlreadyBoundException e) {
            throw new RuntimeException(e);
        }

        lobbies.add(new Lobby());
        lobbies.get(0).setSerialNumber(0);
        System.out.println("Server bound and ready");
    }
    public synchronized void register(Client client) throws RemoteException {
        if (!lobbies.get(lobbies.size() - 1).isOpen()) { //if the lobby is closed, creates a new lobby
            lobbies.add(new Lobby());
            lobbies.get(lobbies.size() - 1).setSerialNumber(lobbies.size() - 1);
        }

        if(lobbies.get(lobbies.size() - 1).getListOfPlayers().size() == 0){ //if the lobby is empty, creates a new model and controller for the game
            List<Player> players = new ArrayList<Player>();
            Boolean firstMatch = lobbies.get(lobbies.size() - 1).isFirstMatch();
            Integer lobbyNumber = lobbies.size() - 1;

            players.add(client.getRemotePlayer().getModelPlayer());
            models.put(lobbyNumber, new Board(firstMatch, players));
            controllers.put(lobbyNumber, new GameController(players, firstMatch)); //TODO: IMPLEMENTS CONTROLLER RMI
        }

        lobbies.get(lobbies.size() - 1).addPlayer(client.getRemotePlayer()); //add the player (client) in the last lobby available
        models.get(lobbies.size() - 1).addPlayer(client.getRemotePlayer().getModelPlayer()); //add the player (client) in the list of players in the model
        System.out.println(client.getNickname() + " has joined the " + (lobbies.size() - 1) + " lobby");
    }

    public synchronized void leave(Client client) throws RemoteException {
        for(Lobby l: lobbies){
            for(RemotePlayer rp: l.getListOfPlayers()){
                if(client.getRemotePlayer().equals(rp)){
                    l.getListOfPlayers().remove(rp);
                }
            }
        }
        System.out.println(client.getNickname() + " has left the server");
    }

    public void update(RemoteHandler o, Object arg) throws RemoteException { //TODO ClientHandlerRMI instead of RemoteHandler
        Integer lobbyNumber = o.getLobby().getSerialNumber();
        //controllers.get(lobbyNumber).update(o, arg);
    }
    public static void main(String args[]) throws Exception {
        ServerImpl server = new ServerImpl();
        server.start();
    }
}
