package Distributed;


import Distributed.RMI.server.ServerImpl;
import Distributed.ServerSocket.ClientHandlerSocket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerApp {
    private int port;
    private final Set<Lobby> lobbySet;
    private Lobby openLobby;

    public ServerApp(int port) {
        this.port = port;
        this.lobbySet = new HashSet<Lobby>();
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
        openLobby = new Lobby();
        lobbySet.add(openLobby);
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


        while(true) {
            Socket socket = serverSocket.accept();
            socket.getOutputStream().flush();
                if (!openLobby.isOpen()) {
                    openLobby = new Lobby();
                    lobbySet.add(openLobby);
                    System.out.println("Created new lobby");
                }
            executor.submit(new ClientHandlerSocket(socket, openLobby, this));
            System.out.println("Passed socket to handler");
        }

    }
    public void removeLobby(Lobby lobby){
        this.lobbySet.remove(lobby);
    }
    public String checkNickname(String input){
        boolean found = true;
        for(Lobby l: lobbySet){
            for(RemotePlayer p:  l.getListOfPlayers()){
                if(p.getNickname().equals(input)){
                    found = false;
                    break;
                }
            }
            if(!found) break;
        }
        if(found) return input;
        return null;
    }

    public void register(Distributed.RMI.client.ClientImpl client) {
        //TODO IMPL
    }

    public Set<Lobby> getLobbySet() {
        return lobbySet;
    }
}
