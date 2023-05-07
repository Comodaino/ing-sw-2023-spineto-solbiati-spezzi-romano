package Distributed.ClientSocket;

import Distributed.Lobby;
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
    ObjectInputStream objIn;

    public ClientAppSocket(int port) {
        this.port = port;
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
            Scanner stdin = new Scanner(System.in);

            try {
                boolean spin = true;
                while (spin) {
                    String messageFromServer = in.nextLine();
                    System.out.println("Message from server " + messageFromServer);
                    switch (messageFromServer) {
                        case "/init":
                            out.println(stdin.nextLine());
                            out.flush();
                            break;
                        case "/wait":
                            break;
                        case "/play":
                            System.out.println("play the next move");
                            out.println(stdin.nextLine());
                            out.flush();
                            break;
                        case "/end":
                            System.out.println("Game over");
                            break;
                        case "/close":
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


}
