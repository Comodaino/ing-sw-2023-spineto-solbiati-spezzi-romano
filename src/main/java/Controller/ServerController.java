package Controller;

import Model.Player;

import java.util.List;

public class ServerController {
    private int numOfPlayerInQueue;
    private List<Player> listOfPlayer;
    ServerController(){
        this.numOfPlayerInQueue = 0;
    }
    public void addPlayer(String name){
        Player pl;
        if(numOfPlayerInQueue == 0) pl = new Player(name, true);
        else pl = new Player(name, false);
        listOfPlayer.add(pl);
    }
    public void startMatch(){

    }
    private List<Player> getListOfPlayer(){
        return this.listOfPlayer;
    }
}
