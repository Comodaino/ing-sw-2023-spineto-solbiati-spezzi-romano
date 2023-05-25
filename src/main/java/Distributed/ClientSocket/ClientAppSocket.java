package Distributed.ClientSocket;

import Distributed.AbstractClient;
import Distributed.States;
import Model.BoardView;
import View.State;
import View.TextualUI;
import View.ViewInterface;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;


public class ClientAppSocket implements AbstractClient {
    private final int port;
    private boolean owner;
    private Socket socket;
    private Scanner in;
    private PrintWriter out;
    private BoardView boardView;
    private final String address = "127.0.0.1";
    private States state;
    private ObjectInputStream objIn;
    private ViewInterface view;
    private String tmpNickname;
    private String nickname;

    public ClientAppSocket(int port, String typeOfView) throws IOException {
        this.port = port;
        this.tmpNickname = null;
        this.nickname = null;
        this.owner = false;
        if (typeOfView.equals("TUI")) {
            System.out.println("creating TUI");
            this.view = new TextualUI(this);
        } //else this.view = new GUIclass();
        state = States.INIT;
    }

    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        System.out.println("Choose type of view:");
        Scanner scanner = new Scanner(System.in);
        ClientAppSocket client = new ClientAppSocket(25565, scanner.nextLine());
        client.connect();
    }

    public void connect() throws IOException, ClassNotFoundException {
        socket = new Socket(address, port);
        objIn = new ObjectInputStream(socket.getInputStream());
        in = new Scanner(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        String input = (String) objIn.readObject();
        if (input.equals("start")) System.out.println("Client starting");

        while (state != States.CLOSE) {
            inputHandler();
        }
        in.close();
        socket.close();

    }

    public void inputHandler() throws IOException, ClassNotFoundException {
        System.out.println("waiting for input");

        String input  = (String) objIn.readObject();
        System.out.println("RECEIVED: " + input);


        if (input.startsWith("/wait")) {
            String[] tmpInput = input.split(" ");
            if (tmpInput.length > 1 && tmpInput[1].equals("owner")) {
                this.owner = true;
            }
            input = tmpInput[0];
        }
        if(input.startsWith("/message")){
            String tmp = input.substring(8);
            view.addChatMessage(tmp);
        }

        if (input.equals("/nickname")) {
            view.update("/nickname");
        } else {
            if (input.charAt(0) == '/') {
                switch (input) {
                    case "/init":
                        state = States.INIT;
                        view.setState(State.HOME);
                        view.update();
                        break;
                    case "/wait":
                        if (this.nickname == null) {
                            this.nickname = tmpNickname;
                            System.out.println("Set nickname: " + nickname);
                        }
                        state = States.WAIT;
                        view.setClient(this);
                        view.setState(State.LOBBY);
                        view.update();
                        break;
                    case "/play":
                        playCommand();
                        break;
                    case "/end":
                        state = States.END;
                        view.setState(State.LOBBY);
                        view.update();
                        break;
                    case "/close":
                        state = States.CLOSE;
                        view.setState(State.CLOSE);
                        view.update();
                        break;
                    case "/update":
                        this.boardView = (BoardView) objIn.readObject();
                        System.out.println("updating...");
                        break;
                    default:
                        if (input.startsWith("/message")) {
                            System.out.println(input);
                            //TODO update(input);
                        }
                        view.update();
                        break;
                }
            }
        }

        if (input.charAt(0) != '/' && state != States.INIT) {
            System.out.println("unclePear");
            out.println(input);
            out.flush();
        }
    }

    private void playCommand() throws IOException {
        state = States.PLAY;
        view.setState(State.PLAY);
        view.update();

    }


    @Override
    public void println(String arg) {
        if (state.equals(States.INIT)) this.tmpNickname = arg;
        System.out.println("SENDING: " + arg);
        out.println(arg);
        out.flush();
    }


    @Override
    public BoardView getBoardView() {
        return boardView;
    }

    @Override
    public String getNickname() {
        return nickname;
    }

    public boolean isOwner() {
        return owner;
    }
}
