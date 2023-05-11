package Distributed.ClientRMI;

import Distributed.ServerRMI.RMIPlayer;
import Distributed.ServerRMI.Server;
import Distributed.RemotePlayer;

import java.rmi.*;
import java.rmi.server.*;

public class ClientImpl extends UnicastRemoteObject implements Client implements RemotePlayer{
    private RemotePlayer player = new RMIPlayer(/*TODO NEEDS ID */ 0);
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
