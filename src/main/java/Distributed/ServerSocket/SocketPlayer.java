package Distributed.ServerSocket;

import Distributed.ConnectionType;
import Distributed.RemotePlayer;
import Model.Player;

import java.io.Serializable;
import java.net.Socket;

public class SocketPlayer extends RemotePlayer implements Serializable {
    private final ConnectionType type;
    private Player modelPlayer;
    private String nickname;
    private ClientHandlerSocket handler;
    private final Socket socket;
    public SocketPlayer(Socket socket, ClientHandlerSocket remoteHandler,ConnectionType type){
        super(type);
        this.type = type;
        this.socket=socket;
        this.handler = remoteHandler;
        this.modelPlayer=null;
        this.nickname= null;
    }
    public Socket getSocket() {
        return socket;
    }
    public void update(){
        handler.update();
    }
    public void message(String arg){
        handler.message(arg);
    }
}
