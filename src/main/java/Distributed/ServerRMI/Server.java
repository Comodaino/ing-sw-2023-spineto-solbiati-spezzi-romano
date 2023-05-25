package Distributed.ServerRMI;

import Distributed.ClientRMI.Client;
import Distributed.ClientRMI.ClientApp;
import Distributed.RemoteHandler;
import Distributed.States;
import Model.BoardView;

import java.io.IOException;
import java.rmi.*;

public interface Server extends Remote {
    public void handler(Client client, String arg) throws RemoteException;
    public BoardView getBoardView(Client client) throws RemoteException;
}
