package Distributed.RMI.server;

import Distributed.RMI.client.Client;
import Distributed.RMI.common.Message;

import java.rmi.*;

public interface Server extends Remote {
    public void join(Client client) throws RemoteException;
    public void leave(Client client) throws RemoteException;
    public void sendMsg(Message msg) throws RemoteException;
}
