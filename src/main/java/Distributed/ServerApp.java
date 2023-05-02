package Distributed;


import Distributed.ServerSocket.ClientHandlerSocket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerApp {
    private int port;
    private List<Lobby> lobbyList;

    public ServerApp(int port) {
        this.port = port;
    }

    public static void main(String[] args) {
        ServerApp server = new ServerApp(25565);
        server.startServer();
    }

    public void startServer() {

        lobbyList = new ArrayList<Lobby>();
        lobbyList.add(new Lobby());

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
        //TODO ASK THE LAB ATTENDANT IF THE SYNCHRONIZE IS CORRECT AND IF DELETING THE ELEMENT FROM THE LIST IS NEEDED OR THE JVM DOES IT FORM ME
        Socket socket = serverSocket.accept();
        synchronized (lobbyList.get(lobbyList.size() - 1)) {
            executor.submit(new ClientHandlerSocket(socket, lobbyList.get(lobbyList.size() - 1)));
            if (!lobbyList.get(lobbyList.size() - 1).isOpen()) {
                lobbyList.add(new Lobby());
            }
        }
        executor.shutdown();
    }

    public void RMIDistributor() {//TODO
    }
}
