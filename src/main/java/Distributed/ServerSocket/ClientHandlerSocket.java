package Distributed.ServerSocket;

import Distributed.*;
import Model.BoardView;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.Scanner;

import static Distributed.States.*;

;

public class ClientHandlerSocket extends RemoteHandler implements Runnable, Serializable {
    private final Socket socket;
    private final SocketPlayer player;
    private final ObjectOutputStream out;
    private final Lobby lobby;
    private Scanner in;

    public ClientHandlerSocket(Socket socket, Lobby lobby, ServerApp serverApp) throws IOException {
        this.socket = socket;
        this.lobby = lobby;
        this.serverApp = serverApp;
        this.type = ConnectionType.SOCKET;
        this.player = new SocketPlayer(socket, this, ConnectionType.SOCKET);
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new Scanner(socket.getInputStream());
        lobby.addPlayer(player);
    }

    /**
     * FSM which executes another method depending on the current state
     */
    public void run() {

        Thread th1 = new Thread() {
            @Override
            public void run() {
                try {
                    inputHandler();
                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        Thread th2 = new Thread() {
            @Override
            public void run() {
                //outputHandler();

            }
        };
        th1.start();
        th2.start();
        try {
            outSocket("ready");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Message sent");
        if (player.getState() == CLOSE) {
            try {
                outSocket("/close");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
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
    public void update(BoardView boardView) throws IOException, InterruptedException {
        outSocket("/update");

        out.writeObject(boardView);
        out.reset();
        out.flush();

        System.out.println("board sent");

        outputHandler();
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
            player.setState(WAIT_SETTING);
            if (player.isOwner()) outSocket("/wait owner");
            else outSocket("/wait");
        } else {

            System.out.println("Nickname not available");
        }
    }

    /**
     * Filters all input coming from non-chair members of the lobby, the lobby-chair can start the match, close the lobby or set the match as "First Match"
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
            lobby.updateAll();
        }
    }

    /**
     * fetches the input from the socket and redirects it to the controller using update()
     *
     * @throws IOException
     */

    public void playCommand(String input) throws IOException, InterruptedException {
        System.out.println("Received command: " + input);
        lobby.getController().update(input);
    }

    @Override
    public void endCommand() {
        try {
            outSocket("/wait");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        player.setState(WAIT_SETTING);
    }

    public void inputHandler() throws IOException, InterruptedException {
        while (!player.getState().equals(CLOSE)) {
            System.out.println("waiting for input");
            String input = in.nextLine();
            System.out.println("RECEIVED " + input);
            if(input.startsWith("/message")){
                lobby.sendMessage(input);
            } else if (input.charAt(0) == '/') {
                switch (player.getState()) {
                    case WAIT_SETTING:
                        waitCommand(input);
                        break;
                    case PLAY:
                        playCommand(input);
                        break;
                    case END:
                        endCommand();
                        break;
                }
            } else {
                if (player.getState().equals(INIT)) {
                    initCommand(input);
                }
            }

        }
    }

    public void outputHandler() throws IOException {
        System.out.println("REFRESH");
        switch (player.getState()) {
            case INIT:
                outSocket("/init");
                break;
            case WAIT_SETTING:
                System.out.println("WAIT");
                outSocket("/wait");
                break;
            case PLAY:
                System.out.println("PLAY");
                outSocket("/play");
                break;
            case END:
                outSocket("/end");
                break;
        }
    }

    @Override
    public void message(String arg) {
        try {
            outSocket(arg);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void outSocket(String arg) throws IOException {
        try {
            out.writeObject(arg);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        out.reset();
        out.flush();
    }
    public void endMatch(){
        try {
            outSocket("/end");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public RemotePlayer getPlayer() {
        return player;
    }
}

