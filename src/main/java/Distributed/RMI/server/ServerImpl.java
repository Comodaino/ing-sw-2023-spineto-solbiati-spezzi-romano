package Distributed.RMI.server;

import Distributed.RMI.client.Client;
import Distributed.RMI.common.Message;

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
        System.out.println(client.getNickname() + " has connected to the server");
        clients.add(client);
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
        ServerImpl server = new ServerImpl();
        Registry registry = LocateRegistry.createRegistry(1099);
        registry.bind("Server", server);
        System.out.println("Server bound and ready");
    }
}
