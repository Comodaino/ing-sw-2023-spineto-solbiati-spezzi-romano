import Distributed.ClientRMI.ClientApp;
import Distributed.ClientSocket.ClientAppSocket;
import Distributed.ServerApp;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        System.out.println(">>insert \"server\" or \"client\"");
        Scanner scanner = new Scanner(System.in);


        switch(scanner.nextLine()){
            case "server": ServerApp.execute();
            break;
            case "client":
                if(args.length == 0) clientInput(null);
                else clientInput(args[0]);
            break;
        }
    }

    private static void clientInput(String arg) {
        System.out.println(">>insert \"socket\" or \"RMI\"");
        Scanner scanner = new Scanner(System.in);
        switch(scanner.nextLine()){
            case "socket":
                try {
                    ClientAppSocket.execute(arg);
                } catch (IOException | ClassNotFoundException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "RMI": ClientApp.execute();
                break;
        }
    }
}
