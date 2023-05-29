package Distributed;

import Model.BoardView;

import java.rmi.RemoteException;

public interface AbstractClient {
    public void println(String arg);
    boolean isOwner() throws RemoteException;
    BoardView getBoardView();
    String getNickname() throws RemoteException;
}
