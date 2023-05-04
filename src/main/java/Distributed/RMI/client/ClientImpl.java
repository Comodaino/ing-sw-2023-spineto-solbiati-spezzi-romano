package Distributed.RMI.client;

import Distributed.RMI.server.Server;

import java.rmi.*;
import java.rmi.server.*;
import java.util.Scanner;

public class ClientImpl extends UnicastRemoteObject implements Client {
    private String nickname;
    private Integer clientID;
    private Integer lobbyID;

    public ClientImpl() throws RemoteException {
        nickname = null;
        clientID = null;
        lobbyID = null;
    }

    @Override
    public String getNickname(){
        return this.nickname;
    }

    @Override
    public void setIDs(Integer clientID, Integer lobbyID){
        this.clientID = clientID;
        this.lobbyID = lobbyID;
    }

    @Override
    public String setNickname(Server server) throws RemoteException{
        String nickname = null;
        boolean found = false;

        do {
            System.out.println("Please choose a unique nickname");
            Scanner scanIn = new Scanner(System.in);
            nickname = scanIn.nextLine();

            nickname = server.checkNickname(nickname);
            if(nickname==null){
                System.out.println("Please be more original, nickname already chosen!");
            }
        } while(nickname==null);

        System.out.println("OK");
        this.nickname = nickname;
        return this.nickname;
    }

    public void doJob(String serverHost) throws Exception {
        Server server;
        // take a reference of the server from the registry
        server = (Server) Naming.lookup("rmi://" + serverHost + "/Server");
        // join
        server.register(this);
        // main loop [...]
        //server.leave(this);
    }

    public static void main(String args[]) throws Exception {
        ClientImpl client = new ClientImpl();
        client.doJob("localhost");
    }
}
