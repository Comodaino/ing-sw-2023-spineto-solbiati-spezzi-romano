package Distributed.RMI.client;

import Distributed.RMI.server.Server;
import Distributed.ServerSocket.States;

import java.rmi.*;

public interface Client extends Remote {
    /**
     * Sets the nickname of a client, checking if there are no other players with the same nickname
     * connected to the server
     * @param server the server the client connects to
     */
    public String setNickname(Server server) throws RemoteException;
    public String getNickname() throws RemoteException;
    public void setIDs(Integer clientID, Integer lobbyID) throws RemoteException;
    public Integer getClientID() throws RemoteException;
    public Integer getLobbyID() throws RemoteException;
    //public void waiting() throws RemoteException;
    public void printMsg(String message) throws RemoteException;
    public void setState(States state) throws RemoteException;
}
