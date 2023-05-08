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
        Thread th1 = new Thread() {
            @Override
            public void run() {
                try {
                    outputHandler();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        Thread th2 = new Thread() {
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
        th1.start();
        th2.start();
        while (state != States.CLOSE) {
            TimeUnit.SECONDS.sleep(1);
        }
        in.close();
        socket.close();

    }

    public synchronized void outputHandler() throws InterruptedException {
        while (state!=States.CLOSE) {
            System.out.println("output");
            if (state == States.INIT) System.out.println("Insert a unique nickname");
            out.println(stdIn.nextLine());
            out.flush();
            this.wait();
        }
    }

    public synchronized void inputHandler() throws InterruptedException {
        while (state!=States.CLOSE) {
            System.out.println("input");
            String input = in.nextLine();
            System.out.println("RECEIVED: " + input);
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
            this.notifyAll();
        }
    }
}
