package Distributed.RMI.client;

import Distributed.AbstractClient;
import Distributed.ClientRMI.Client;
import Distributed.ServerApp;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class ClientImpl extends UnicastRemoteObject implements Client, AbstractClient {
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
    public String setNickname(ServerApp server) throws RemoteException{
        String nickname = null;
        boolean found = false;

        System.out.println("Please choose a unique nickname");
        do {
            Scanner scanIn = new Scanner(System.in);
            nickname = scanIn.nextLine();

            nickname = server.checkNickname(nickname);
            if(nickname==null){
                System.out.println("Nickname already chosen! Please be more original and retry");
            }
        } while(nickname==null);

        System.out.println("OK");
        this.nickname = nickname;
        return this.nickname;
    }

    public void doJob(String serverHost) throws Exception {
        ServerApp server;
        // take a reference of the server from the registry
        server = (ServerApp) Naming.lookup("rmi://" + serverHost + "/Server");
        // join
        server.register(this);
        // main loop [...]
        //server.leave(this);
    }

    public static void main(String args[]) throws Exception {
        ClientImpl client = new ClientImpl();
        client.doJob("localhost");
    }

    @Override
    public void println(String arg) {

    }
}
