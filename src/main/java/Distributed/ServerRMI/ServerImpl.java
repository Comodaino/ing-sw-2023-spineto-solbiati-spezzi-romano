/*
package Distributed.ServerRMI;

import Distributed.ClientRMI.Client;
import Distributed.Lobby;
import Distributed.RemotePlayer;
import Distributed.ServerApp;
import Distributed.States;
import Model.BoardView;
import Model.Player;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import static Distributed.States.*;

//TODO check synchronization
public class ServerImpl extends UnicastRemoteObject implements Server {
    private final ServerApp serverApp;

    public ServerImpl(ServerApp serverApp) throws RemoteException {
        this.serverApp = serverApp;
    }

    public void start() {
        Registry registry = null;
        try {
            registry = LocateRegistry.createRegistry(1099); //Creates RMI Registry
            registry.bind("Server", this); //Binds the name of the server with this server
        } catch (RemoteException | AlreadyBoundException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Server bound and ready");
    }

    @Override
    public void register(Client client) throws RemoteException {
        //Creates a new RemotePlayer and sets its nickname, asking it to the client
        RMIPlayer rp = new RMIPlayer(client);
        String nickname = client.setNickname(serverApp);
        rp.setNickname(nickname);
        //If the lobby is closed, creates a new lobby and sets its ID, adds the player in the list of RemotePlayer of the Lobby the client joined
        this.serverApp.addPlayer(client, rp);
        //Sets the client state in WAIT
        client.setState(WAIT_SETTINGS);
        rp.setState(WAIT_SETTINGS);
    }

    @Override
    public void leave(Client client) throws RemoteException {
        synchronized (serverApp.getLobbies()) {
            for (Lobby l : serverApp.getLobbies()) {
                for (RemotePlayer rp : l.getListOfPlayers()) {
                    if (client.getNickname().equals(rp.getNickname())) {
                        l.getListOfPlayers().remove(rp);
                        client.setState(CLOSE);
                        rp.setState(CLOSE);
                        System.out.println(client.getNickname() + " has left the server");
                        return;
                    }
                }
            }
        }
    }

    @Override
    public void update(Client client, String command) throws RemoteException {
        synchronized (serverApp.getLobbies()) { //TODO: ask if this is necessary, since this method does not modify the list of lobby
            serverApp.getLobbies().get(client.getLobbyID() - 1).getController().update(command);
        }
    }

    @Override
    public String checkNicknameRMI(String nickname) throws RemoteException {
        synchronized (serverApp.getLobbies()) {
            //Iterates on each RemotePlayer in each Lobby and return null if the nickname is already taken
            for (Lobby l : serverApp.getLobbies()) {
                for (RemotePlayer rp : l.getListOfPlayers()) {
                    if (rp.getNickname().equals(nickname)) {
                        return null;
                    }
                }
            }
        }
        System.out.println("OK");
        return nickname;
    }

    @Override
    public boolean closeLobby(Client client) throws RemoteException {
        synchronized (serverApp.getLobbies()) {
            List<RemotePlayer> rps = serverApp.getLobbies().get(client.getLobbyID() - 1).getListOfPlayers();
            for (RemotePlayer rp : rps) {
                if (rp.isOwner() && rp.getNickname().equals(client.getNickname())) {
                    //Searches the client who is trying to close the lobby: if it is the owner of the lobby, then it closes the lobby
                    return serverApp.getLobbies().get(client.getLobbyID() - 1).closeLobby();
                }
            }
        }
        client.printMsg("You cannot close the lobby because you're not the owner");
        return false;
    }

    @Override
    public BoardView getBoardView(Integer lobbyID) {
        synchronized (serverApp.getLobbies()) { //TODO: ask if this is necessary, since this method does not modify the list of lobby
            return (BoardView) serverApp.getLobbies().get(lobbyID-1).getBoardView();
        }
    }

    @Override
    public States myState(Client client) throws RemoteException {
        Lobby lobby = null;
        synchronized (serverApp.getLobbies()){
            lobby = serverApp.getLobbies().get(client.getLobbyID()-1);
        }

        for(RemotePlayer rp: lobby.getListOfPlayers()) {
            if (rp.getNickname().equals(client.getNickname())) {
                return rp.getState();
            }
        }

        return ERROR;
    }

    @Override
    public void waitCommand(Client client, String input) throws RemoteException {
        Lobby lobby = null;
        synchronized (serverApp.getLobbies()){
            lobby = serverApp.getLobbies().get(client.getLobbyID()-1);
        }

        if(input.equals("/leave")){
            leave(client);
        } else if(client.isOwner()){
            switch (input) {
                case "/start":
                    if(closeLobby(client)){
                        lobby.startGame();
                        for(RemotePlayer rp: lobby.getListOfPlayers()){
                            if(rp.getNickname().equals(client.getNickname())){
                                rp.setState(PLAY);
                                client.setState(PLAY);
                            } else {
                                rp.setState(WAIT_TURN);
                            }
                        }
                    } else {
                        client.printMsg("You cannot start the game because there are no other players in the lobby");
                    }
                    break;
                case "/firstMatch":
                    lobby.setFirstMatch(true);
                    break;
                case "/notFirstMatch":
                    lobby.setFirstMatch(false);
                    break;
                case "/closeLobby":
                    closeLobby(client);
                    break;
                default:
                    client.printMsg("Command not valid");
                    break;
            }
        } else { client.printMsg("You're not the owner of the lobby!"); }
    }

    @Override
    public void playCommand(Client client, String input) throws RemoteException {
        Lobby lobby = null;
        synchronized (serverApp.getLobbies()){
            lobby = serverApp.getLobbies().get(client.getLobbyID()-1);
        }

        System.out.println("Received command: " + input);
        lobby.getController().update(input);

        client.setState(WAIT_TURN);
        for(RemotePlayer rp: lobby.getListOfPlayers()) {
            if (rp.getNickname().equals(client.getNickname())) {
                rp.setState(WAIT_TURN);
            }
        }
    }

    @Override
    public void endCommand(Client client) throws RemoteException {
        Lobby lobby = null;
        synchronized (serverApp.getLobbies()){
            lobby = serverApp.getLobbies().get(client.getLobbyID()-1);
        }

    }

}
*/