package Distributed.RMI.server;

import Distributed.Lobby;
import Distributed.RMI.client.Client;
import Distributed.RMI.common.Message;
import Distributed.ServerSocket.ClientHandlerSocket;

import java.io.IOException;
import java.net.Socket;
import java.util.*;
import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerApp extends UnicastRemoteObject implements Server {
    private List<Lobby> lobbies;

    public ServerApp() throws RemoteException {
        lobbies = new ArrayList<Lobby>();
    }

    public void startServer() {
        //Creates RMI Registry
        Registry registry = null;
        try {
            registry = LocateRegistry.createRegistry(1099);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        //Binds the name of the server with this server
        try {
            registry.bind("ServerApp", this);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        } catch (AlreadyBoundException e) {
            throw new RuntimeException(e);
        }

        lobbies.add(new Lobby());
        System.out.println("Server bound and ready");

        while(true) {
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
    }
    public synchronized void join(Client client) throws RemoteException {
        lobbies.get(lobbies.size()-1).addPlayer(client.getPlayer());
        System.out.println(client.getNickname() + " has connected to the server");
    }

    public synchronized void leave(Client client) throws RemoteException {
        System.out.println(client.getNickname() + " has left the server");
        clients.remove(client);
    }

    public void sendMsg(Message msg) throws RemoteException {
        List<Client> clientsCopy = null;
        synchronized(this) {
            clientsCopy = new ArrayList<Client>(clients);
        }
        for(Client c: clientsCopy) {
            if(!c.getNickname().equals(msg.getAuthor())){
                c.printMsg(msg);
            }
        }
    }

    public static void main(String args[]) throws Exception {
        ServerApp server = new ServerApp();
        server.startServer();
    }
}
