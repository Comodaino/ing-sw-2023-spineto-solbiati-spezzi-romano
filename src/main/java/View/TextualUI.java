package View;

import Distributed.AbstractClient;
import Distributed.RemotePlayer;
import Model.BoardView;
import Model.Tile;

import java.io.IOException;
import java.util.Scanner;

public class TextualUI implements ViewInterface {

    private State state;
    private final Scanner input;
    private RemotePlayer player;
    private BoardView boardView;
    private AbstractClient client;

    public TextualUI(AbstractClient client) {
        System.out.println("Created TUI");
        this.player = client.getPlayer();
        this.state = State.HOME;
        this.input = new Scanner(System.in);
        this.client = client;
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
        String command = input.nextLine();
        client.println(command);

    }

    public void update(String arg) throws IOException {
        String command;
        switch (this.state) {
            case HOME:
                System.out.println("WELCOME TO MY SHELFIE !\n");
                homePrint(arg);
                this.state = State.LOBBY;
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
                } else {
                    System.out.println("wait for the owner to start the game");
                    try {
                        wait();   //not sure yet
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                this.state = State.PLAY;
                break;
            case PLAY:
                System.out.println("Your turn!");
                showBoard();
                showShelf();
                System.out.println("Commands you can use:");
                System.out.println("/add");
                System.out.println("/remove");
                inputHandler();
                break;
        }
    }

    private void showShelf() {
        System.out.println("YOUR SHELF:");
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                Tile tile = player.getModelPlayer().getShelf().getTile(i, j);
                if (tile.equals(null)) {
                    System.out.print("    ");
                } else {
                    System.out.print(" " + tile.getColor().name().charAt(0));
                    switch (tile.getType()) {
                        case ONE:
                            System.out.print("1 ");
                            break;
                        case TWO:
                            System.out.print("2 ");
                            break;
                        case THREE:
                            System.out.print("3 ");
                            break;
                    }
                }
                System.out.print("\n");
            }
        }
    }

    private void showBoard() {
        System.out.println("showing the BOARD...");
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                Tile tile = client.getBoardView().getCell(i, j).getTile();
                if (tile.equals(null)) {
                    System.out.print("|    |");
                } else {
                    System.out.print("| " + tile.getColor().name().charAt(0));
                    switch (tile.getType()) {
                        case ONE:
                            System.out.print("1 |");
                            break;
                        case TWO:
                            System.out.print("2 |");
                            break;
                        case THREE:
                            System.out.print("3 |");
                            break;
                    }
                }


            }
            System.out.print("\n");
        }
    }

    public void homePrint(String arg) throws IOException {
        if (arg.equals("/nickname")) {
            System.out.println("nickname already used, please insert another nickname:  ");
            inputHandler();
        } else {
            System.out.println("insert your nickname:  ");
            inputHandler();
        }

    }

    public void setState(State state) {
        this.state = state;
    }

    public void setBoardView(BoardView boardView) {
        this.boardView = boardView;
    }
}
