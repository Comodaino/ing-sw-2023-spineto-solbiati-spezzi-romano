package Distributed;


import Distributed.ServerSocket.ClientHandlerSocket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
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
        server.startServer();
    }

    public void startServer() {
        openLobby = new Lobby(this);
        lobbySet.add(openLobby);

        System.out.println("Server ready");
        while (true) {
            new Runnable() {
                public void run() {
                    try {
                        socketAccepter();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

            };
            new Runnable() {
                public void run() {
                    RMIDistributor();
                }

            };
        }

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
        Socket socket = serverSocket.accept();
        synchronized (lobbySet) {
            if(!openLobby.isOpen()){
                openLobby = new Lobby(this);
                lobbySet.add(openLobby);
            }
            executor.submit(new ClientHandlerSocket(socket,openLobby));
        }
        executor.shutdown();
    }
    public void removeLobby(Lobby lobby){
        this.lobbySet.remove(lobby);
    }
    public void RMIDistributor() {//TODO IMPLEMENT
    }
}
