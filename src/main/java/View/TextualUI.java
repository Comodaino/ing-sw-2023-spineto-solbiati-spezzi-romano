package View;

import Distributed.RemotePlayer;

import java.util.Scanner;

public class TextualUI extends ViewInterface {

    private State state;
    private final Scanner input;
    private RemotePlayer player;

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
        switch (this.state) {
            case HOME:
                homePrint(arg);
                break;
            case LOBBY:
                if (player.isOwner()) {
                    if (player.isOwner()) {
                        System.out.println("write /play to start the game");
                    } else
                        System.out.println(player.getNickname() + "wait for the owner to start the game with command /play");
                }


                break;
            case PLAY:
                break;
        }
    }

    public void homePrint(String arg) {
        System.out.println("WELCOME TO MY SHELFIE !/s");
        if (arg.equals(null)) {
            System.out.println("Please insert your nickname here: ");
            client.println(input.nextLine());
            //TODO inserire controllo nickname
        }
    }
}
