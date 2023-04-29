package Distributed.server;

import Distributed.client.Client;
import Distributed.common.Message;

import java.rmi.*;

public interface Server extends Remote {
    public void join(Client client) throws RemoteException;
    public void leave(Client client) throws RemoteException;
    public void sendMsg(Message msg) throws RemoteException;
}
