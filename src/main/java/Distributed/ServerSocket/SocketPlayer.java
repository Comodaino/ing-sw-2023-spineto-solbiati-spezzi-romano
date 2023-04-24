package Distributed.ServerSocket;

import Distributed.RemoteHandler;
import Distributed.RemotePlayer;
import Model.Player;

import java.net.Socket;

public class SocketPlayer extends RemotePlayer {
    private Player modelPlayer;
    private String nickname;
    private final Socket socket;
    public SocketPlayer(Socket socket, RemoteHandler remoteHandler){
        this.socket=socket;
        this.remoteHandler = remoteHandler;
        this.modelPlayer=null;
        this.nickname= null;
    }
    public Socket getSocket() {
        return socket;
    }
}
