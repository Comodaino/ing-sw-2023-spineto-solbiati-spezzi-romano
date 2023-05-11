package Distributed.RMI.client;

import Distributed.ServerRMI.Server;
import Distributed.ServerApp;
import Distributed.ServerSocket.States;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class ClientImpl extends UnicastRemoteObject implements Client {
    private String nickname;
    private Integer clientID; //maybe it is not necessary because the nickname is already unique
    private Integer lobbyID;
    private States state;

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
    public void setLobbyID(Integer lobbyID){
        this.lobbyID = lobbyID;
    }

    @Override
    public String setNickname(Server server) throws RemoteException{
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

    @Override
    public Integer getClientID() throws RemoteException {
        return this.clientID;
    }

    @Override
    public Integer getLobbyID() throws RemoteException {
        return this.lobbyID;
    }

    @Override
    public void printMsg(String message){
        System.out.println(message);
    }

    public void run(String serverHost) throws Exception {
        Server server = null;
        // take a reference of the server from the registry
        server = (Server) Naming.lookup("rmi://" + serverHost + "/Server");

        // join
        server.register(this);

        // main loop
        while(true){

            /* try {
                while (!state.equals(States.CLOSE)){
                    switch (state) {
                        case WAIT:
                            server.waitCommand(this);
                            break;
                        case PLAY:
                            server.playCommand(this);
                            break;
                        case END:
                            server.endCommand(this);
                            break;
                    }
                }
            } catch (IOException e){
                System.err.println(e.getMessage());
            } */

            Scanner scanIn = new Scanner(System.in);
            switch (scanIn.nextLine()) {
                case "/closeLobby":
                    server.closeLobby(this);
                    break;
                case "/leave":
                    server.leave(this);
                    return;
            }
        }
    }

    public static void main(String args[]) {
        ClientImpl client = null;
        try {
            client = new ClientImpl();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        try {
            client.run("localhost");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
