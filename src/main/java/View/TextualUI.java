package View;

import Distributed.AbstractClient;
import Distributed.RemotePlayer;
import Model.Player;
import Model.Tile;

import java.io.IOException;
import java.util.Scanner;

public class TextualUI implements ViewInterface {

    private State state;
    private final Scanner input;
    private final RemotePlayer player;
    private final AbstractClient client;

    public TextualUI(AbstractClient client) throws IOException {
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
        update(null);
    }

    public void inputHandler() throws IOException {
        String command = input.nextLine();
        client.println(command);

    }

    @Override
    public void update(String arg) throws IOException {
        switch (this.state) {
            case HOME:
                System.out.println("WELCOME TO MY SHELFIE !\n");
                homePrint(arg);
                break;
            case LOBBY:
                if (player.isOwner()) {
                    if (arg.equals("/commands")) System.out.println("command not valid, please try again");
                    System.out.println("Commands you can use:");
                    System.out.println("/start to start the game");
                    System.out.println("/firstMatch if this is your first match\nOR");
                    System.out.println("/notFirstMatch if you have already played");
                    inputHandler();
                } else System.out.println("wait for the owner to start the game");
                break;
            case PLAY:
                System.out.println("Your turn!");
                showBoard();
                showShelf();
                System.out.println("Commands you can use:");
                System.out.println("/add column  -- add tile in the column of your shelf");
                System.out.println("/remove row column   -- remove tile[row][column] from the board");
                inputHandler();
                break;
            case END:
                String winner = client.getBoardView().getWinner().getNickname();
                System.out.println("SCORES:");
                for (Player p : client.getBoardView().getListOfPlayer()) {
                    System.out.println(p.getNickname() + "\t---->\t" + p.getScore());
                }
                System.out.println("The winner is......");
                System.out.println("\t\t\t\t\t" + winner + "\t\t\t\t\t");
                break;
            case CLOSE:
                System.out.println("The lobby has been closed, thank you for playing!");
                break;
        }
    }

    private void showShelf() {
        System.out.println("YOUR SHELF:");
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                Tile tile = player.getModelPlayer().getShelf().getTile(i, j);
                if (tile == null) {
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
                if (tile == (null)) {
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
        if (arg != null) {
            if (arg.equals("/nickname")) {
                System.out.println("nickname already used, please insert another nickname:  ");
                inputHandler();
            } else {
                System.out.println("insert your nickname:  ");
                inputHandler();
            }
        }
    }

    @Override
    public void setState(State state) {
        this.state = state;
    }
}
