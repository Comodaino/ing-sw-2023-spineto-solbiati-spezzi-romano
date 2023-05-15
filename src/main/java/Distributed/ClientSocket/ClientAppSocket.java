package Distributed.ClientSocket;

import Distributed.AbstractClient;
import Distributed.Lobby;
import Distributed.RemotePlayer;
import Distributed.States;
import Model.BoardView;
import View.GUIclass;
import View.State;
import View.TextualUI;
import View.ViewInterface;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;


public class ClientAppSocket implements AbstractClient {
    private final int port;
    private List<Lobby> lobbyList;
    private Socket socket;
    private Scanner in;
    private PrintWriter out;
    private BoardView boardView;
    private final String address = "127.0.0.1";
    private States state;
    private ObjectInputStream objIn;
    private ViewInterface view;
    private RemotePlayer player;

    public ClientAppSocket(int port, String typeOfView) throws IOException {
        this.port = port;
        this.player = null;
        if(typeOfView.equals("TUI")) this.view = new TextualUI(this);
        else this.view  = new GUIclass();
        state = States.INIT;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        ClientAppSocket client = new ClientAppSocket(25565, args[0]);
        client.Connect();
    }

    public void Connect() throws IOException, InterruptedException {
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
            System.out.println("input");
            String input = in.nextLine();
            System.out.println("RECEIVED: " + input);
            if (input.charAt(0) == '/') {
                switch (input) {
                    case "/init":
                        state = States.INIT;
                        view.setState(State.HOME);
                        break;
                    case "/wait":
                        if(this.player == null){
                            player = (RemotePlayer) objIn.readObject();
                        }
                        state = States.WAIT;
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
                        update();
                }
            }
            if (input.charAt(0) == '+') {
                String[] message = input.split(" ", 2);
                System.out.print("[" + message[0] + "]: " + message[1]);
            }
        }
    }


    public void update(){}

    @Override
    public void println(String arg){
        out.println(arg);
    }

    @Override
    public RemotePlayer getPlayer() {
        return this.player;
    }
}
