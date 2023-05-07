package Distributed.ClientSocket;

import Distributed.Lobby;
import Distributed.States;
import Model.BoardView;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Scanner;

public class ClientAppSocket {
    private final int port;
    private List<Lobby> lobbyList;
    private Socket socket;
    private Scanner in;
    private PrintWriter out;
    private BoardView boardView;
    private final String address = "127.0.0.1";
    private final Scanner stdIn = new Scanner(System.in);
    private boolean firstWait;
    private States state;
    ObjectInputStream objIn;

    public ClientAppSocket(int port) {
        this.port = port;
        firstWait = true;
    }

    public static void main(String[] args) throws IOException {
        ClientAppSocket client = new ClientAppSocket(25565);
        client.Connect();
    }

    public void Connect() throws IOException {
        try {
            socket = new Socket(address, port);
            objIn = new ObjectInputStream(socket.getInputStream());
            in = new Scanner(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);


            try {
                boolean spin = true;
                while (spin) {
                    System.out.println("Waiting for response from server");
                    String messageFromServer = in.nextLine();
                    System.out.println("Message from server " + messageFromServer);
                    switch (messageFromServer) {
                        case "/init":
                            state = States.INIT;
                            out.println(stdIn.nextLine());
                            out.flush();
                            break;
                        case "/wait":
                            state = States.WAIT;
                            System.out.println("we are in wait");
                            waitCommand();
                            break;
                        case "/play":
                            state = States.PLAY;
                            System.out.println("play the next move");
                            out.println(stdIn.nextLine());
                            out.flush();
                            break;
                        case "/end":
                            state = States.END;
                            System.out.println("Game over");
                            break;
                        case "/close":
                            state = States.CLOSE;
                            spin = false;
                            in.close();
                            out.close();
                            socket.close();
                            break;
                        case "/update":

                            boardView = (BoardView) objIn.readObject();
                            //TODO IMPLEMENT update();
                            break;
                    }
                    System.out.println("end of current cycle");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (UnknownHostException e) {
            System.err.println("Error connecting to server");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void waitCommand() {
        if(in.nextLine().equals("/true")){
            out.println(stdIn.nextLine());
            out.flush();
        }
    }
}
