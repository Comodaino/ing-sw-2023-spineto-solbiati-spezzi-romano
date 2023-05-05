package Distributed.ServerSocket;

import Controller.GameController;
import Distributed.*;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.Socket;
import java.util.Scanner;

public class ClientHandlerSocket extends RemoteHandler implements Runnable {
    private final Socket socket;
    private final SocketPlayer player;
    private final ObjectOutputStream objOut;
    private Scanner in;
    private Writer out;
    private final Lobby lobby;
    private GameController gameController;

    public ClientHandlerSocket(Socket socket, Lobby lobby) throws IOException {
        this.socket = socket;
        this.state = States.INIT;
        this.lobby = lobby;
        this.type = ConnectionType.Socket;
        this.player = new SocketPlayer(socket, this);
        this.objOut = new Object.getHandler()OutputStream(socket.getOutputStream());
        this.in = new Scanner(socket.getInputStream());
        this.out = new PrintWriter(socket.getOutputStream());
        lobby.addPlayer(player);
    }

    /**
     * FSM which executes another method depending on the current state
     */
    public void run() {
        try {
            //TODO OUTPUT TO CLIENT IS ONLY FOR DEBUG
            while (!state.equals(States.CLOSE)) {
                switch (state) {
                    case INIT:
                        out.write("/init");
                        initCommand();
                        break;
                    case WAIT:
                        out.write("/wait");
                        waitCommand();
                        break;
                    case PLAY:
                        out.write("/play");
                        playCommand();
                        break;
                    case END:
                        out.write("/end");
                        endCommand();
                        break;
                }
            }
            out.write("/close");
            in.close();
            socket.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Asks the socket to write a nickname, repeats the operation if the nickname is already used
     * @throws IOException
     */
    private void initCommand() throws IOException {
        String input;
        do {
            //TODO CHECK SYNCHRONIZED
            out.write(" .Please insert a unique nickname");
            input = in.nextLine();
        } while (!nicknameChecker(input));
        player.setNickname(input);
        state = States.WAIT;
    }

    /**
     * filters all input coming from non-chair members of the lobby, the lobby-chair can start the match, close the lobby or set the match as "FIrst Match"
     * @throws IOException
     */
    private void waitCommand() throws IOException {

        if (in.hasNextLine()) {
            for (RemotePlayer p : lobby.getListOfPlayers()) {
                if (p.isChair() && p.getNickname().equals(player.getNickname())) {
                    synchronized (lobby) {
                        switch (in.nextLine()) {
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
                    }
                } else {
                    out.write("Wait for chair player to start the match");
                }

            }
        }
    }

    /**
     * fetches the input from the socket and redirects it to the controller using update()
     * @throws IOException
     */

    public void playCommand() throws IOException {
        out.write("Play a command, all commands should start with /");
        gameController.update(this, in.nextLine());
    }
    public RemotePlayer getPlayer() {
        return player;
    }

    /**
     * writes the serializble ModelView to the client's socket
     * @throws IOException
     */
    public void update() throws IOException {
        out.write("/update");
        objOut.writeObject(lobby.getBoardView());
    }
}