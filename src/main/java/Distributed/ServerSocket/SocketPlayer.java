package Distributed.ServerSocket;

import Distributed.RemotePlayer;
import Model.Player;

import java.net.Socket;

public class SocketPlayer extends RemotePlayer {
    private Player modelPlayer;
    private String nickname;
    private final Socket socket;
    public SocketPlayer(Socket socket){
        this.socket=socket;
        this.modelPlayer=null;
    }
    public Socket getSocket() {
        return socket;
    }
}
