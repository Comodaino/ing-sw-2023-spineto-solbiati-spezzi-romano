package Distributed.RMI.client;

import Distributed.RMI.server.Server;
import Distributed.RemotePlayer;
import Model.Player;

import java.rmi.*;
import java.rmi.server.*;
import java.util.Scanner;

public class ClientImpl extends UnicastRemoteObject implements Client {
    private RemotePlayer player = new RemotePlayer();
    public static void main(String args[]) throws Exception {
        ClientImpl client = new ClientImpl();
        client.doJob("localhost");
    }

    public ClientImpl() throws RemoteException {
    }

    public void doJob(String serverHost) throws Exception {
        Server server;

        // take a reference of the server from the registry
        server = (Server) Naming.lookup("rmi://" + serverHost + "/Server");

        // join
        server.register(this);

        // main loop [...]

        server.leave(this);
    }

    @Override
    public String getNickname(){
        return player.getNickname();
    }

    @Override
    public RemotePlayer getRemotePlayer() throws RemoteException { return player; }
}
