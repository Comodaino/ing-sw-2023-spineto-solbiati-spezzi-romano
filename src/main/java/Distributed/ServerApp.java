package Distributed;


import Distributed.ClientRMI.Client;
import Distributed.ServerRMI.RMIPlayer;
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
    private static ServerImpl serverRMI;

    public ServerApp(int port) throws RemoteException {
        super();
        this.port = port;
        this.lobbies = new ArrayList<Lobby>();
        serverRMI = new ServerImpl(this);
        openLobby = new Lobby(this);
        openLobby.setID(1);
        lobbies.add(openLobby);
    }

    public static void execute() {
        ServerApp server = null;
        try {
            server = new ServerApp(25565);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }


        serverRMI.start();
        try {
            server.startServer();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }


    }

    public void startServer() throws IOException, InterruptedException {
        socketAccepter();
    }

    public void socketAccepter() throws IOException, InterruptedException {
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
            addPlayer(null, null);
            executor.submit(new ClientHandlerSocket(socket, openLobby, this));
            System.out.println("Passed socket to handler");
        }
    }

    public void removeLobby(Lobby lobby) {
        synchronized (lobbies) {
            this.lobbies.remove(lobby);
        }
    }

    public List<Lobby> getLobbies() {
        synchronized (lobbies) {
            return lobbies;
        }
    }

    public void handler(Client client, String arg) throws RemoteException {
        Lobby lobby = null;
        States clientState = null;


        if(client.getState()==States.INIT){
                initCommand(client, arg);
        }

        synchronized (lobbies) {
            System.out.println(client.getLobbyID());
            lobby = lobbies.get(client.getLobbyID() - 1);
        }

        for (RemotePlayer rp : lobby.getListOfPlayers()) {
            if (rp.getNickname().equals(client.getNickname())) {
                clientState = rp.getState();
            }
        }

        switch (clientState) {
            case INIT:
                break;
            case WAIT:
                if (client.isOwner()) {
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

        try {
            lobby.updateAll();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void initCommand(Client client, String nickname) throws RemoteException {
        Lobby lobby = null;
        States clientState = null;

        if (checkNickname(nickname) != null ) {
            RMIPlayer rp = new RMIPlayer(client);
            rp.setNickname(nickname);
            client.setNickname(nickname);
            addPlayer(client, rp);
            rp.setState(States.WAIT);

        } else client.update(null, "/nickname");
    }

    public void waitCommand(Client client, String command) throws RemoteException {
        Lobby lobby = null;
        synchronized (lobbies) {
            lobby = lobbies.get(client.getLobbyID() - 1);
        }

        switch (command) {
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
        synchronized (lobbies) {
            lobby = lobbies.get(client.getLobbyID() - 1);
        }

        System.out.println("Received command: " + command);
        try {
            lobby.getController().update(command);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void endCommand(Client client) throws RemoteException {
        Lobby lobby = null;
        States clientState = null;
        synchronized (lobbies) {
            lobby = lobbies.get(client.getLobbyID() - 1);
        }

        for (RemotePlayer rp : lobby.getListOfPlayers()) {
            if (rp.getNickname().equals(client.getNickname())) {
                rp.setState(States.WAIT);
            }
        }
    }

    //LOCAL FUNCTIONS

    /**
     * This method checks if the nickname chosen by the client is available, searching if there are other players
     * connected to the server with the same nickname.
     *
     * @param nickname the nickname chosen by the client
     * @return null if the nickname is not available, the nickname itself otherwise
     * @author Nicolò
     */
    public String checkNickname(String nickname) {
        boolean found = true;
        synchronized (lobbies) {
            for (Lobby l : lobbies) {
                for (RemotePlayer rp : l.getListOfPlayers()) {
                    if (rp.getNickname().equals(nickname)) {
                        found = false;
                        break;
                    }
                }
                if (!found) break;
            }
        }
        if (found) return nickname;
        return null;
    }

    /**
     * This method add the client and his associated remote player to the lobby. If there are no lobby opened, it opens a new lobby.
     *
     * @param client client to be added to a lobby
     * @param rp     the RemotePlayer associated to the client
     * @author Nicolò
     */
    public void addPlayer(Client client, RemotePlayer rp) throws RemoteException {
        synchronized (lobbies) {
            //If the lobby is closed, creates a new lobby and sets its ID

            if (!openLobby.isOpen()) {
                openLobby = new Lobby(this);
                lobbies.add(openLobby);
                openLobby.setID(lobbies.size());
                System.out.println("Created new lobby");

            }


            if (client != null && rp!=null) {
                lobbies.get(lobbies.size() - 1).addPlayer(rp);
                client.setOwner(rp.isOwner());
                client.setLobbyID(lobbies.get(lobbies.size() - 1).getID());
                System.out.println(rp.getNickname() + " has joined the " + (lobbies.size()) + " lobby");
                System.out.println("owner: " + rp.isOwner());
            }

        }
    }

    public Lobby getLobby(String nickname) {
        for (Lobby l : lobbies) {
            for (RemotePlayer p : l.getListOfPlayers()) {
                if (p.getNickname().equals(nickname)) return l;
            }
        }
        return null;
    }

}