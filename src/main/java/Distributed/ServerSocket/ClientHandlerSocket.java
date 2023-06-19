package Distributed.ServerSocket;

import Distributed.*;
import Model.BoardView;

import java.io.*;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import static Distributed.States.*;

;

public class ClientHandlerSocket extends RemoteHandler implements Runnable, Serializable {
    private final Socket socket;
    private final SocketPlayer player;
    private final ObjectOutputStream out;
    private Lobby lobby;
    private Scanner in;

    /**
     * Constructor for the Client Handler
     *
     * @param socket    socket connection already activated
     * @param lobby     lobby in which the player is part of
     * @param serverApp reference to the server that manages the connection
     * @throws IOException
     * @throws InterruptedException
     */
    public ClientHandlerSocket(Socket socket, Lobby lobby, ServerApp serverApp) throws IOException, InterruptedException {
        this.socket = socket;
        this.lobby = lobby;
        this.serverApp = serverApp;
        this.type = ConnectionType.SOCKET;
        this.player = new SocketPlayer(socket, this, ConnectionType.SOCKET);
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new Scanner(socket.getInputStream());
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

        th1.start();
        try {
            outSocket("ready");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Message sent");
        while (player.getState() != CLOSE && socket.isConnected()) {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        if (!socket.isConnected()) player.setConnected(false);
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

    /**
     * Writes the serializable ModelView to the client's socket
     *
     * @param boardView board view to send
     * @throws IOException
     * @throws InterruptedException
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
    private void initCommand(String input) throws IOException, InterruptedException {
        System.out.println("INIT");
        System.out.println("Received " + input);

        switch (serverApp.checkNickname(input)) {
            case "true":
                lobby.addPlayer(player);
                System.out.println("Nickname is available");
                player.setNickname(input);
                player.setState(WAIT);
                player.setConnected(true);

                outSocket("/setnickname " + input);
                if (player.isOwner()) outSocket("/wait owner ");
                else outSocket("/wait");

                break;
            case "false":
                System.out.println("Nickname not available");
                outSocket("/nickname");
                break;
            case "reconnected":
                this.lobby = serverApp.getLobby(input);
                player.setNickname(input);
                player.setState(PLAY);
                player.setConnected(true);
                outSocket("/setnickname " + input);
                update(lobby.getBoardView());
                outSocket("/play " + input);
                break;
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

    private void playCommand(String input) throws IOException, InterruptedException {
        System.out.println("Received command: " + input);
        lobby.getController().update(input);
    }

    /**
     * sends the player to the lobby
     */
    @Override
    public void endCommand() {
        player.setState(WAIT);
        try {
            outSocket("/wait");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void inputHandler() throws IOException, InterruptedException {
        while (!player.getState().equals(CLOSE) && socket.isConnected()) {
            System.out.println("waiting for input");
            try {
                String input = in.nextLine();
                System.out.println("RECEIVED " + input);
                if (input.charAt(0) == '/') {
                    switch (player.getState()) {
                        case WAIT:
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
            } catch (NoSuchElementException e){
                player.setConnected(false);
                socket.close();
                break;
            }
        }
    }

    private void outputHandler() throws IOException {
        System.out.println("REFRESH");
        switch (player.getState()) {
            case INIT:
                outSocket("/init");
                break;
            case WAIT:
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

    /**
     * Sends a chat message to the client
     *
     * @param arg
     */
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

    /**
     * Ends the match for the player
     */
    public void endMatch() {
        try {
            outSocket("/end");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private RemotePlayer getPlayer() {
        return player;
    }

}

