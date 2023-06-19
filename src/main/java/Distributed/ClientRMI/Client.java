package Distributed.ClientRMI;
import Distributed.States;
import Model.BoardView;

import java.rmi.*;

/**
 * Client is an interface which extends Remote and represents a "local" (server side) proxy of the client, called skeleton.
 * It represents the client from the point of view of the RMI server and offers a list of methods which can be called by the server.
 * @author Nicolò
 */
public interface Client extends Remote {
    public void update() throws RemoteException;
    public void update(BoardView boardView, String arg) throws RemoteException;
    public void setNickname(String nickname) throws RemoteException;
    public String getNickname() throws RemoteException;
    public void setLobbyID(Integer lobbyID) throws RemoteException;
    public Integer getLobbyID() throws RemoteException;
    public void setState(States state) throws RemoteException;
    public States getState() throws RemoteException;
    public void setOwner(boolean owner) throws RemoteException;
    public boolean isOwner() throws RemoteException;
    public boolean beat() throws RemoteException;
}