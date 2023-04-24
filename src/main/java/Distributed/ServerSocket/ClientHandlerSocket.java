package Distributed.ServerSocket;

import Controller.GameControllerSocket;
import Distributed.HandlersType;
import Distributed.Lobby;
import Distributed.RemoteHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.Socket;
import java.util.Scanner;
//TODO CREATE A CUSTOM HANDLER FOR CHAIR
public class ClientHandlerSocket extends RemoteHandler implements Runnable{
    private Socket socket;
    private Scanner in;
    private Writer out;
    private String input;
    private GameControllerSocket gameController;

    public ClientHandlerSocket(Socket socket, Lobby lobby) {
        this.socket = socket;
        this.state = States.INIT;
        this.lobby = lobby;
        this.type = HandlersType.Socket;
    }
    private void initCommand(Scanner in, Writer out) throws IOException {
        do{
            out.write("Please insert a unique nickname");
            input = in.nextLine();
        }while(!nicknameChecker(input));
        state=States.WAIT;
    }
    public void run() {
        try {
            //TODO OUTPUT TO CLIENT IS ONLY FOR DEBUG
            in = new Scanner(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream());

            while(true) {
                if(state.equals(States.END)) break;
                switch(state){
                    case INIT: initCommand(in, out);
                        break;
                    case WAIT:
                        break;
                    case PLAY: playCommand(in, out);
                        break;
                }
            }
            in.close();
            socket.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
    //TODO SERVER MUST CALL CLIENTS UPDATE

    public void playCommand(Scanner in, Writer out) throws IOException {
        out.write("Play a command, all commands should start with /");
        gameController.update(this, in.nextLine());
    }
    public void initPlayer(){
        String line = in.nextLine();
    }
}