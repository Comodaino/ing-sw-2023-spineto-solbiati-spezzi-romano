package ServerSocket;

import java.io.IOException;
import java.io.Writer;
import java.net.Socket;
import java.util.Scanner;

public class ClientHandlerSocket implements Runnable {
    private Socket socket;
    private States state;
    private Scanner in;
    private Writer out;
    public ClientHandlerSocket(Socket socket) {
        this.socket = socket;
        this.state = States.INIT;
    }
    public void run() {
        try {

            in = new Scanner(socket.getInputStream());

            while(true) {
                if(state.equals(States.END)) break;
                switch(state){
                    case INIT:
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

    public void initPlayer(){
        String line = in.nextLine();

    }
}