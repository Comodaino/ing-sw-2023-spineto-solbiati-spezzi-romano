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
    private RemotePlayer player;
    private AbstractClient client;
    public static final String RESET = "\033[0m";

    public TextualUI(AbstractClient client) throws IOException {

        //this.player = client.getPlayer();
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
        update();
        th.start();
    }

    public void inputHandler() throws IOException {
        String command = input.nextLine();
        client.println(command);

    }

    @Override
    public void update(String arg) throws IOException {
        System.out.println("update: " + this.state);
            switch (this.state) {
                case HOME:
                    //String fmt = "%1$4s";
                    //Console cnsl = System.console();
                    //cnsl.printf(fmt, "WELCOME");
                    System.out.println(ConsoleColors.YELLOW_BOLD + "\n" +
                            "\t\t _    _ _____ _     _____ ________  ________   _____ _____  ___  ____   __  _____ _   _ _____ _    ______ _____ _____ _ \n" +
                            "\t\t| |  | |  ___| |   /  __ \\  _  |  \\/  |  ___| |_   _|  _  | |  \\/  \\ \\ / / /  ___| | | |  ___| |   |  ___|_   _|  ___| |\n" +
                            "\t\t| |  | | |__ | |   | /  \\/ | | | .  . | |__     | | | | | | | .  . |\\ V /  \\ `--.| |_| | |__ | |   | |_    | | | |__ | |\n" +
                            "\t\t| |/\\| |  __|| |   | |   | | | | |\\/| |  __|    | | | | | | | |\\/| | \\ /    `--. \\  _  |  __|| |   |  _|   | | |  __|| |\n" +
                            "\t\t\\  /|  / |___| |___| \\__/\\ \\_/ / |  | | |___    | | \\ \\_/ / | |  | | | |   /\\__/ / | | | |___| |___| |    _| |_| |___|_|\n" +
                            "\t\t \\/  |/\\____/\\_____/\\____/\\___/\\_|  |_|____/    \\_/  \\___/  \\_|  |_/ \\_/   \\____/\\_| |_|____/\\_____|_|    \\___/\\____/(_)\n"+ RESET );
                    homePrint(arg);
                    break;
                case LOBBY:
                    if (client.isOwner()) {
                        if (arg!= null && arg.equals("/commands"))
                            System.out.println("command not valid, please try again");
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
                    showYourShelf();
                    showOthersShelf();
                    showCommonGoals();
                    System.out.println("Commands you can use:");
                    System.out.println("/add column  -- add tile in the column of your shelf");
                    System.out.println("/remove row column   -- remove tile[row][column] from the board");
                    inputHandler();
                    if (player != null) System.out.println("your score:\t" + player.getModelPlayer().getScore());
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
    public void update() throws IOException {
        System.out.println("update: " + this.state);
        switch (this.state) {
            case HOME:
                System.out.println(ConsoleColors.YELLOW_BOLD + "\n" +
                        "\t\t _    _ _____ _     _____ ________  ________   _____ _____  ___  ____   __  _____ _   _ _____ _    ______ _____ _____ _ \n" +
                        "\t\t| |  | |  ___| |   /  __ \\  _  |  \\/  |  ___| |_   _|  _  | |  \\/  \\ \\ / / /  ___| | | |  ___| |   |  ___|_   _|  ___| |\n" +
                        "\t\t| |  | | |__ | |   | /  \\/ | | | .  . | |__     | | | | | | | .  . |\\ V /  \\ `--.| |_| | |__ | |   | |_    | | | |__ | |\n" +
                        "\t\t| |/\\| |  __|| |   | |   | | | | |\\/| |  __|    | | | | | | | |\\/| | \\ /    `--. \\  _  |  __|| |   |  _|   | | |  __|| |\n" +
                        "\t\t\\  /|  / |___| |___| \\__/\\ \\_/ / |  | | |___    | | \\ \\_/ / | |  | | | |   /\\__/ / | | | |___| |___| |    _| |_| |___|_|\n" +
                        "\t\t \\/  |/\\____/\\_____/\\____/\\___/\\_|  |_|____/    \\_/  \\___/  \\_|  |_/ \\_/   \\____/\\_| |_|____/\\_____|_|    \\___/\\____/(_)\n"+ RESET );
                System.out.print("Insert your nickname:\t");
                break;
            case LOBBY:
                if (client.isOwner()) {
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
                showYourShelf();
                showOthersShelf();
                showCommonGoals();
                System.out.println("Commands you can use:");
                System.out.println("/add column  -- add tile in the column of your shelf");
                System.out.println("/remove row column   -- remove tile[row][column] from the board");
                inputHandler();
                if (player != null) System.out.println("your score:\t" + player.getModelPlayer().getScore());
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

    private void showCommonGoals() {
        System.out.println("COMMON GOALS:");
        client.getBoardView().getSetOfCommonGoal().forEach((goal) -> System.out.println(goal.getName()));
    }

    private void showOthersShelf() {
        System.out.println("OTHERS' SHELVES:");
        for (Player p : client.getBoardView().getListOfPlayer()) {
            if (!p.getNickname().equals(client.getNickname())) //TODO THIS WAS CHANGED
                System.out.println(p.getNickname() + " SHELF:");
            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 5; j++) {
                    Tile tile = p.getShelf().getTile(i, j);
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

    }

    private void showYourShelf() {
        if (player != null) {
            String tType = null;
            System.out.println("YOUR SHELF:");
            for (int i = 0; i < 6; i++) {
                if(i==0) System.out.println("\t \t======================");
                System.out.print("\t"+ i + "\t");
                for (int j = 0; j < 5; j++) {
                    Tile tile = player.getModelPlayer().getShelf().getTile(i, j);
                    if(j==0){
                        System.out.println("|");
                    }
                    if (tile == null) {
                        System.out.print("   |");
                    } else {
                        switch (tile.getType()) {
                            case ONE:
                                tType = " 1 ";
                                break;
                            case TWO:
                                tType = " 2 ";
                                break;
                            case THREE:
                                tType = " 3 ";
                                break;
                        }
                        switch (tile.getColor()) {
                            case WHITE:
                                System.out.print(ConsoleColors.WHITE_BACKGROUND + ConsoleColors.BLACK + tType + RESET + "|");
                                break;
                            case YELLOW:
                                System.out.print(ConsoleColors.YELLOW_BACKGROUND + ConsoleColors.BLACK + tType + RESET + "|");
                                break;
                            case LIGHTBLUE:
                                System.out.print(ConsoleColors.CYAN_BACKGROUND + ConsoleColors.BLACK + tType + RESET + "|");
                                break;
                            case GREEN:
                                System.out.print(ConsoleColors.GREEN_BACKGROUND + ConsoleColors.BLACK + tType + RESET + "|");
                                break;
                            case BLUE:
                                System.out.print(ConsoleColors.BLUE_BACKGROUND + ConsoleColors.BLACK + tType + RESET + "|");
                                break;
                            case PINK:
                                System.out.print(ConsoleColors.PURPLE_BACKGROUND + ConsoleColors.BLACK + tType + RESET + "|");
                                break;
                        }
                    }
                }
                System.out.println("\n==========================");
            }
        }
        System.out.println("NO SHELF AVAILABLE");
    }

    private void showBoard() {
        System.out.println("\t\t\tBOARD:");
        String tType = null;
        for (int i = 0; i < 9; i++) {
            System.out.print("\t" + i + "\t");
            for (int j = 0; j < 9; j++) {
                if(i==0 && j==0){
                    System.out.println("_____________________________________");
                }
                Tile tile = client.getBoardView().getCell(i, j).getTile();
                if(j==0){
                    System.out.print("|");
                }
                if (tile == (null)) {
                    System.out.print("   |");
                } else {
                    switch (tile.getType()) {
                        case ONE:
                            tType = " 1 ";
                            break;
                        case TWO:
                            tType = " 2 ";
                            break;
                        case THREE:
                            tType = " 3 ";
                            break;
                    }
                    switch (tile.getColor()) {
                        case WHITE:
                            System.out.print(ConsoleColors.WHITE_BACKGROUND + ConsoleColors.BLACK + tType + RESET);
                            break;
                        case YELLOW:
                            System.out.print(ConsoleColors.YELLOW_BACKGROUND + ConsoleColors.BLACK + tType + RESET);
                            break;
                        case LIGHTBLUE:
                            System.out.print(ConsoleColors.CYAN_BACKGROUND + ConsoleColors.BLACK + tType + RESET);
                            break;
                        case GREEN:
                            System.out.print(ConsoleColors.GREEN_BACKGROUND + ConsoleColors.BLACK + tType + RESET);
                            break;
                        case BLUE:
                            System.out.print(ConsoleColors.BLUE_BACKGROUND + ConsoleColors.BLACK + tType + RESET);
                            break;
                        case PINK:
                            System.out.print(ConsoleColors.PURPLE_BACKGROUND + ConsoleColors.BLACK + tType + RESET);
                            break;
                    }
                }
            }
            System.out.println("\n+---+---+---+---+---+---+---+---+---+");
        }System.out.println("\t\t  0   1   2   3   4   5   6   7   8");
    }

    public void homePrint(String arg) throws IOException {
        if (arg != null && arg.equals("/nickname")) {
            System.out.println("nickname already used, please insert another nickname:  ");
            inputHandler();
        } else {
            System.out.println("insert your nickname:  ");
            inputHandler();
        }

    }

    @Override
    public void setState(State state) {
        this.state = state;
    }

    @Override
    public void setClient(AbstractClient client) {
        this.client = client;
        this.player = client.getPlayer();
    }
}
