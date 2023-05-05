package Distributed.ServerSocket;

import Distributed.RemotePlayer;
import Model.Player;

import java.net.Socket;

public class SocketPlayer extends RemotePlayer {
    private Player modelPlayer;
    private String nickname;
    private ClientHandlerSocket handler;
    private final Socket socket;
    public SocketPlayer(Socket socket, ClientHandlerSocket remoteHandler){
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
}
