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
import java.util.Scanner;


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
    }

    /**
     * Starts the client
     *
     * @param address ip address of the server
     * @throws InterruptedException
     * @throws ClassNotFoundException
     */
    public static void execute(String address, String typeOfView) throws IOException, InterruptedException, ClassNotFoundException {
        System.out.println("exec");
        if(typeOfView == null){
            System.out.println(">>insert \"TUI\" or \"GUI\"");
            Scanner scanner = new Scanner(System.in);
            ClientAppSocket client = new ClientAppSocket(address, 25565, scanner.nextLine());
            client.connect();
        }else{
            ClientAppSocket client = new ClientAppSocket(address, 25565, typeOfView);
            client.connect();
        }

    }

    private void connect() throws IOException, ClassNotFoundException {
        socket = new Socket(address, port);
        objIn = new ObjectInputStream(socket.getInputStream());
        in = new Scanner(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        String input = (String) objIn.readObject();
        if (input.equals("ready")) {
            System.out.println("Client starting");


            if (typeOfView.equals("TUI")) {
                System.out.println("creating TUI");
                this.view = new TextualUI(this);
            }
            if (typeOfView.equals("GUI")){
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


            while (state != States.CLOSE) {
                inputHandler();
            }
        }
        in.close();
        socket.close();

    }

    private void inputHandler() throws IOException, ClassNotFoundException {
        System.out.println("waiting for input");

        String input = (String) objIn.readObject();
        if (input != null) {

            System.out.println("RECEIVED: " + input);


            if (input.startsWith("/wait")) {
                String[] tmpInput = input.split(" ");
                if (tmpInput.length > 1 &&  (tmpInput[1].equals("owner"))) {
                    this.owner = true;
                }
                input = tmpInput[0];
            }
            if(input.startsWith("/setnickname")){
                String[] tmpInput = input.split(" ");
                this.nickname = tmpInput[1];
            }
            if (input.equals("/nickname")) {
                view.update("/nickname");
            } else {
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
                            state = States.END;
                            view.setState(State.END);
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
                            view.update();
                            break;
                    }
                }
            }
        }

        assert input != null;
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


    /**
     * takes the parameter arg and sends it to the server, in case of a message or "/whisper" it reformats the string correctly
     *
     * @param arg string to send to the server
     */
    @Override
    public void println(String arg) {

        if (!state.equals(States.INIT) && !arg.startsWith("/")) arg = "/message " + nickname + " " + arg;

        if(arg.startsWith("/whisper")) {
            String[] tmp =  arg.split(" ");
            String last = tmp[tmp.length-1];
            String[] tmp2 = new String[tmp.length];
            tmp2[0] = tmp[0];
            tmp2[1] = tmp[1];
            for(int i= 1 ; i < tmp.length - 1; i++ ){
                tmp2[i+1] = tmp[i];
            }
            tmp2[1] = this.nickname;
            arg = tmp2[0];
            for(int i = 1; i< tmp.length; i++){
                arg = arg + " " + tmp2[i];
            }
            arg = arg + " " + last;
        }
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

    public RemotePlayer getPlayer() {
        return null;
    }

}
