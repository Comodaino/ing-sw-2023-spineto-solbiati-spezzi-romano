package Distributed.RMI.client;

import Distributed.RemotePlayer;

import java.rmi.*;

public interface Client extends Remote {
    public String getNickname() throws RemoteException;
    public RemotePlayer getRemotePlayer() throws RemoteException;
}
