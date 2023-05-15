package Distributed.ServerRMI;

import Distributed.ClientRMI.Client;
import Distributed.Lobby;
import Distributed.RemoteHandler;
import Distributed.RemotePlayer;
import Distributed.ServerApp;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

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
        RMIPlayer rp = new RMIPlayer(11); //TODO: check the constructor
        String nickname = client.setNickname(serverApp);
        rp.setNickname(nickname);

        //If the lobby is closed, creates a new lobby and sets its ID
        this.serverApp.addPlayer(client, rp);
    }

    @Override
    public void leave(Client client) throws RemoteException {
        synchronized (serverApp.getLobbies()) {
            for (Lobby l : serverApp.getLobbies()) {
                for (RemotePlayer rp : l.getListOfPlayers()) {
                    if (client.getNickname().equals(rp.getNickname())) {
                        l.getListOfPlayers().remove(rp);
                        System.out.println(client.getNickname() + " has left the server");
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void update(RemoteHandler o, Object arg) throws RemoteException { //TODO ClientHandlerRMI instead of RemoteHandler
        o.getGameController().update(arg.toString());
    }

    @Override
    public String checkNickname(String nickname) throws RemoteException {
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
                    //searches the client who is trying to close the lobby: if it is the owner of the lobby, then it closes the lobby
                    return serverApp.getLobbies().get(client.getLobbyID() - 1).closeLobby();
                }
            }
        }
        client.printMsg("You cannot close the lobby because you're not the owner");
        return false;
    }

    @Override
    public void waitCommand(Client client) throws RemoteException {
    }

    @Override
    public void playCommand(Client client) throws RemoteException {
    }

    @Override
    public void endCommand(Client client) throws RemoteException {
    }

}
