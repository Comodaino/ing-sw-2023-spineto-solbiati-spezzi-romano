package Distributed;


import Distributed.ClientRMI.Client;
import Distributed.ServerRMI.RMIPlayer;
import Distributed.ServerRMI.Server;
//import Distributed.ServerRMI.ServerImpl;
import Distributed.ServerSocket.ClientHandlerSocket;
import Model.BoardView;

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

public class ServerApp extends UnicastRemoteObject implements Server {
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
        } catch (RemoteException e ) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void startServer() throws IOException {
        openLobby = new Lobby(this);
        openLobby.setID(1);
        lobbies.add(openLobby);
        //ServerImpl serverRMI = new ServerImpl(this);
        //serverRMI.start();
        start();
        System.out.println("Server RMI ready");
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

    //EXPERIMENT

    public void start() {
        Registry registry = null;
        try {
            registry = LocateRegistry.createRegistry(1099); //Creates RMI Registry
            registry.bind("ServerApp", this); //Binds the name of the server with this server
        } catch (RemoteException | AlreadyBoundException e) {
            throw new RuntimeException(e);
        }
        //System.out.println("Server bound and ready");
    }

    @Override
    public void register(Client client) throws RemoteException {
        //Creates a new RemotePlayer and sets its nickname, asking it to the client
        RMIPlayer rp = new RMIPlayer(client);
        String nickname = client.setNickname(this);
        rp.setNickname(nickname);
        //If the lobby is closed, creates a new lobby and sets its ID, adds the player in the list of RemotePlayer of the Lobby the client joined
        addPlayer(client, rp);
        //Sets the client state in WAIT
        client.setState(WAIT_SETTING);
        rp.setState(WAIT_SETTING);
    }

    @Override
    public void leave(Client client) throws RemoteException {
        if(client.isOwner()){
            closeLobby(client);
        }

        synchronized (lobbies) {
            for (Lobby l : lobbies) {
                for (RemotePlayer rp : l.getListOfPlayers()) {
                    if (client.getNickname().equals(rp.getNickname())) {
                        l.getListOfPlayers().remove(rp);
                        client.setState(CLOSE);
                        rp.setState(CLOSE);
                        System.out.println(client.getNickname() + " has left the server");
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void update(Client client, String command) throws IOException {
        synchronized (lobbies) { //TODO: ask if this is necessary, since this method does not modify the list of lobby
            lobbies.get(client.getLobbyID() - 1).getController().update(command);
        }
    }

    @Override
    public String checkNicknameRMI(String nickname) throws RemoteException {
        synchronized (lobbies) {
            //Iterates on each RemotePlayer in each Lobby and return null if the nickname is already taken
            for (Lobby l : lobbies) {
                for (RemotePlayer rp : l.getListOfPlayers()) {
                    if (rp.getNickname().equals(nickname)) {
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
        synchronized (lobbies) {
            List<RemotePlayer> rps = lobbies.get(client.getLobbyID() - 1).getListOfPlayers();
            for (RemotePlayer rp : rps) {
                if (rp.isOwner() && rp.getNickname().equals(client.getNickname())) {
                    //Searches the client who is trying to close the lobby: if it is the owner of the lobby, then it closes the lobby
                    return lobbies.get(client.getLobbyID() - 1).closeLobby();
                }
            }
        }
        client.printMsg("You cannot close the lobby because you're not the owner");
        return false;
    }

    @Override
    public BoardView getBoardView(Integer lobbyID) {
        synchronized (lobbies) { //TODO: ask if this is necessary, since this method does not modify the list of lobby
            return (BoardView) lobbies.get(lobbyID-1).getBoardView();
        }
    }

    @Override
    public States myState(Client client) throws RemoteException {
        Lobby lobby = null;
        Integer lobbyID = client.getLobbyID();
        synchronized (lobbies){
            lobby = lobbies.get(lobbyID-1);
        }

        for(RemotePlayer rp: lobby.getListOfPlayers()) {
            if (rp.getNickname().equals(client.getNickname())) {
                return rp.getState();
            }
        }

        return null; //TODO CHECK ERROR
    }

    @Override
    public void waitCommand(Client client, String input) throws RemoteException {
        Lobby lobby = null;
        Integer lobbyID = client.getLobbyID();
        synchronized (lobbies){
            lobby = lobbies.get(lobbyID-1);
        }

        if(input.equals("/leave")){
            leave(client);
        } else if(client.isOwner()){
            switch (input) {
                case "/start":
                    if(closeLobby(client)){
                        lobby.startGame();
                        for(RemotePlayer rp: lobby.getListOfPlayers()){
                            if(rp.getNickname().equals(client.getNickname())){
                                rp.setState(PLAY);
                                client.setState(PLAY);
                            } else {
                                rp.setState(WAIT_TURN);
                            }
                        }
                    } else {
                        client.printMsg("You cannot start the game because there are no other players in the lobby");
                    }
                    break;
                case "/firstMatch":
                    lobby.setFirstMatch(true);
                    break;
                case "/notFirstMatch":
                    lobby.setFirstMatch(false);
                    break;
                case "/closeLobby":
                    closeLobby(client);
                    break;
                default:
                    client.printMsg("Command not valid");
                    break;
            }
        } else { client.printMsg("You're not the owner of the lobby!"); }
    }

    @Override
    public void playCommand(Client client, String input) throws IOException {
        Lobby lobby = null;
        Integer lobbyID = client.getLobbyID();
        synchronized (lobbies){
            lobby = lobbies.get(lobbyID-1);
        }

        System.out.println("Received command: " + input);
        lobby.getController().update(input);

        client.setState(WAIT_TURN);
        for(RemotePlayer rp: lobby.getListOfPlayers()) {
            if (rp.getNickname().equals(client.getNickname())) {
                rp.setState(WAIT_TURN);
            }
        }
    }

    @Override
    public void endCommand(Client client) throws RemoteException {
        Lobby lobby = null;
        Integer lobbyID = client.getLobbyID();
        synchronized (lobbies){
            lobby = lobbies.get(lobbyID-1);
        }

        for(RemotePlayer rp: lobby.getListOfPlayers()) {
            rp.setState(CLOSE);
        }

    }
}