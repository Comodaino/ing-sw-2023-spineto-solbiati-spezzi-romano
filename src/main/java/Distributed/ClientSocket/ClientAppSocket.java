package Distributed.ClientSocket;

import Distributed.Lobby;
import Distributed.States;
import Model.BoardView;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class ClientAppSocket {
    private final int port;
    private List<Lobby> lobbyList;
    private Socket socket;
    private Scanner in;
    private PrintWriter out;
    private BoardView boardView;
    private final String address = "127.0.0.1";
    private final Scanner stdIn = new Scanner(System.in);
    private States state;
    ObjectInputStream objIn;

    public ClientAppSocket(int port) {
        this.port = port;
        state = States.INIT;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        ClientAppSocket client = new ClientAppSocket(25565);
        client.Connect();
    }

    public void Connect() throws IOException, InterruptedException {
        socket = new Socket(address, port);
        objIn = new ObjectInputStream(socket.getInputStream());
        in = new Scanner(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        if (in.nextLine().equals("ready")) System.out.println("Client starting");
        new Runnable() {
            @Override
            public void run() {
                try {
                    outputHandler();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        new Runnable() {
            @Override
            public void run() {
                try {
                    inputHandler();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        System.out.println("Handlers created");
        while (state != States.CLOSE) {
            TimeUnit.SECONDS.sleep(1);
        }
        in.close();
        socket.close();

    }

    public void outputHandler() throws InterruptedException {
        while (true) {
            if (state == States.INIT) System.out.println("Insert a unique nickname");
            out.println(stdIn.nextLine());
            out.flush();
            TimeUnit.SECONDS.sleep(1);
        }
    }

    public void inputHandler() throws InterruptedException {
        while (true) {
            String input = in.nextLine();
            if (input.charAt(0) == '/') {
                switch (input) {
                    case "/init":
                        state = States.INIT;
                        break;
                    case "/wait":
                        state = States.WAIT;
                        break;
                    case "/play":
                        state = States.PLAY;
                        break;
                    case "/end":
                        state = States.END;
                        break;
                    case "/close":
                        state = States.CLOSE;
                        break;
                }
            }
            if (input.charAt(0) == '+') {
                String[] message = input.split(" ", 2);
                System.out.print("[" + message[0] + "]: " + message[1]);
            }
            TimeUnit.SECONDS.sleep(1);
        }
    }
}
