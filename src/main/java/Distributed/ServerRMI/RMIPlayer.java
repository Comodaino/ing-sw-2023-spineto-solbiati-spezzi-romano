package Distributed.ServerRMI;

import Distributed.ClientRMI.Client;
import Distributed.ConnectionType;
import Distributed.RemotePlayer;
import Model.Player;

public class RMIPlayer extends RemotePlayer {
    private String nickname;
    private Player modelPlayer;
    public RMIPlayer(Client client){
        super(ConnectionType.RMI);
        this.modelPlayer = null;
        this.nickname = null;
    }
}
