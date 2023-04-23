package Distributed.ServerSocket;

import Distributed.Lobby;
import Distributed.RemotePlayer;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.Socket;
import java.util.Scanner;

public class ClientHandlerSocket implements Runnable {
    private final Lobby lobby;
    private Socket socket;
    private States state;
    private Scanner in;
    private Writer out;
    private String input;

    public ClientHandlerSocket(Socket socket, Lobby lobby) {
        this.socket = socket;
        this.state = States.INIT;
        this.lobby = lobby;
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
                    case PLAY:
                        break;
                }
            }
            in.close();
            socket.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private void initCommand(Scanner in, Writer out) throws IOException {
        do{
            out.write("Please insert a unique nickname");
            input = in.nextLine();
        }while(!nicknameChecker(input));
        state=States.WAIT;
    }

    /**
     *
     * @param input
     * @return return true if the input nickname is avaible
     */
    private boolean nicknameChecker(String input) {
        for(RemotePlayer p: lobby.getListOfPlayers()){
            if(p.getNickname().equals(input)) return false;
        }
        return true;
    }

    public void initPlayer(){
        String line = in.nextLine();
    }
}