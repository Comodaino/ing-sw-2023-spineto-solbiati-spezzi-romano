package Distributed.RMI.client;

import Distributed.RMI.common.Message;
import Distributed.RemotePlayer;

import java.rmi.*;

public interface Client extends Remote {
    public void printMsg(Message msg) throws RemoteException;
    public String getNickname() throws RemoteException;
    public RemotePlayer getPlayer() throws RemoteException;
}
