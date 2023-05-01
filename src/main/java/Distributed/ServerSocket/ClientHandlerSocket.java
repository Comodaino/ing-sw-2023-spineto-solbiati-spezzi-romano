package Distributed.ServerSocket;

import Controller.GameControllerSocket;
import Distributed.HandlersType;
import Distributed.Lobby;
import Distributed.RemoteHandler;
import Distributed.RemotePlayer;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.Socket;
import java.util.Scanner;

public class ClientHandlerSocket extends RemoteHandler implements Runnable {
    private final Socket socket;
    private final SocketPlayer player;
    private Scanner in;
    private Writer out;
    private GameControllerSocket gameController;
    private final ObjectOutputStream objOut;

    public ClientHandlerSocket(Socket socket, Lobby lobby) throws IOException {
        this.socket = socket;
        this.state = States.INIT;
        this.lobby = lobby;
        this.type = HandlersType.Socket;
        this.player = new SocketPlayer(socket, this);
        this.objOut = new ObjectOutputStream(socket.getOutputStream());
        this.in = new Scanner(socket.getInputStream());
        this.out = new PrintWriter(socket.getOutputStream());
        lobby.addPlayer(player);
    }

    public void run() {
        try {
            //TODO OUTPUT TO CLIENT IS ONLY FOR DEBUG
            while (!state.equals(States.CLOSE)) {
                switch (state) {
                    case INIT:
                        initCommand();
                        break;
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
            in.close();
            socket.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
    //TODO SERVER MUST CALL CLIENTS UPDATE

    private void initCommand() throws IOException {
        String input;
        do {
            //TODO CHECK SYNCHRONIZED
            out.write("Please insert a unique nickname");
            input = in.nextLine();
        } while (!nicknameChecker(input));
        player.setNickname(input);
        state = States.WAIT;
    }

    private void waitCommand() throws IOException {
        if(in.hasNextLine()) {
            for (RemotePlayer p : lobby.getListOfPlayers()) {
                if (p.isChair() && p.getNickname().equals(player.getNickname())) {
                    switch (in.nextLine()) {
                        case "/start":
                            lobby.startGame();
                            state = States.PLAY;
                            break;
                        case "/firstMatch":
                            lobby.setFirstMatch(true);
                            break;
                        case "/notfirstMatch":
                            lobby.setFirstMatch(false);
                            break;
                        case "/closelobby":
                            lobby.close();
                            break;
                    }
                } else {
                    out.write("Wait for chair player to start the match");
                }
            }
        }
    }

    public void playCommand() throws IOException {
        out.write("Play a command, all commands should start with /");
        gameController.update(this, in.nextLine());
    }
    public void initPlayer() {
        String line = in.nextLine();
    }
    public RemotePlayer getPlayer() {
        return player;
    }
    public void update() throws IOException {
        objOut.writeObject(lobby.getBoardView());
    }
}