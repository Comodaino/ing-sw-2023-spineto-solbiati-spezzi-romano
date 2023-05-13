package Distributed.ClientRMI;
import Distributed.ServerApp;
import Distributed.States;

import java.rmi.*;

public interface Client extends Remote {
    /**
     * Sets the nickname of a client, checking if there are no other players with the same nickname
     * connected to the server
     * @param server the server the client connects to
     */
    public String setNickname(ServerApp server) throws RemoteException;
    public String getNickname() throws RemoteException;
    public void setLobbyID(Integer lobbyID) throws RemoteException;
    public Integer getLobbyID() throws RemoteException;
    public void setState(States state) throws RemoteException;
    public void setOwner(boolean owner) throws RemoteException;
    public boolean isOwner() throws RemoteException;
    public void printMsg(String message) throws RemoteException;
}
