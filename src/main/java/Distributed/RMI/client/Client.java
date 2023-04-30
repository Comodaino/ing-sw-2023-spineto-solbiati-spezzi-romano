package Distributed.RMI.client;

import Distributed.RMI.common.Message;

import java.rmi.*;

public interface Client extends Remote {
    public void printMsg(Message msg) throws RemoteException;
    public String getNickname() throws RemoteException;
}
