package Distributed.RMI.client;

import java.rmi.*;

public interface Client extends Remote {
    public String getNickname() throws RemoteException;
    public void getRemotePlayer() throws RemoteException;
}
