package Distributed;


import Distributed.ClientRMI.Client;
import Distributed.ServerRMI.ServerImpl;
import Distributed.ServerSocket.ClientHandlerSocket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerApp {
    private int port;
    private final List<Lobby> lobbies;
    private Lobby openLobby;

    public ServerApp(int port) {
        this.port = port;
        this.lobbies = new ArrayList<Lobby>();
    }

    public static void main(String[] args) {
        ServerApp server = new ServerApp(25565);

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
        lobbies.add(openLobby);
        ServerImpl serverRMI = new ServerImpl(this);
        serverRMI.start();
        System.out.println("Server ready");
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
        this.lobbies.remove(lobby);
    }

    public String checkNickname(String input) {
        boolean found = true;
        for (Lobby l : lobbies) {
            for (RemotePlayer p : l.getListOfPlayers()) {
                if (p.getNickname().equals(input)) {
                    found = false;
                    break;
                }
            }
            if (!found) break;
        }
        if (found) return input;
        return null;
    }

    public List<Lobby> getLobbies() {
        return lobbies;
    }

    public void addPlayer(Client client, RemotePlayer rp) throws RemoteException {
        synchronized (lobbies) {
            if (!lobbies.get(lobbies.size() - 1).isOpen()) {
                lobbies.add(new Lobby(this));
                lobbies.get(lobbies.size() - 1).setID(lobbies.size());
            }
            //Adds the player in the list of RemotePlayer of the Lobby the client joined
            lobbies.get(lobbies.size() - 1).addPlayer(rp);
            //Sets lobbyID and clientID in Client
            client.setLobbyID(lobbies.get(lobbies.size() - 1).getID());
            System.out.println(rp.getNickname() + " has joined the " + (lobbies.size()) + " lobby");
            System.out.println("owner: " + rp.isOwner());
        }
    }
}
