package Distributed.ServerSocket;

import Distributed.ClientRMI.Client;
import Distributed.ConnectionType;
import Distributed.RemotePlayer;
import Distributed.States;
import Model.BoardView;
import Model.Player;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;

public class SocketPlayer extends RemotePlayer implements Serializable {
    private final ConnectionType type;
    private Player modelPlayer;
    private String nickname;
    private States state;
    private ClientHandlerSocket handler;
    private Socket socket;
    public SocketPlayer(Socket socket, ClientHandlerSocket remoteHandler,ConnectionType type){
        super(type);
        this.type = type;
        this.socket=socket;
        this.handler = remoteHandler;
        this.state = States.INIT;
        this.modelPlayer=null;
        this.nickname= null;
    }
    public Socket getSocket() {
        return socket;
    }
    public void update(BoardView boardView) throws IOException, InterruptedException {
        handler.update(boardView);
    }
    public void message(String arg){
        handler.message(arg);
    }
    @Override
    public void endMatch() {
        handler.endMatch();
    }

    @Override
    public States getState() {
        return this.state;
    }

    @Override
    public void reconnect(Client client, int id) {
        return;
    }

    @Override
    public void setState(States state) {
        this.state = state;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
}
