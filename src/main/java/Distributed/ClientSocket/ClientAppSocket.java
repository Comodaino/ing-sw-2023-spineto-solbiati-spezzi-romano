package Distributed.ClientSocket;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ClientAppSocket {
    private Socket socket;
    private Scanner in;
    private PrintWriter out;
    private int port;
    private final static String ip = "127.0.0.1";

    public ClientAppSocket(int port){
        this.port = port;
    }
    public void Connect() throws IOException {
        try {
            socket = new Socket(ip, port);

            in = new Scanner(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            String messageFromServer = in.nextLine();
            System.out.println("Message from server " + messageFromServer);
            socket.close();
        } catch (UnknownHostException e){
            System.err.println("Error connecting to server");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static void main(String[] args) throws IOException {
        ClientAppSocket client = new ClientAppSocket(25565);
        client.Connect();
    }


}
