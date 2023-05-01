package Distributed.RMI.client;

import Distributed.RMI.common.ChatMessage;
import Distributed.RMI.common.Message;
import Distributed.RMI.server.Server;
import Distributed.RemotePlayer;
import Model.Player;

import java.rmi.*;
import java.rmi.server.*;
import java.util.Scanner;

public class ClientImpl extends UnicastRemoteObject implements Client {
    private RemotePlayer player;
    public static void main(String args[]) throws Exception {
        ClientImpl client = new ClientImpl();
        client.doJob("localhost");
    }

    public ClientImpl() throws RemoteException {
    }

    public void doJob(String serverHost) throws Exception {
        Server server;
        Message msg;

        // take a reference of the server from the registry
        server = (Server) Naming.lookup("rmi://" + serverHost + "/ServerApp");

        // join
        server.join(this);

        // main loop
        final Scanner stdin = new Scanner(System.in);
        while (true) {
            String line = stdin.nextLine();
            msg = new ChatMessage(line, player.getNickname());
            if (msg.getContent().equals("end")) {
                break;
            }
            server.sendMsg(msg);
        }
        server.leave(this);
    }

    @Override
    public String getNickname(){
        return player.getNickname();
    }

    @Override
    public RemotePlayer getPlayer() throws RemoteException {
        return player;
    }

    @Override
    public void printMsg(Message msg) throws RemoteException {
        System.out.println("["+ msg.getAuthor() +"] " + msg.getContent());
    }
}
