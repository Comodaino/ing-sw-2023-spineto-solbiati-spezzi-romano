package Distributed.client;

import Distributed.common.Message;

import java.rmi.*;

public interface Client extends Remote {
    public void printMsg(Message msg) throws RemoteException;
}
