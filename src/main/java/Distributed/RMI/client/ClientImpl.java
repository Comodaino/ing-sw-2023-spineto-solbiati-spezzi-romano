package Distributed.RMI.client;

import Distributed.RMI.server.Server;
import Distributed.RemotePlayer;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.rmi.*;
import java.rmi.server.*;

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
    public void getRemotePlayer() throws RemoteException { //serializes the RemotePlayer
        try{
            FileOutputStream fileOut = new FileOutputStream("src/main/java/Distributed/RMI/serialized/remotePlayer.json");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this.player);
            out.flush();
            out.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
