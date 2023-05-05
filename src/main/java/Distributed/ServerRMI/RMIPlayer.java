package Distributed.ServerRMI;

import Distributed.RemotePlayer;
import Model.Player;

public class RMIPlayer extends RemotePlayer {
    private int id;
    private String nickname;
    private Player modelPlayer;
    public RMIPlayer(int id){
        this.id = id;
        this.modelPlayer=null;
        this.nickname= null;
    }
}
