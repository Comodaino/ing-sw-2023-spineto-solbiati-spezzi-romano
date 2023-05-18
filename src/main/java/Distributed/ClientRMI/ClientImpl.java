package Distributed.ClientRMI;

import Distributed.ServerRMI.Server;
import Distributed.States;
import Model.BoardView;
import Model.Player;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;


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
    public String setNickname(Server server) throws RemoteException{
        String nickname = null;
        boolean found = false;

        System.out.println("Please choose a unique nickname");
        do {
            Scanner scanIn = new Scanner(System.in);
            nickname = scanIn.nextLine();

            nickname = server.checkNicknameRMI(nickname);
            if(nickname==null){
                System.out.println("Nickname already chosen! Please be more original and retry");
            }
        } while(nickname==null);

        System.out.println("OK");
        this.nickname = nickname;
        return this.nickname;
    }

    public void run(String serverHost) throws Exception {
        boolean endOfGame = false;
        int numOfAdd, numOfRemove;
        Server server = null;
        // take a reference of the server from the registry
        server = (Server) Naming.lookup("rmi://" + serverHost + "/ServerApp");

        // join
        server.register(this);

        // main loop
        while(!endOfGame){

            try {
                while (!this.state.equals(States.CLOSE)){
                    switch (state) {
                        case WAIT:
                            if (owner) { //TODO: modifica in modo che si possa fare anche /leave per un giocatore non owner
                                System.out.println("Choose a command:");
                                System.out.println("/start, /firstMatch, /notFirstMatch, /closeLobby or /leave");
                                Scanner input = new Scanner(System.in);
                                server.waitCommand(this, input.nextLine());
                            } else {
                                this.state = server.myState(this);
                                System.out.println("Wait for the owner..."); //TODO: write this just one time
                                TimeUnit.SECONDS.sleep(20);
                            }
                            break;

                        case WAIT_TURN:
                            System.out.println("Wait for your turn..."); //TODO: write this just one time
                            TimeUnit.SECONDS.sleep(20);
                            view = server.getBoardView(lobbyID);
                            if(view!=null && view.getCurrentPlayer().getNickname().equals(this.nickname)){
                                this.state = States.PLAY;
                                //TODO server.setRemoteState(States.PLAY);
                            }
                            break;

                        case PLAY:
                            numOfRemove = 0;
                            numOfAdd = 0;
                            while(numOfAdd<1 && numOfRemove<3){
                                System.out.println("Choose a command. You can remove max 3 tiles and add them just one time");
                                System.out.println("/add [column], /remove [row][column]");
                                Scanner input = new Scanner(System.in);
                                String command = input.nextLine();

                                String[] arg = command.split(" ");
                                if (arg[0].charAt(0) == '/') {
                                    switch (arg[0]) {
                                        case "/remove":
                                            numOfRemove++;
                                            break;
                                        case "/add":
                                            numOfAdd++;
                                            break;
                                        default:
                                            System.out.println("Command not valid!");
                                    }
                                }

                                server.playCommand(this, command);
                                view = server.getBoardView(lobbyID);
                                if(view!=null) {
                                    for(Player p: view.getDonePlayers()){
                                        if(p.getNickname().equals(this.nickname)) {
                                            this.state = States.END;
                                            //TODO server.setRemoteState(States.END);
                                        }
                                    }
                                }
                            }
                            break;

                        case END:
                            server.endCommand(this);
                            endOfGame = true;
                            break;
                    }
                }
            } catch (IOException e){
                System.err.println(e.getMessage());
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
