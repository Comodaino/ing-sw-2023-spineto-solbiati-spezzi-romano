package View;

import Distributed.AbstractClient;
import Distributed.ClientSocket.ClientAppSocket;
import Distributed.RemotePlayer;

import java.util.Scanner;

public class TextualUI extends ViewInterface {

    private State state;
    private final Scanner input;
    private RemotePlayer player;
    private AbstractClient client;

    public TextualUI(AbstractClient client) {
        this.player = client.getPlayer();
        this.state = State.HOME;
        this.input = new Scanner(System.in);
        Thread th = new Thread() {
            @Override
            public void run() {
                inputHandler();
            }
        };

        th.start();
    }

    public void inputHandler() {
        System.out.println("write a command/s");
        String command = input.nextLine();
        System.out.println(command);

    }

    public void update(String arg) {
        String command;
        switch (this.state) {
            case HOME:
                System.out.println("WELCOME TO MY SHELFIE !/s");
                homePrint(arg);
                break;
            case LOBBY:
                if (player.isOwner()) {
                    System.out.println("Commands you can use:");
                    System.out.println("/start to start the game");
                    System.out.println("/firstMatch if this is your first match/sOR");
                    System.out.println("/notFirstMatch if you have already played");
                    do {
                        command = input.nextLine();
                        client.println(command);
                    } while (!command.equals("/start"));
                } else{
                        System.out.println("wait for the owner to start the game");
                }
                break;
            case PLAY:
                break;
        }
    }

    public void homePrint(String arg) {
        if (arg.equals("/nickname")) {
            System.out.println("nickname already used /splease insert another nickname:  ");
            client.println(input.nextLine());
        }
        else{
            System.out.println("insert here your nickname:  ");
            client.println(input.nextLine());
        }

    }
}
