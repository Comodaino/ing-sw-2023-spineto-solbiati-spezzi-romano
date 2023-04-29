package Distributed.server;

import Distributed.client.Client;
import Distributed.common.Message;

import java.util.*;
import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.*;

public class ServerImpl extends UnicastRemoteObject implements Server {
    private List<Client> clients;

    public ServerImpl() throws RemoteException {
        clients = new ArrayList<Client>();
    }

    public synchronized void join(Client client) throws RemoteException {
        clients.add(client);
    }

    public synchronized void leave(Client client) throws RemoteException {
        clients.remove(client);
    }

    public void sendMsg(Message msg) throws RemoteException {
        List<Client> clientsCopy = null;
        synchronized(this) {
            clientsCopy = new ArrayList<Client>(clients);
        }
        for(Client c: clientsCopy) {
            c.printMsg(msg);
        }
    }

    public static void main(String args[]) throws Exception {
        ServerImpl server = new ServerImpl();
        Registry registry = LocateRegistry.createRegistry(1099);
        registry.bind("Server", server);
        System.out.println("Server bound and ready");
    }
}
