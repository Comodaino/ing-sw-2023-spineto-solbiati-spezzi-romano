package Distributed.RMI.server;

import Controller.GameController;
import Distributed.HandlersType;
import Distributed.Lobby;
import Distributed.RMI.client.Client;
import Distributed.RemoteHandler;
import Distributed.RemotePlayer;
import Distributed.ServerSocket.States;

import java.io.IOException;
import java.util.Scanner;

public class ClientHandlerRMI extends RemoteHandler implements Runnable { //TODO
    protected Lobby lobby;
    protected States state;
    protected GameController gameController;
    protected HandlersType type;
    private RemotePlayer player;
    private Client client;
    private Scanner in = new Scanner(System.in);

    public void run() {
        try {
            //TODO OUTPUT TO CLIENT IS ONLY FOR DEBUG
            while (!state.equals(States.CLOSE)){
                switch (state) {
                    case WAIT:
                        waitCommand();
                        break;
                    case PLAY:
                        playCommand();
                        break;
                    case END:
                        endCommand();
                        break;
                }
            }
        } catch (IOException e){
            System.err.println(e.getMessage());
        }
    }

    private void waitCommand() throws IOException {
        if (in.hasNextLine()){
            for (RemotePlayer p : lobby.getListOfPlayers()){
                if (p.isOwner() && p.getNickname().equals(player.getNickname())){
                    switch (in.nextLine()){
                        case "/start":
                            lobby.startGame();
                            state = States.PLAY;
                            break;
                        case "/firstMatch":
                            lobby.setFirstMatch(true);
                            break;
                        case "/notFirstMatch":
                            lobby.setFirstMatch(false);
                            break;
                        case "/closeLobby":
                            lobby.close();
                            break;
                    }
                } else {
                    client.printMsg("Wait for lobby owner to start the match");
                }
            }
        }
    }

    public void playCommand() throws IOException {
        client.printMsg("Play a command, all commands should start with /");
        gameController.update(this, in.nextLine());

        client.printMsg("/update");
        //TODO THIS SHOULD SEND THE SERIALIZED MODELVIEW

    }
}
