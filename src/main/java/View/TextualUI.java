package View;

import Distributed.AbstractClient;
import Model.Player;
import Model.Tile;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Scanner;

public class TextualUI implements ViewInterface {

    private State state;
    private final Scanner input;
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

        while(state!=State.CLOSE){
            client.println(input.nextLine());
        }
    }

    @Override
    public void update(String arg) throws IOException {
        System.out.println("update: " + this.state);
            switch (this.state) {
                case HOME:
                    System.out.println(ConsoleColors.RED_BOLD + ConsoleColors.YELLOW_BACKGROUND + "\n" +
                            "\t\t _    _ _____ _     _____ ________  ________   _____ _____  ___  ____   __  _____ _   _ _____ _    ______ _____ _____ _ \n" +
                            "\t\t| |  | |  ___| |   /  __ \\  _  |  \\/  |  ___| |_   _|  _  | |  \\/  \\ \\ / / /  ___| | | |  ___| |   |  ___|_   _|  ___| |\n" +
                            "\t\t| |  | | |__ | |   | /  \\/ | | | .  . | |__     | | | | | | | .  . |\\ V /  \\ `--.| |_| | |__ | |   | |_    | | | |__ | |\n" +
                            "\t\t| |/\\| |  __|| |   | |   | | | | |\\/| |  __|    | | | | | | | |\\/| | \\ /    `--. \\  _  |  __|| |   |  _|   | | |  __|| |\n" +
                            "\t\t\\  /|  / |___| |___| \\__ |/ \\_/ / |  | | |___    | | \\ \\_/ / | |  | | | |   /\\__/ / | | | |___| |___| |    _| |_| |___|_|\n" +
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
                    } else System.out.println("wait for the owner to start the game");
                    break;
                case PLAY:
                    if(client.getNickname().equals(client.getBoardView().getCurrentPlayer().getNickname())) System.out.println("Your turn!");
                    showBoard();
                    showYourShelf();
                    showOthersShelf();
                    showGoals();
                    System.out.println("Commands you can use:");
                    System.out.println("/add column  -- add tile in the column of your shelf");
                    System.out.println("/remove row column   -- remove tile[row][column] from the board");
                    //if (client.getPlayer() != null) System.out.println("your score:\t" + client.getPlayer().getModelPlayer().getScore());
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
                        "\t\t _    _ _____ _     _____ ________  ________   _____ _____   ___  ____   __  _____ _   _ _____ _    ______ _____ _____ _ \n" +
                        "\t\t| |  | |  ___| |   /  __ \\  _  |  \\/  |  ___| |_   _|  _  |  |  \\/  \\ \\ / / /  ___| | | |  ___| |   |  ___|_   _|  ___| |\n" +
                        "\t\t| |  | | |__ | |   | /  \\/ | | | .  . | |__     | | | | | |  | .  . |\\ V /  \\ `--.| |_| | |__ | |   | |_    | | | |__ | |\n" +
                        "\t\t| |/\\| |  __|| |   | |   | | | | |\\/| |  __|    | | | | | |  | |\\/| | \\ /    `--. \\  _  |  __|| |   |  _|   | | |  __|| |\n" +
                        "\t\t\\  /|  / |___| |___| \\__/| \\_/ / |  | | |___    | | \\ \\_/ /  | |  | | | |   /\\__/ / | | | |___| |___| |    _| |_| |___|_|\n" +
                        "\t\t \\/  |/\\____/\\_____/\\____/\\___/\\_|  |_|____/    \\_/  \\___/   \\_|  |_/ \\_/   \\____/\\_| |_|____/\\_____|_|    \\___/\\____/(_)\n"+ RESET );
                System.out.print("Insert your nickname:\t");
                break;
            case LOBBY:
                if (client.isOwner()) {
                    System.out.println("Commands you can use:");
                    System.out.println("/start to start the game");
                    System.out.println("/firstMatch if this is your first match\nOR");
                    System.out.println("/notFirstMatch if you have already played");
                } else System.out.println("wait for the owner to start the game");
                break;
            case PLAY:
                if(client.getNickname().equals(client.getBoardView().getCurrentPlayer().getNickname())) System.out.println("Your turn!");
                showBoard();
                showYourShelf();
                showOthersShelf();
                showGoals();
                System.out.println("Commands you can use:");
                System.out.println("/add column  -- add tile in the column of your shelf");
                System.out.println("/remove row column   -- remove tile[row][column] from the board");
                for(Player p: client.getBoardView().getListOfPlayer()) {
                    if (p.getNickname().equals(client.getNickname()))
                        System.out.println("your score:\t" + p.getScore());
                }break;
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

    private void showGoals() throws RemoteException {
        System.out.print("COMMON GOALS:\t\t");
        client.getBoardView().getSetOfCommonGoal().forEach((goal) -> System.out.println(goal.getName()));
        System.out.print("PRIVATE GOALS:\t\t");
        for (Player p: client.getBoardView().getListOfPlayer()
             ) {
            if (client.getNickname().equals(p.getNickname()))
                System.out.println(p.getGoal().toString() + "\t" + p.getNearGoal().toString()); //todo find a way to show goals

        }
    }

    private void showOthersShelf() throws RemoteException {
        System.out.println("OTHERS' SHELVES:");
        for (Player p : client.getBoardView().getListOfPlayer()) {
            if (!p.getNickname().equals(client.getNickname())) {
                System.out.println(p.getNickname() + " SHELF:");
                String tType = null;
                for (int i = 0; i < 6; i++) {
                    for (int j = 0; j < 5; j++) {
                        if(i==0 && j==0){
                            System.out.println("\t=======================");
                        }
                        if(j==0)
                            System.out.print("\t" +i);
                        Tile tile = p.getShelf().getTile(i, j);
                        if(j==0){
                            System.out.print("||");
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
                                    System.out.print(ConsoleColors.BLACK + ConsoleColors.WHITE_BACKGROUND + tType + RESET + "|");
                                    break;
                                case YELLOW:
                                    System.out.print(ConsoleColors.BLACK + ConsoleColors.YELLOW_BACKGROUND + tType + RESET + "|");
                                    break;
                                case LIGHTBLUE:
                                    System.out.print(ConsoleColors.BLACK + ConsoleColors.CYAN_BACKGROUND +   tType + RESET + "|");
                                    break;
                                case GREEN:
                                    System.out.print(ConsoleColors.BLACK + ConsoleColors.GREEN_BACKGROUND +  tType + RESET + "|");
                                    break;
                                case BLUE:
                                    System.out.print(ConsoleColors.BLACK + ConsoleColors.BLUE_BACKGROUND + tType + RESET + "|");
                                    break;
                                case PINK:
                                    System.out.print(ConsoleColors.BLACK + ConsoleColors.PURPLE_BACKGROUND + tType + RESET + "|");
                                    break;
                            }
                        }if(j==4) System.out.print("|");
                    }
                    System.out.println("\n\t++===+===+===+===+===++");
                }System.out.println("\t   0   1   2   3   4   ");
            }
        }

    }

    private void showYourShelf() throws RemoteException {
        for (Player player: client.getBoardView().getListOfPlayer()) {
            if (player.getNickname().equals(client.getNickname())) {
                System.out.println("YOUR SHELF:");
                String tType = null;
                for (int i = 0; i < 6; i++) {
                    for (int j = 0; j < 5; j++) {
                        if(i==0 && j==0){
                            System.out.println("\t=======================");
                        }
                        if(j==0)
                            System.out.print("\t" +i );
                        Tile tile = player.getShelf().getTile(i, j);
                        if(j==0){
                            System.out.print("||");
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
                                    System.out.print(ConsoleColors.BLACK + ConsoleColors.WHITE_BACKGROUND + tType + RESET + "|");
                                    break;
                                case YELLOW:
                                    System.out.print(ConsoleColors.BLACK + ConsoleColors.YELLOW_BACKGROUND + tType + RESET + "|");
                                    break;
                                case LIGHTBLUE:
                                    System.out.print(ConsoleColors.BLACK + ConsoleColors.CYAN_BACKGROUND +   tType + RESET + "|");
                                    break;
                                case GREEN:
                                    System.out.print(ConsoleColors.BLACK + ConsoleColors.GREEN_BACKGROUND +  tType + RESET + "|");
                                    break;
                                case BLUE:
                                    System.out.print(ConsoleColors.BLACK + ConsoleColors.BLUE_BACKGROUND + tType + RESET + "|");
                                    break;
                                case PINK:
                                    System.out.print(ConsoleColors.BLACK + ConsoleColors.PURPLE_BACKGROUND + tType + RESET + "|");
                                    break;
                            }
                        }if(j==4) System.out.print("|");
                    }
                    System.out.println("\n\t++===+===+===+===+===++");
                }System.out.println("\t   0   1   2   3   4   ");
            }
        }
    }

    private void showBoard() {
        System.out.println("\t\t\t\t\tBOARD:");
        String tType = null;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if(i==0 && j==0){
                    System.out.println("\t\t_____________________________________");
                }
                if(j==0)
                    System.out.print("\t" +i + "\t");
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
                            System.out.print(ConsoleColors.BLACK + ConsoleColors.WHITE_BACKGROUND + tType + RESET + "|");
                            break;
                        case YELLOW:
                            System.out.print(ConsoleColors.BLACK + ConsoleColors.YELLOW_BACKGROUND + tType + RESET + "|");
                            break;
                        case LIGHTBLUE:
                            System.out.print(ConsoleColors.BLACK + ConsoleColors.CYAN_BACKGROUND +   tType + RESET + "|");
                            break;
                        case GREEN:
                            System.out.print(ConsoleColors.BLACK + ConsoleColors.GREEN_BACKGROUND +  tType + RESET + "|");
                            break;
                        case BLUE:
                            System.out.print(ConsoleColors.BLACK + ConsoleColors.BLUE_BACKGROUND + tType + RESET + "|");
                            break;
                        case PINK:
                            System.out.print(ConsoleColors.BLACK + ConsoleColors.PURPLE_BACKGROUND + tType + RESET + "|");
                            break;
                    }
                }
            }
            System.out.println("\n\t\t+---+---+---+---+---+---+---+---+---+");
        }System.out.println("\t\t  0   1   2   3   4   5   6   7   8");
    }

    public void homePrint(String arg) throws IOException {
        if (arg != null && arg.equals("/nickname")) {
            System.out.println("nickname already used, please insert another nickname:  ");
        } else {
            System.out.println("insert your nickname:  ");
        }

    }

    @Override
    public void setState(State state) {
        this.state = state;
    }

    @Override
    public void setClient(AbstractClient client) {
        this.client = client;
       // this.player = client.getPlayer();
    }

    @Override
    public void addChatMessage(String tmp) {

    }
}
