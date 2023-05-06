package Distributed.ClientSocket;

import Distributed.Lobby;
import Distributed.States;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.SecureRandom;
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
    public void Connect(){
        try {
            socket = new Socket(ip, port);

            in = new Scanner(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            Scanner stdin = new Scanner(System.in);
            System.out.println("test");
            out.write("ok");
            String messageFromServer = in.nextLine();
            System.out.println("test");
            System.out.println("Message from server " + messageFromServer);

            try {
                boolean spin = true;
                while (spin) {
                    switch (messageFromServer) {
                        case "/init":
                            System.out.println(in.nextLine());
                            String nickname = stdin.nextLine();
                            out.println(nickname);
                            out.flush();
                            break;
                        case "/wait":
                            if (in.hasNextLine())
                                System.out.println(in.nextLine());
                            break;
                        case "/play":
                            System.out.println(in.nextLine());
                            out.println(stdin.nextLine());
                            out.flush();
                        case "/end":
                            System.out.println("Game over");
                        case "/close":
                            in.close();
                            out.close();
                            socket.close();
                        default: break;
                    }
                }
            }
            catch(IOException e){
                System.err.println(e.getMessage());
            }

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
