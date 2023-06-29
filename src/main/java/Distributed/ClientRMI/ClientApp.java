package Distributed.ClientRMI;

import Distributed.AbstractClient;
import Distributed.ServerRMI.Server;
import Distributed.States;
import Model.BoardView;
import View.*;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Timestamp;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * ClientApp is a class which extends UnicastRemoteObject and implements Client and AbstractClient
 * It implements the RMI Client and all the methods through which it communicates with the RMI Server amd the ViewInterface
 *
 * @author Nicolò
 */
public class ClientApp extends UnicastRemoteObject implements Client, AbstractClient {
    private static String address;
    private String nickname;
    private Integer lobbyID;
    private States state;
    private BoardView boardView;
    private ViewInterface view;
    private Server server;
    private boolean owner;
    private double tmpStamp;
    private Timestamp timeStamp;
    private boolean endFlag;

    public ClientApp(String typeOfView, String arg) throws RemoteException {
        nickname = null;
        lobbyID = null;
        server = null;
        boardView = null;
        owner = false;
        state = States.INIT;
        timeStamp = new Timestamp(System.currentTimeMillis());
        if (arg == null) address = "localhost";
        else address = arg;

        if (typeOfView.equals("TUI")) {
            try {
                this.view = new TextualUI(this);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
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


    /**
     * This method is invoked by the view method inputHandler() to catch the input of a client from the keyboard.
     * It invokes the server handler method, passing the input of the client as a parameter.
     *
     * @param arg the input written by the client (caught by the view)
     * @author Nicolò
     */
    @Override
    public void println(String arg) {
        try {
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
                    String newMsg = tmp[0] + " " + this.nickname + " ";
                    for (int i = 1; i < tmp.length; i++) {
                        newMsg += tmp[i] + " ";
                    }
                    arg = newMsg;
                }
            }
            server.handler(this, arg);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * This method is invoked by the server every time that a move is made. It invokes the view update and sets the boardView to the client.
     *
     * @param boardView the serializable boardView
     * @param arg       a message from the server (not always used)
     * @author Nicolò
     */
    @Override
    public void update(BoardView boardView, String arg) throws RemoteException {

        this.boardView = boardView;

        if (this.state == States.END && endFlag) {
            endFlag = false;
            return;
        }
        endFlag = true;
        if (arg == null || arg.length() == 0) {
            try {
                view.update();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                view.update(arg);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


    }

    /**
     * This method asks the Client which type of View does it want;
     * then creates an instance of ClientApp and connects it to the server through the method run().
     *
     * @param typeOfView the type of View can be TUI (textual user interface) or GUI (graphical user interface)
     * @param ip         the IP address the Client connects to
     * @author Nicolò
     */
    public static void execute(String typeOfView, String ip) {

        ClientApp client = null;
        if (typeOfView == null) {
            System.out.println(">>insert \"TUI\" or \"GUI\"");
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();

            try {
                client = new ClientApp(input, ip);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                client = new ClientApp(typeOfView, ip);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }


        try {
            client.run(address);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method take a reference of the Server from the RMI Registry and "connects" the Client to it.
     *
     * @param serverHost the IP address or the name of the Server the Client connects to
     * @author Nicolò
     */
    public void run(String serverHost) throws Exception {
        this.server = (Server) Naming.lookup("rmi://" + serverHost + "/ServerRMI");


        Thread rmiHB = new Thread() {
            @Override
            public void run() {
                while (true) {

                    if (heartBeatServiceClient()) {
                        timeStamp = new Timestamp(System.currentTimeMillis());
                    }

                    try {
                        TimeUnit.SECONDS.sleep(5);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

            }
        };

        Thread rmiHBCheck = new Thread() {
            @Override
            public void run() {
                while (true) {

                    tmpStamp = timeStamp.getTime();
                    try {
                        TimeUnit.SECONDS.sleep(10);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    if (tmpStamp == timeStamp.getTime()) {
                        try {
                            view.update("disconnected");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    }
                }

            }
        };

        rmiHBCheck.start();
        rmiHB.start();

    }

    private boolean heartBeatServiceClient() {
        try {
            return this.server.beat();
        } catch (RemoteException e) {
            return false;
        }
    }

    /**
     * This method is invoked periodically by the Server to check if the Client is still connected.
     * It is used to implement the disconnection resilience.
     *
     * @return true
     * @author Nico
     */
    @Override
    public boolean beat() throws RemoteException {
        return true;
    }


    //SETTER AND GETTER METHODS
    @Override
    public BoardView getBoardView() {
        return this.boardView;
    }

    @Override
    public void setNickname(String nickname) throws RemoteException {
        this.nickname = nickname;
    }

    @Override
    public String getNickname() throws RemoteException {
        return this.nickname;
    }

    @Override
    public void setLobbyID(Integer lobbyID) throws RemoteException {
        this.lobbyID = lobbyID;
    }

    @Override
    public Integer getLobbyID() throws RemoteException {
        return this.lobbyID;
    }

    @Override
    public void setState(States state) throws RemoteException {
        this.state = state;
        switch (state) { //It also sets the state of the view
            case PLAY:
                view.setState(State.PLAY);
                this.endFlag = false;
                break;
            case WAIT:
                view.setState(State.LOBBY);
                break;
            case END:
                view.setState(State.END);
                break;
            case CLOSE:
                view.setState(State.CLOSE);
                break;
        }
    }

    @Override
    public States getState() throws RemoteException {
        return this.state;
    }

    @Override
    public void setOwner(boolean owner) throws RemoteException {
        this.owner = owner;
    }

    @Override
    public boolean isOwner() throws RemoteException {
        return this.owner;
    }
}