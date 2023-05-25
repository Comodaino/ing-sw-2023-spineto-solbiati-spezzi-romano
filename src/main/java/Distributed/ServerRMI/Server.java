package Distributed.ServerRMI;

import Distributed.ClientRMI.Client;
import Distributed.States;
import Model.BoardView;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Server extends Remote {
    /**
     * Registers a client to the server
     * Adds a client to a new lobby (if there is not any lobby open) and creates a model and a controller for that lobby
     * @param client the client to register
     */
    public void register(Client client) throws IOException, InterruptedException;

    /**
     * Checks if there are no other players with the same nickname connected to the server
     * @param nickname the nickname which will be checked
     */
    public String checkNicknameRMI(String nickname) throws RemoteException;

    /**
     * Deletes a client from the server
     * @param client the client to delete
     */
    public void leave(Client client) throws RemoteException;

    /**
     * If the client is the owner of the lobby and there >= 2 players in the lobby, it closes the lobby
     * @param client the client who is trying to close the lobby
     */
    public boolean closeLobby(Client client) throws RemoteException;

    /**
     * Notifies the server that a client has made his move
     * @param client the client who generated the event
     * @param command the move made by the client
     */
    public void update(Client client, String command) throws IOException, InterruptedException;

    /**
     * @param lobbyID the ID of the lobby of the client who is requiring the model view
     * @return BoardView the serializable model view BoardView
     */
    public BoardView getBoardView(Integer lobbyID) throws RemoteException;
    public States myState(Client client) throws RemoteException;
    public void waitCommand(Client client, String input) throws IOException;
    public void playCommand(Client client, String input) throws IOException, InterruptedException;
    public void endCommand(Client client) throws RemoteException;
}
