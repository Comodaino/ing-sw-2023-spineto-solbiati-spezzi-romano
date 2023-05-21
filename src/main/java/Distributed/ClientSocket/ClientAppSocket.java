package Distributed.ClientSocket;

import Distributed.AbstractClient;
import Distributed.RemotePlayer;
import Distributed.ServerSocket.SocketPlayer;
import Distributed.States;
import Model.BoardView;
import View.GUIclass;
import View.State;
import View.TextualUI;
import View.ViewInterface;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;


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
    private SocketPlayer player;
    private String tmpNickname;
    private String nickname;

    public ClientAppSocket(int port, String typeOfView) throws IOException {
        this.port = port;
        this.tmpNickname = null;
        this.player = null;
        this.nickname = null;
        this.owner = false;
        if(typeOfView.equals("TUI")) {
            System.out.println("creating TUI");
            this.view = new TextualUI(this);
        }
        else this.view  = new GUIclass();
        state = States.INIT;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Choose type of view:");
        Scanner scanner = new Scanner(System.in);
        ClientAppSocket client = new ClientAppSocket(25565, scanner.nextLine());
        client.connect();
    }

    public void connect() throws IOException, InterruptedException {
        socket = new Socket(address, port);
        objIn = new ObjectInputStream(socket.getInputStream());
        in = new Scanner(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        if (in.nextLine().equals("ready")) System.out.println("Client starting");
        Thread th2 = new Thread() {
            @Override
            public void run() {
                try {
                    inputHandler();
                } catch (InterruptedException | IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        System.out.println("Handlers created");
        th2.start();
        while (state != States.CLOSE) {
            TimeUnit.SECONDS.sleep(1);
        }
        in.close();
        socket.close();

    }

    public void inputHandler() throws InterruptedException, IOException, ClassNotFoundException {
        while (state!=States.CLOSE) {
            System.out.println("waiting for input");
            String input = in.nextLine();
            System.out.println("RECEIVED: " + input);
            if(input.startsWith("/wait")){
                String[] tmpInput = input.split(" ");
                if(tmpInput.length>1 && tmpInput[1].equals("owner")) {
                    this.owner = true;
                    this.player.setOwner();
                }
                input = tmpInput[0];
            }
            if(input.equals("/nickname")){
                view.update("/nickname");
            }else {
                if (input.charAt(0) == '/') {
                    switch (input) {
                        case "/init":
                            state = States.INIT;
                            view.setState(State.HOME);
                            break;
                        case "/wait":
                            if (this.nickname == null) {
                                this.nickname = tmpNickname;
                                System.out.println("Setted nickname: " + nickname);
                            }
                            state = States.WAIT_SETTING;
                            view.setClient(this);
                            view.setState(State.LOBBY);
                            break;
                        case "/play":
                            state = States.PLAY;
                            view.setState(State.PLAY);
                            break;
                        case "/end":
                            state = States.END;
                            view.setState(State.LOBBY);
                            break;
                        case "/close":
                            state = States.CLOSE;
                            view.setState(State.CLOSE);
                            break;
                        case "/update":
                            boardView = (BoardView) objIn.readObject();
                            update(null);
                        default:
                            if(input.startsWith("/message")){
                                System.out.println(input);
                                //TODO update(input);
                            }
                    }
                    view.update(null);
                }
            }

            if (input.charAt(0) != '/' && state!=States.INIT) {
                out.println(input);
            }
        }
    }


    public void update(String arg) throws IOException {
        view.update(arg);
    }

    @Override
    public void println(String arg){
        if(state.equals(States.INIT)) this.tmpNickname = arg;
        out.println(arg);
    }

    @Override
    public RemotePlayer getPlayer() {
        return this.player;
    }

    @Override
    public BoardView getBoardView() {
        return boardView;
    }

    public String getNickname() {
        return nickname;
    }
    public boolean isOwner(){
        return owner;
    }
}
