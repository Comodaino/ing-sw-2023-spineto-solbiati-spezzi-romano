package Distributed.ServerSocket;

import Distributed.*;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import static Distributed.States.*;

public class ClientHandlerSocket extends RemoteHandler implements Runnable {
    private final Socket socket;
    private final SocketPlayer player;
    private final ObjectOutputStream objOut;
    private final Lobby lobby;
    private Scanner in;
    private PrintWriter out;
    private boolean firstWait;

    public ClientHandlerSocket(Socket socket, Lobby lobby, ServerApp serverApp) throws IOException {
        this.socket = socket;
        this.lobby = lobby;
        this.serverApp = serverApp;
        this.type = ConnectionType.SOCKET;
        this.player = new SocketPlayer(socket, this, ConnectionType.SOCKET);
        this.objOut = new ObjectOutputStream(socket.getOutputStream());
        this.in = new Scanner(socket.getInputStream());
        this.out = new PrintWriter(socket.getOutputStream());
        this.firstWait = true;
        lobby.addPlayer(player);
    }

    /**
     * FSM which executes another method depending on the current state
     */
    public void run() {

        try {
            System.out.println("Handler Ready");
            out.println("Handler Ready");
            //TODO OUTPUT TO CLIENT IS ONLY FOR DEBUG
            while (!player.getState().equals(CLOSE)) {
                switch (player.getState()) {
                    case INIT:
                        out.println("Please insert a unique nickname");
                        out.println("/init");
                        out.flush();
                        initCommand();
                        break;
                    case WAIT:
                        waitCommand();
                        break;
                    case PLAY:
                        System.out.println("PLAY");
                        out.println("/play");
                        playCommand();
                        break;
                    case END:
                        out.println("/end");
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
     *
     * @throws IOException
     */
    private void initCommand() throws IOException {
        String input;
        System.out.println("INIT");

        input = in.nextLine();
        System.out.println("Received " + input);
        if (!nicknameChecker(input)) {
            while (!nicknameChecker(input)) {
                System.out.println("Asking for nickname");
                out.println("Please retry to insert a unique nickname");
                out.println("/init");
                out.flush();
                input = in.nextLine();
                System.out.println("Received " + input);
            }
        }
        System.out.println("All good, the player has been created ");
        player.setNickname(input);
        player.setState(WAIT);
    }

    /**
     * filters all input coming from non-chair members of the lobby, the lobby-chair can start the match, close the lobby or set the match as "FIrst Match"
     *
     * @throws IOException
     */
    private void waitCommand() throws IOException {
        System.out.println("WAIT");
        out.println("/wait");
        if(firstWait) {
            firstWait = false;
            new Runnable() {
                public void run() {
                    if (in.hasNextLine()) {
                        if (player.isOwner()) {
                            switch (in.nextLine()) {
                                case "/start":
                                    lobby.startGame();
                                    player.setState(PLAY);
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
                            out.write("Wait for owner player to start the match");
                        }
                    }
                }

            };
        }
    }

    /**
     * fetches the input from the socket and redirects it to the controller using update()
     *
     * @throws IOException
     */

    public void playCommand() throws IOException {
        out.println("Play a command, all commands should start with /");
        gameController.update(this, in.nextLine());
    }

    public RemotePlayer getPlayer() {
        return player;
    }

    /**
     * writes the serializble ModelView to the client's socket
     *
     * @throws IOException
     */
    public void update() {
        try {
            out.write("/update");
            objOut.writeObject(lobby.getBoardView());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}