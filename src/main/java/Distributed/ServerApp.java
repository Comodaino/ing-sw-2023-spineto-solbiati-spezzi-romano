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
    public void startServer() {

        lobbyList = new ArrayList<Lobby>();
        lobbyList.add(new Lobby());
        ExecutorService executor = Executors.newCachedThreadPool();
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return;
        }

        System.out.println("Server ready");
        while (true) {
            try {
                //Accepts new players and creates another lobby if it's full
                //TODO ASK THE LAB ATTENDANT IF THE SYNCHRONIZE IS CORRECT
                Socket socket = serverSocket.accept();
                synchronized(lobbyList.get(lobbyList.size() - 1)) {
                    executor.submit(new ClientHandlerSocket(socket, lobbyList.get(lobbyList.size() - 1)));
                    if (!lobbyList.get(lobbyList.size() - 1).isOpen()) {
                        lobbyList.add(new Lobby());
                    }
                }
            } catch(IOException e) {
                break;
            }
        }
        executor.shutdown();
    }
    public static void main(String[] args) {
        ServerApp server = new ServerApp(25565);
        server.startServer();
    }
}
