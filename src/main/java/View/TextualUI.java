package View;

import Distributed.AbstractClient;
import Distributed.RemotePlayer;

import java.io.IOException;
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
        this.client= client;
        Thread th = new Thread() {
            @Override
            public void run() {
                try {
                    inputHandler();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        th.start();
    }

    public void inputHandler() throws IOException {
        System.out.println("write a command\n");
        String command = input.nextLine();
        client.println(command);

    }

    @Override
    public void update(String arg) throws IOException {
        String command;
        switch (this.state) {
            case HOME:
                System.out.println("WELCOME TO MY SHELFIE !\n");
                homePrint(arg);
                break;
            case LOBBY:
                if (player.isOwner()) {
                    System.out.println("Commands you can use:");
                    System.out.println("/start to start the game");
                    System.out.println("/firstMatch if this is your first match\nOR");
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

    public void homePrint(String arg) throws IOException {
        if (arg.equals("/nickname")) {
            System.out.println("nickname already used \nplease insert another nickname:  ");
            client.println(input.nextLine());
        }
        else{
            System.out.println("insert here your nickname:  ");
            client.println(input.nextLine());
        }

    }
}
