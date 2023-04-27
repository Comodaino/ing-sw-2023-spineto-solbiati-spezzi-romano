package Distributed.ClientSocket;

import Distributed.Lobby;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class ClientAppSocket {
    private List<Lobby> lobbyList;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private int port;

    public ClientAppSocket(int port){
        this.port = port;
    }
    public void Connect() throws IOException {
        try {
            lobbyList = new ArrayList<Lobby>();
            lobbyList.add(new Lobby());
            socket = new Socket(lobbyList.toString(),port);

            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);


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
