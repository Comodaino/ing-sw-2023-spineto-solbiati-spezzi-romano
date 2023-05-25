package Distributed.ClientRMI;

import Distributed.AbstractClient;
import Distributed.ServerRMI.Server;
import Distributed.States;
import Model.BoardView;
import View.State;
import View.TextualUI;
import View.ViewInterface;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class ClientApp extends UnicastRemoteObject implements Client, AbstractClient {
    private String nickname;
    private Integer lobbyID;
    private States state;
    private BoardView boardView;
    private ViewInterface view;
    private Server server;
    private boolean owner;

    public ClientApp(String typeOfView) throws RemoteException {
        nickname = null;
        lobbyID = null;
        server = null;
        boardView = null;
        owner = false;
        state = States.INIT;

        if (typeOfView.equals("TUI")) {
            try {
                this.view = new TextualUI(this);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } //else this.view = new GUIclass(); //TODO implement after GUI
    }


    @Override
    public void println(String arg) {
        try {
            server.handler(this, arg);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update() throws RemoteException {
        try {
            view.update();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(BoardView boardView, String arg) throws RemoteException {
        this.boardView = boardView;
        try {
            view.update(arg);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void run(String serverHost) throws Exception {
        this.server = (Server) Naming.lookup("rmi://" + serverHost + "/Server"); // take a reference of the server from the registry
    }

    public static void main(String args[]) {
        System.out.println("Choose type of view:");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        ClientApp client = null;
        try {
            client = new ClientApp(input);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        try {
            client.run("localhost");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    //SETTER AND GETTER METHODS
    @Override
    public BoardView getBoardView() { return this.boardView; }

    @Override
    public void setNickname(String nickname) throws RemoteException { this.nickname = nickname; }

    @Override
    public String getNickname() throws RemoteException { return this.nickname; }

    @Override
    public void setLobbyID(Integer lobbyID) throws RemoteException { this.lobbyID = lobbyID; }

    @Override
    public Integer getLobbyID() throws RemoteException { return this.lobbyID; }

    @Override
    public void setState(States state) throws RemoteException {
        this.state = state;
        switch (state) {
            case PLAY:
                view.setState(State.PLAY);
                break;
            case WAIT:
                view.setState(State.LOBBY);
                break;
            case END:
                view.setState(State.END);
                break;
            case CLOSE:
                view.setState(State.CLOSE);
                break;
        }
    }

    @Override
    public States getState() throws RemoteException { return this.state; }

    @Override
    public void setOwner(boolean owner) throws RemoteException { this.owner = owner; }

    @Override
    public boolean isOwner() throws RemoteException { return this.owner; }
}
