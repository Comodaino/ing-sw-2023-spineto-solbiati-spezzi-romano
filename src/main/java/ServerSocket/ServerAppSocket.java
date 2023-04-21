package ServerSocket;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerAppSocket {
    private int port;
    public ServerAppSocket(int port) {
        this.port = port;
    }
    public void startServer() {
        ExecutorService executor = Executors.newCachedThreadPool();
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return;
        }
        System.out.println("Server ready");
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                executor.submit(new ClientHandlerSocket(socket));
            } catch(IOException e) {
                break;
            }
        }
        executor.shutdown();
    }
    public static void main(String[] args) {
        ServerAppSocket server = new ServerAppSocket(25565);
        server.startServer();
    }
}
