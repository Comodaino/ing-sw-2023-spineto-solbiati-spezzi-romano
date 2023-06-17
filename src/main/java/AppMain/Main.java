package AppMain;

import Distributed.ClientRMI.ClientApp;
import Distributed.ClientSocket.ClientAppSocket;
import Distributed.ServerApp;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {

        String type = null;
        String ip = null;
        String view = null;
        String connection = null;

        if (args.length != 0){


            for(int i = 0; i< args.length; i+=2) {
                System.out.println("itering args");
                if (args[i].startsWith("-")) {
                    switch (args[i]) {
                        case "-t":
                            if(args[i+1].equals("client") || args[i+1].equals("server")) type = args[i+1];
                            break;
                        case "-i":
                            ip = args[i+1];
                            break;
                        case "-v":
                            if(args[i+1].equals("TUI") || args[i+1].equals("GUI")) view = args[i+1];
                            break;
                        case "-c":
                            if(args[i+1].equals("socket") || args[i+1].equals("RMI")) connection = args[i+1];
                            break;
                    }
                }
            }

        }
        if (args.length == 0 || type == null) {
            System.out.println(">>insert \"server\" or \"client\"");
            Scanner scanner = new Scanner(System.in);
            switch (scanner.nextLine()) {
                case "server":
                    ServerApp.execute();
                    break;
                case "client":
                    clientInput(ip, connection, view);
                    break;
            }
        }else{
            switch (type) {
                case "server":
                    ServerApp.execute();
                    break;
                case "client":
                    System.out.println("client");
                    clientInput(ip, connection, view);
                    break;
            }
        }
    }





    private static void clientInput(String ip, String connection, String view) {

        if(connection == null){
            System.out.println(">>insert \"socket\" or \"RMI\"");
            Scanner scanner = new Scanner(System.in);
            switch (scanner.nextLine()) {
                case "socket":
                    try {
                        ClientAppSocket.execute(ip, view);
                    } catch (IOException | ClassNotFoundException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case "RMI":
                    ClientApp.execute();
                    break;
            }
        }else{
            switch (connection) {
                case "socket":
                    try {
                        System.out.println("socket");
                        ClientAppSocket.execute(ip, view);
                    } catch (IOException | ClassNotFoundException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case "RMI":
                    ClientApp.execute();
                    break;
            }
        }

    }
}
