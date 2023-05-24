package Distributed.ClientRMI;
import Distributed.ServerApp;
import Distributed.ServerRMI.Server;
import Distributed.States;
import Model.BoardView;

import java.rmi.*;

public interface Client extends Remote {
    public void update() throws RemoteException;
    public void update(String arg) throws RemoteException;
    public void setNickname(String nickname) throws RemoteException;
    public String getNickname() throws RemoteException;
    public void setLobbyID(Integer lobbyID) throws RemoteException;
    public Integer getLobbyID() throws RemoteException;
    public void setState(States state) throws RemoteException;
    public States getState() throws RemoteException;
    public void setOwner(boolean owner) throws RemoteException;
    public boolean isOwner() throws RemoteException;
}
