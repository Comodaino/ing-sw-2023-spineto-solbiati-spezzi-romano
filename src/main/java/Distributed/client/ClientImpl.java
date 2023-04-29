package Distributed.client;

import Distributed.common.ChatMessage;
import Distributed.common.Message;
import Distributed.server.Server;

import java.rmi.*;
import java.rmi.server.*;
import java.util.Scanner;

public class ClientImpl extends UnicastRemoteObject implements Client {
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
        server = (Server) Naming.lookup("rmi://" + serverHost + "/Server");

        // join
        server.join(this);

        // main loop
        final Scanner stdin = new Scanner(System.in);
        while (true) {
            String line = stdin.nextLine();
            msg = new ChatMessage(line);
            if (msg.getContent().equals("end")) {
                break;
            }
            server.sendMsg(msg);
        }
        server.leave(this);
    }

    @Override
    public void printMsg(Message msg) throws RemoteException {
        System.out.println(msg.getContent());
    }
}
