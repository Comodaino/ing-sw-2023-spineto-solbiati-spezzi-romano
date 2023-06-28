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
import java.util.concurrent.TimeUnit;


public class ServerApp {
    private int port;
    private static List<Lobby> lobbies;
    private Lobby openLobby;
    private static ServerImpl serverRMI;

    public ServerApp(int port) throws RemoteException {
        super();
        this.port = port;
        lobbies = new ArrayList<Lobby>();
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

        Thread rmiHB = new Thread(){
            @Override
            public void run() {
                while(true){
                    heartBeatService();

                    try {
                        TimeUnit.SECONDS.sleep(5);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

            }
        };

        rmiHB.start();

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

    /**
     * This method remove a Lobby from the list of lobbies.
     * @param lobby the lobby to be removed
     * @author Alessio
     */
    public void removeLobby(Lobby lobby) {
        synchronized (lobbies) {
            this.lobbies.remove(lobby);
        }
    }

    /**
     * This method is invoked by the ServerImpl method handler().
     * It gets the client state and, according to it, it invokes other ServerApp methods to handle the client;
     * then it update all the players' views.
     * @param client the client who invokes the method
     * @param arg the input written by the client
     * @author Nicolò
     */
    public void handler(Client client, String arg) throws RemoteException {
        Lobby lobby = null;
        System.out.println("state: " + client.getState());

        if(client.getState()==States.INIT){
            initCommand(client, arg);
        }

        synchronized (lobbies) {
            System.out.println(client.getLobbyID());
            lobby = lobbies.get(client.getLobbyID() - 1);
        }


        try {
        switch (client.getState()) {
            case INIT:
                break;
            case WAIT:
                waitCommand(client, arg);
                lobby.updateAll();
                break;
            case PLAY:
                playCommand(client, arg);
                lobby.updateAll();
                break;
            case END:
                endCommand(client);
                lobby.updateAll();
                break;
        }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }


    }

    /**
     * This method is invoked by the method handler(Client c, String s) if the client state is INIT.
     * It invokes the nickname checker and, if the return is positive, registers the client to the server, adding it to the list of player of the lobby;
     * otherwise it notifies the client that the nickname is not available.
     * If the same client is trying to reconnect itself to the game it was playing, it invokes the RMIPlayer method reconnect().
     * @param client the client who invokes the method
     * @param nickname the nickname chosen by the client
     * @author Nicolò
     */
    public void initCommand(Client client, String nickname) throws RemoteException {
        Lobby lobby = null;
        States clientState = null;

        String check = checkNickname(nickname);
        System.out.println("returned: " + check);
        if (check.equals("true") ) {
            RMIPlayer rp = new RMIPlayer(client);
            rp.setNickname(nickname);
            client.setNickname(nickname);
            addPlayer(client, rp);

        } else if(check.equals("reconnected")) {
            client.setNickname(nickname);
            synchronized (lobbies) {
                for(Lobby l: lobbies) {
                    for(RemotePlayer rp: l.getListOfPlayers()) {
                        if(rp.getNickname().equals(nickname)) {
                            rp.reconnect(client, l.getID());
                            break;
                        }
                    }
                }
            }
        } else client.update(null, "/nickname");
    }

    /**
     * This method is invoked by the method handler(Client c, String s) if the client state is WAIT and if it is the owner.
     * It handles the command written by the client, calling other methods. If the command is not correct it notifies the client.
     * @param client the client who invokes the method
     * @param command the command chosen by the owner of the lobby
     * @author Nicolò
     */
    public void waitCommand(Client client, String command) throws IOException, InterruptedException {
        Lobby lobby = null;
        synchronized (lobbies) {
            lobby = lobbies.get(client.getLobbyID() - 1);
        }

        switch (command) {
            case "/start":
                if (client.isOwner()) {
                    if(lobby.getListOfPlayers().size()>1){
                        lobby.startGame();
                    }
                }
                break;
            case "/firstMatch":
                if (client.isOwner()) {
                    lobby.setFirstMatch(true);
                    System.out.println("First match: " + lobby.isFirstMatch());
                }
                break;
            case "/notFirstMatch":
                if (client.isOwner()) {
                    lobby.setFirstMatch(false);
                    System.out.println("First match: " + lobby.isFirstMatch());
                }
                break;
            case "/closeLobby":
                if (client.isOwner()) {
                    lobby.close();
                }
                break;
            default:
                if(command != null && command.startsWith("/set")) {
                    if(command.length()==6) lobby.setMaxNumberOfPlayers(command.charAt(5) - 48);
                }else lobby.getController().update(command);
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

    /**
     * This method is invoked by the method handler(Client c, String s) if the client state is END.
     * It sets the state of all the players in the lobby to WAIT, so they can decide if to play another game or to leave.
     * @param client the client who invokes the method
     * @author Nicolò
     */
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

    /**
     * This method checks for disconnections for all the RMI Players
     * @author Nicolò
     */
    public static void heartBeatService() {
        synchronized (lobbies) {
            for(Lobby l: lobbies){
                for(RemotePlayer rp: l.getListOfPlayers()){
                    if(rp.getConnectionType()==ConnectionType.RMI && rp.isConnected()){
                        boolean allOk;
                        try {
                            allOk = rp.getClient().beat();
                        } catch (RemoteException e) {
                            allOk = false;
                        }
                        if(!allOk) {
                            rp.setConnected(false);
                        }
                    }
                }
            }
        }
    }

    /**
     * This method checks if the nickname chosen by the client is available, searching if there are other players
     * connected to the server with the same nickname.
     * @param nickname the nickname chosen by the client
     * @return "true" if the nickname is available, "false" if it is not available, "reconnected" if the nickname is already registered
     * and the client is trying to reconnect to the game
     * @author Nicolò
     */
    public String checkNickname(String nickname) {
        boolean notFound = true;
        synchronized (lobbies) {
            for (Lobby l : lobbies) {
                for (RemotePlayer rp : l.getListOfPlayers()) {
                    if (rp.getNickname().equals(nickname)) {
                        if(!rp.isConnected()){
                            rp.setConnected(true);
                            return "reconnected";
                        }
                        notFound = false;
                        break;
                    }
                }
                if (!notFound) break;
            }
        }
        if (notFound) return "true";
        return "false";
    }

    /**
     * This method add the client and his associated remote player to the lobby. If there are no lobby opened, it opens a new lobby.
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
                try {
                    lobbies.get(lobbies.size() - 1).addPlayer(rp);
                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
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

    public List<Lobby> getLobbies() {
        synchronized (lobbies) {
            return lobbies;
        }
    }
}