package Distributed;

import Model.BoardView;

import java.rmi.RemoteException;

/**
 * AbstractClient is an abstract class which represent a general client: RMI or Socket.
 * @author Nicolò
 */
public interface AbstractClient {
    public void println(String arg);
    boolean isOwner() throws RemoteException;
    BoardView getBoardView();
    String getNickname() throws RemoteException;
}
