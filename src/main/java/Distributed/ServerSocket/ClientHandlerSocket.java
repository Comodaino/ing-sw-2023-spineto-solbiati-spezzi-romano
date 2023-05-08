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

    public ClientHandlerSocket(Socket socket, Lobby lobby, ServerApp serverApp) throws IOException {
        this.socket = socket;
        this.lobby = lobby;
        this.serverApp = serverApp;
        this.type = ConnectionType.SOCKET;
        this.player = new SocketPlayer(socket, this, ConnectionType.SOCKET);
        this.objOut = new ObjectOutputStream(socket.getOutputStream());
        this.in = new Scanner(socket.getInputStream());
        this.out = new PrintWriter(socket.getOutputStream());
        lobby.addPlayer(player);
    }

    /**
     * FSM which executes another method depending on the current state
     */
    public void run() {

        Thread th1 = new Thread(){
            @Override
            public void run() {
                try {
                    inputHandler();
                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        Thread th2 = new Thread(){
            @Override
            public void run() {
                try {
                    outputHandler();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        th1.start();
        th2.start();
        out.println("ready");
        out.flush();
        System.out.println("Message sent");
        if(player.getState() == CLOSE) {
            out.println("/close");
            out.flush();
            in.close();
            try {
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * writes the serializble ModelView to the client's socket
     *
     * @throws IOException
     */
    public void update() {
        try {
            out.println("/update");
            out.flush();
            objOut.writeObject(lobby.getBoardView());
            objOut.flush();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Asks the socket to write a nickname, repeats the operation if the nickname is already used
     *
     * @throws IOException
     */
    private void initCommand(String input) throws IOException {
        System.out.println("INIT");
        System.out.println("Received " + input);
        if (nicknameChecker(input)) {
            System.out.println("Nickname is available");
            player.setNickname(input);
            player.setState(WAIT);
        } else System.out.println("Nickname not available");
    }

    /**
     * filters all input coming from non-chair members of the lobby, the lobby-chair can start the match, close the lobby or set the match as "FIrst Match"
     *
     * @throws IOException
     */
    private void waitCommand(String input) throws IOException, InterruptedException {
        System.out.println("WAIT");
        if (player.isOwner()) {
            switch (input) {
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
        }
    }

    /**
     * fetches the input from the socket and redirects it to the controller using update()
     *
     * @throws IOException
     */

    public void playCommand(String input) throws IOException {
        out.println("Play a command, all commands should start with /");
        out.flush();
        gameController.update(this, input);
    }

    public RemotePlayer getPlayer() {
        return player;
    }

    public synchronized void inputHandler() throws IOException, InterruptedException {
        while (!player.getState().equals(CLOSE)) {
            String input = in.nextLine();
            System.out.println("RECEIVED " + input);
            if(input.charAt(0)=='/') {
                switch (player.getState()) {
                    case INIT:
                        initCommand(input);
                        break;
                    case WAIT:
                        waitCommand(input);
                        break;
                    case PLAY:
                        playCommand(input);
                    case END:
                        endCommand();
                }
            }else{
                lobby.sendMessage(player, input);
            }
            System.out.println(player.getState());
            notifyAll();
        }
    }

    public synchronized void outputHandler() throws InterruptedException {
        while (!player.getState().equals(CLOSE)) {
            switch (player.getState()) {
                case INIT:
                    out.println("/init");
                    out.flush();
                    break;
                case WAIT:
                    out.println("/wait");
                    out.flush();
                    break;
                case PLAY:
                    System.out.println("PLAY");
                    out.println("/play");
                    out.flush();
                    break;
                case END:
                    out.println("/end");
                    out.flush();
                    break;
            }
            this.wait();
        }
    }
}

