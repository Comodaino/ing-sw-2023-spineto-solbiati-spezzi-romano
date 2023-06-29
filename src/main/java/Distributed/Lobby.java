package Distributed;

import Controller.GameController;
import Model.BoardView;
import Model.Player;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 *  Lobby for the game, a hub on which players connects to each other to play a match
 * @author Alessio
 */

public class Lobby {
    private final List<RemotePlayer> lp;
    private Integer ID;
    private boolean open;
    private boolean firstMatch;
    private BoardView boardView;
    private GameController controller;
    private ServerApp serverApp;
    private boolean playing;
    private int maxNumberOfPlayers;

    public Lobby(ServerApp serverApp) {
        this.lp = new ArrayList<RemotePlayer>();
        this.firstMatch = false;
        this.ID = null;
        this.serverApp = serverApp;
        this.maxNumberOfPlayers = 4;

        try {
            controller = new GameController(false, this);
            System.out.println("Created lobby's controller");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.boardView = controller.getBoardView();
        System.out.println("BOARD: " + this.boardView);


        this.open = true;
    }


    /**
     * Adds a player to the lobby
     *
     * @param p tha player that needs to be added
     */
    public void addPlayer(RemotePlayer p) throws IOException, InterruptedException {
        if (open) {
            if (lp.isEmpty()) {
                p.setOwner(); //the first player to join the lobby become the owner
            }
            lp.add(p);
            p.setController(controller);
            controller.addPlayer(new Player(p.getNickname()));
            TimeUnit.MILLISECONDS.sleep(50);
            if (lp.size() >= maxNumberOfPlayers){
                p.setState(States.PLAY);
                p.update(controller.getBoardView());
                startGame();
                this.open = false;
            }else{
                p.setState(States.WAIT);
            }
        }
    }

    /**
     * Remove p from the lobby
     * @param p
     */
    public void removePlayer(RemotePlayer p){

        for(int i = 0; i<lp.size(); i++){
            if(lp.get(i).getNickname().equals(p.getNickname())){
                lp.remove(i);
                this.open = true;
            }
        }

    }

    private boolean closeLobby() {
        if (lp.size() >= 2) {
            this.open = false;
            return true; //lobby closed
        }
        return false; //lobby not closed
    }

    /**
     * Starts the match
     * @throws RemoteException
     */
    public void startGame() throws RemoteException {
        this.open = false;
        controller.startGame();
        for (RemotePlayer rp : lp) {
            rp.setState(States.PLAY);
        }
        this.playing = true;
    }

    /**
     * Ends the match
     */
    public void endMatch() {
        this.playing = false;
        for (RemotePlayer p : lp) {
            p.setState(States.END);
            p.endMatch();
        }
    }

    /**
     * Closes the lobby so no other player can join
     */
    public void close() {
        for (RemotePlayer p : lp) {
            p.setState(States.CLOSE);
        }
        serverApp.removeLobby(this);
    }

    /**
     * Updates every player
     * @throws IOException
     * @throws InterruptedException
     */
    public void updateAll() throws IOException, InterruptedException {
        for (RemotePlayer p : lp) {
            if (p.isConnected()) p.update(boardView);
        }
    }


    public void setFirstMatch(boolean firstMatch) {
        this.firstMatch = firstMatch;
        this.getController().setFM(firstMatch);
    }

    public boolean isFirstMatch() {
        return firstMatch;
    }

    public boolean isOpen() {
        return open;
    }

    public List<RemotePlayer> getListOfPlayers() {
        return lp;
    }

    public BoardView getBoardView() {
        return boardView;
    }

    public void setID(Integer i) {
        this.ID = i;
    }

    public Integer getID() {
        return this.ID;
    }

    public GameController getController() {
        return controller;
    }

    /**
     * Checks and eventually updates if the lobby should be open
     */
    public void checkOpen() {
        if (this.lp.size() == 4) this.open = false;
    }

    public boolean getPlay() {
        return playing;
    }

    public void setMaxNumberOfPlayers(int maxNumberOfPlayers) throws IOException, InterruptedException {
        if(maxNumberOfPlayers > 1 && maxNumberOfPlayers <= 4 ){
            if(maxNumberOfPlayers >= this.getListOfPlayers().size()){
                this.maxNumberOfPlayers = maxNumberOfPlayers;
                if(!this.open && maxNumberOfPlayers > this.getListOfPlayers().size()) this.open=true;

                if(maxNumberOfPlayers == this.getListOfPlayers().size()){
                    startGame();
                    updateAll();
                }
            }
        }
    }
}
