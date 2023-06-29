package Distributed.ClientSocket;

import Distributed.AbstractClient;
import Distributed.RemotePlayer;
import Distributed.States;
import Model.BoardView;
import View.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * Client side application for connection using TCP sockets
 *
 * @author Alessio
 */
public class ClientAppSocket implements AbstractClient {
    private final int port;
    private final String typeOfView;
    private boolean owner;
    private Socket socket;
    private Scanner in;
    private PrintWriter out;
    private BoardView boardView;
    private final String address;
    private States state;
    private ObjectInputStream objIn;
    private ViewInterface view;
    private String nickname;
    private boolean beat;
    private Timestamp timeStamp;

    private int endFlag;


    /**
     * Client App constructor for socket connection
     *
     * @param address    ip address of the server
     * @param port       port of the server
     * @param typeOfView type of interface: GUI or TUI
     * @throws IOException
     */
    public ClientAppSocket(String address, int port, String typeOfView) throws IOException {
        if (address == null) this.address = "127.0.0.1";
        else this.address = address;
        this.port = port;
        this.nickname = null;
        this.owner = false;
        this.typeOfView = typeOfView;
        state = States.INIT;
        this.timeStamp = new Timestamp(System.currentTimeMillis());
        this.endFlag = 0;
    }

    /**
     * Starts the client
     *
     * @param address ip address of the server
     * @throws InterruptedException
     * @throws ClassNotFoundException
     */
    public static void execute(String address, String typeOfView) throws IOException, InterruptedException, ClassNotFoundException {
        if (typeOfView == null) {
            System.out.println(">>insert \"TUI\" or \"GUI\"");
            Scanner scanner = new Scanner(System.in);
            ClientAppSocket client = new ClientAppSocket(address, 25565, scanner.nextLine());
            client.connect();
        } else {
            ClientAppSocket client = new ClientAppSocket(address, 25565, typeOfView);
            client.connect();
        }

    }

    private void connect() throws ClassNotFoundException, IOException {
        try {
            socket = new Socket(address, port);

            objIn = new ObjectInputStream(socket.getInputStream());

            in = new Scanner(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            String input = (String) objIn.readObject();


            if (input.equals("ready")) {


                if (typeOfView.equals("TUI")) {
                    this.view = new TextualUI(this);
                }
                if (typeOfView.equals("GUI")) {
                    PassParameters.setClient(this);
                    PassParameters.setState(State.HOME);
                    this.view = new GUIApp();

                    Thread th = new Thread() {
                        @Override
                        public void run() {
                            view.setClient(null);
                        }
                    };
                    th.start();

                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        Thread th = new Thread() {
            @Override
            public void run() {
                double tmpStamp;
                while (true) {
                    try {
                        tmpStamp = timeStamp.getTime();
                        TimeUnit.SECONDS.sleep(10);
                        if (tmpStamp == timeStamp.getTime()) {
                            view.update("disconnected");
                            break;
                        }

                    } catch (InterruptedException | IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        };

        th.start();

        while (state != States.CLOSE && socket.isConnected()) {
            try {
                inputHandler();
            } catch (IOException e) {
                in.close();
                socket.close();
                view.update("disconnected");
                break;
            }
        }

        in.close();
        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void inputHandler() throws IOException, ClassNotFoundException {

        String input = (String) objIn.readObject();
        if (input != null) {

            boolean flag = true;


            if (input.startsWith("/wait")) {
                String[] tmpInput = input.split(" ");
                if (tmpInput.length > 1 && (tmpInput[1].equals("owner"))) {
                    this.owner = true;
                }
                input = tmpInput[0];
            }
            if (input.startsWith("/setnickname")) {
                String[] tmpInput = input.split(" ");
                this.nickname = tmpInput[1];
                flag = false;
            }
            if (input.equals("/nickname")) {
                view.update("/nickname");
                flag = false;
            }
            if (flag) {
                if (input.charAt(0) == '/') {
                    switch (input) {
                        case "/setnickname":

                            break;
                        case "/init":
                            state = States.INIT;
                            view.setState(State.HOME);
                            view.update();
                            break;
                        case "/wait":
                            state = States.WAIT;
                            view.setClient(this);
                            view.setState(State.LOBBY);
                            view.update();
                            break;
                        case "/play":
                            playCommand();
                            view.setState(State.PLAY);
                            break;
                        case "/end":
                            if(endFlag <= 1){
                                endFlag ++;
                                break;
                            }
                            state = States.END;
                            view.setState(State.END);
                            view.update();
                            endFlag = 0;
                            break;
                        case "/close":
                            state = States.CLOSE;
                            view.setState(State.CLOSE);
                            view.update();
                            break;
                        case "/update":
                            this.boardView = (BoardView) objIn.readObject();
                            break;

                        case "/ping":
                            timeStamp = new Timestamp(System.currentTimeMillis());
                            break;
                        default:
                            view.update();
                            break;
                    }
                }
            }
        }

        assert input != null;
        if (input.charAt(0) != '/' && state != States.INIT) {


            out.println(input);
            out.flush();
        }
    }

    private void playCommand() throws IOException {
        state = States.PLAY;
        view.setState(State.PLAY);
        view.update();
    }


    /**
     * takes the parameter arg and sends it to the server, in case of a message or "/whisper" it reformats the string correctly
     *
     * @param arg string to send to the server
     */
    @Override
    public void println(String arg) {

        if (arg.equals("/exit") || arg.equals("/quit")) System.exit(0);

        if (!state.equals(States.INIT) && !arg.startsWith("/")) arg = "/message " + nickname + " " + arg;

        if (arg.startsWith("/whisper")) {
            String[] tmp = arg.split(" ");
            String last = tmp[tmp.length - 1];
            String[] tmp2 = new String[tmp.length];
            tmp2[0] = tmp[0];
            tmp2[1] = tmp[1];
            for (int i = 1; i < tmp.length - 1; i++) {
                tmp2[i + 1] = tmp[i];
            }
            tmp2[1] = this.nickname;
            arg = tmp2[0];
            for (int i = 1; i < tmp.length; i++) {
                arg = arg + " " + tmp2[i];
            }
            arg = arg + " " + last;
        } else {
            if ((arg.startsWith("/remove") || arg.startsWith("/switch")) || arg.startsWith("/add")) {
                String[] tmp = arg.split(" ");
                String newMsg = tmp[0] + " " + nickname + " ";
                for (int i = 1; i < tmp.length; i++) {
                    newMsg += tmp[i] + " ";
                }
                arg = newMsg;
            }
        }

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

    public RemotePlayer getPlayer() {
        return null;
    }

}
