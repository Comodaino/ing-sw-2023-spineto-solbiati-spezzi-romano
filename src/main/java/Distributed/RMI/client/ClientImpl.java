package Distributed.RMI.client;

import Distributed.RMI.common.ChatMessage;
import Distributed.RMI.common.Message;
import Distributed.RMI.server.Server;
import Model.Player;

import java.rmi.*;
import java.rmi.server.*;
import java.util.Scanner;

public class ClientImpl extends UnicastRemoteObject implements Client {
    private String nickname; //just to try rmi
    public static void main(String args[]) throws Exception {
        ClientImpl client = new ClientImpl("Ale");
        client.doJob("localhost");
    }

    public ClientImpl(String nickname) throws RemoteException {
        this.nickname = nickname;
    }

    public void doJob(String serverHost) throws Exception {
        Server server;
        Message msg;

        // take a reference of the server from the registry
        server = (Server) Naming.lookup("rmi://" + serverHost + "/Server");

        // join
        server.join(this);

        // main loop
        final Scanner stdin = new Scanner(System.in);
        while (true) {
            String line = stdin.nextLine();
            msg = new ChatMessage(line, nickname);
            if (msg.getContent().equals("end")) {
                break;
            }
            server.sendMsg(msg);
        }
        server.leave(this);
    }

    @Override
    public String getNickname(){
        return this.nickname;
    }

    @Override
    public void printMsg(Message msg) throws RemoteException {
        System.out.println("["+ msg.getAuthor() +"] " + msg.getContent());
    }
}
