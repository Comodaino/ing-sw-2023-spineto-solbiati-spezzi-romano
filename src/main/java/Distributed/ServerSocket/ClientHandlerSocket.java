package Distributed.ServerSocket;

import Distributed.*;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.Socket;
import java.util.Scanner;

import static Distributed.States.*;

public class ClientHandlerSocket extends RemoteHandler implements Runnable, Serializable {
    private final Socket socket;
    private final SocketPlayer player;
    private final ObjectOutputStream objOut;
    private final Lobby lobby;
    private Scanner in;
    private PrintWriter out;
    private Object lock;

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
     * Writes the serializable ModelView to the client's socket
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
    private void initCommand(String input){
        System.out.println("INIT");
        System.out.println("Received " + input);
        if (nicknameChecker(input)) {
            System.out.println("Nickname is available");
            player.setNickname(input);
            player.setState(States.WAIT);
            if(player.isOwner()) out.println("/wait owner");
            else out.println("/wait");
            out.flush();
        } else {
            System.out.println("Nickname not available");
        }
    }

    /**
     * Filters all input coming from non-chair members of the lobby, the lobby-chair can start the match, close the lobby or set the match as "First Match"
     *
     * @throws IOException
     */
    private void waitCommand(String input) throws IOException {
        System.out.println("WAIT");
        if (player.isOwner()) {
            switch (input) {
                case "/start":
                    lobby.startGame();
                    player.setState(PLAY);
                    out.println("/play");
                    out.flush();
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
        System.out.println("Received command: " + input);
        gameController.update(input);
    }

    public RemotePlayer getPlayer() {
        return player;
    }

    public synchronized void inputHandler() throws IOException, InterruptedException {
        while (!player.getState().equals(CLOSE)) {
            System.out.println("waiting for input");
            String input = in.nextLine();
            System.out.println("RECEIVED " + input);
            if(input.charAt(0)=='/') {
                switch (player.getState()) {
                    case WAIT:
                        waitCommand(input);
                        break;
                    case PLAY:
                        playCommand(input);
                    case END:
                        endCommand();
                }
            }else {
                if (player.getState().equals(INIT)) {
                    initCommand(input);
                } else lobby.sendMessage(player, input);
            }
            notifyAll();
        }
    }

    public synchronized void outputHandler() throws InterruptedException {
        out.println("/init");
        out.flush();
        while (!player.getState().equals(CLOSE)) {
            this.wait();
            switch (player.getState()) {
                case INIT:
                    out.println("/init");
                    out.flush();
                    break;
                case WAIT:
                    System.out.println("WAIT");
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
        }
    }

    @Override
    public void message(String arg) {
        System.out.println("/message " + arg);
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
                out.println("/play");
                out.flush();
                break;
            case END:
                out.println("/end");
                out.flush();
                break;
        } //TODO CHANGE IMPLEMENTATION ONCE TUI IS FINISHED
    }
}

