package Distributed;

import Model.BoardView;

import java.rmi.RemoteException;

public interface AbstractClient {
    public void println(String arg);
    public RemotePlayer getPlayer();
    public boolean isOwner() throws RemoteException;
    public BoardView getBoardView();
    public String getNickname() throws RemoteException;
}
