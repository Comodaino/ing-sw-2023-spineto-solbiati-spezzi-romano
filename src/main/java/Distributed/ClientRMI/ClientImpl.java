package Distributed.ClientRMI;

import Distributed.ServerRMI.Server;
import Distributed.ServerApp;
import Distributed.States;
import Model.BoardView;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class ClientImpl extends UnicastRemoteObject implements Client {
    private String nickname;
    private Integer lobbyID;
    private States state;
    private BoardView view;
    private boolean owner;

    public ClientImpl() throws RemoteException {
        nickname = null;
        lobbyID = null;
        view = null;
        owner = false;
        state = States.INIT;
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
    public Integer getLobbyID() throws RemoteException { return this.lobbyID; }

    @Override
    public void setState(States state) throws RemoteException { this.state = state; }

    @Override
    public void setOwner(boolean owner) throws RemoteException { this.owner = owner; }

    @Override
    public boolean isOwner() throws RemoteException { return this.owner; }

    @Override
    public void printMsg(String message){
        System.out.println(message);
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

    public void run(String serverHost) throws Exception {
        Server server = null;
        // take a reference of the server from the registry
        server = (Server) Naming.lookup("rmi://" + serverHost + "/Server");

        // join
        server.register(this);

        // main loop
        while(true){

            try {
                while (!state.equals(States.CLOSE)){
                    switch (state) {

                        case WAIT_SETTINGS:
                            if (owner) { //TODO: modifica in modo che si possa fare anche /leave
                                System.out.println("Choose a command:");
                                System.out.println("/start, /firstMatch, /notFirstMatch, /closeLobby");
                                Scanner input = new Scanner(System.in);
                                server.waitCommand(this, input.nextLine());
                            } else { System.out.println("Wait for the owner..."); } //TODO: write this just one time
                            break;

                        case WAIT_TURN:
                            System.out.println("Wait for your turn..."); //TODO: write this just one time
                            view = server.getBoardView(lobbyID);
                            if(view.getCurrentPlayer().getNickname().equals(this.nickname)){
                                this.state = States.PLAY;
                            }
                            break;

                        case PLAY:
                            System.out.println("Choose a command:");
                            System.out.println("/add, /remove");
                            Scanner input = new Scanner(System.in);
                            server.playCommand(this, input.nextLine());
                            break;

                        case END:
                            server.endCommand(this);
                            break;
                    }
                }
            } catch (IOException e){
                System.err.println(e.getMessage());
            }

            /* Scanner scanIn = new Scanner(System.in);
            switch (scanIn.nextLine()) {
                case "/closeLobby":
                    server.closeLobby(this);
                    break;
                case "/leave":
                    server.leave(this);
                    return;
            } */
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
