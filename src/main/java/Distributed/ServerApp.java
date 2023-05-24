package Distributed;


import Distributed.ClientRMI.Client;
import Distributed.ServerRMI.Server;
//import Distributed.ServerRMI.ServerImpl;
import Distributed.ServerSocket.ClientHandlerSocket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static Distributed.States.*;

public class ServerApp {
    private int port;
    private final List<Lobby> lobbies;
    private Lobby openLobby;

    public ServerApp(int port) throws RemoteException {
        super();
        this.port = port;
        this.lobbies = new ArrayList<Lobby>();
    }

    public static void main(String[] args) {
        ServerApp server = null;
        try {
            server = new ServerApp(25565);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        try {
            server.startServer();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void startServer() throws IOException {
        openLobby = new Lobby(this);
        openLobby.setID(1);
        lobbies.add(openLobby);
        socketAccepter();
    }

    public void socketAccepter() throws IOException {
        ExecutorService executor = Executors.newCachedThreadPool();
        ServerSocket serverSocket;

        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Server socket ready");
        //Accepts new players and creates another lobby if it's full


        while (true) {
            Socket socket = serverSocket.accept();
            socket.getOutputStream().flush();
            synchronized (lobbies) {
                if (!openLobby.isOpen()) {
                    openLobby = new Lobby(this);
                    lobbies.add(openLobby);
                    System.out.println("Created new lobby");
                }
                executor.submit(new ClientHandlerSocket(socket, openLobby, this));
                System.out.println("Passed socket to handler");
            }
        }
    }

    public void removeLobby(Lobby lobby) {
        synchronized (lobbies) {
            this.lobbies.remove(lobby);
        }
    }

    public String checkNickname(String input) {
        boolean found = true;
        synchronized (lobbies) {
            for (Lobby l : lobbies) {
                for (RemotePlayer p : l.getListOfPlayers()) {
                    if (p.getNickname().equals(input)) {
                        found = false;
                        break;
                    }
                }
                if (!found) break;
            }
        }
        if (found) return input;
        return null;
    }

    public List<Lobby> getLobbies() {
        synchronized (lobbies) {
            return lobbies;
        }
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
            if (rp.getConnectionType().equals(ConnectionType.RMI)) {
                lobbies.get(lobbies.size() - 1).addClientRMI(client);
            }
            //Sets lobbyID in Client and if it is owner of the lobby or not
            client.setOwner(rp.isOwner());
            client.setLobbyID(lobbies.get(lobbies.size() - 1).getID());
            System.out.println(rp.getNickname() + " has joined the " + (lobbies.size()) + " lobby");
            System.out.println("owner: " + rp.isOwner());
        }
    }
}