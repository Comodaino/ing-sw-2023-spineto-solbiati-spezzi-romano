package View;

import Distributed.AbstractClient;
import Model.Board;
import Model.CommonGoals.CommonGoal;
import Model.Player;
import Model.Tile;
import Model.Whisper;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.*;

public class TextualUI implements ViewInterface {

    private State state;
    private final Scanner input;
    private AbstractClient client;
    private static final String RESET = "\033[0m";
    private static final int maxMsgLength = 50;
    private int removeSize;

    public String[] msgBuffer;

    public TextualUI(AbstractClient client) throws IOException {

        this.state = State.HOME;
        this.input = new Scanner(System.in);
        this.client = client;
        this.msgBuffer = new String[4];
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

        while(state!=State.CLOSE) {
            String in = input.nextLine();
            if(state!=State.HOME){

                if (in.length() > maxMsgLength) System.out.println("Input too long, maximum character: " + maxMsgLength);
                else if( state == State.PLAY && !correctInput(in)) System.out.println("Command is invalid, please try again");
                else if( state == State.HOME && !client.isOwner()) System.out.println("Please wait for the owner");
                    else client.println(in);



            } else {
                String nick = in;
                if(nick!= null && nick.length()>10) {
                    System.out.println("Nickname too long, please insert a nickname with less than 10 characters");
                }
                else if (nick!= null)
                    client.println(nick);
            }
        }
    }


    @Override
    public void update(String arg) throws IOException {
        //System.out.println("update: " + this.state);
            switch (this.state) {
                case HOME:
                    System.out.println(ConsoleColors.RED_BOLD + "\n"
                            + "\n" + "       █  █                                                        "
                            + "\n" + "       █  ██     █████   █                 █                       "
                            + "\n" + "      ██ ███     █░░░█   █                ██  ████                 "
                            + "\n" + "     ███ ███    ██ █░█  ██                █  ██░░░█                "
                            + "\n" + "     ███████    █░ ███  █░                █  █░   █                "
                            + "\n" + "     ███████    ██     ██░               ██  █   █░                "
                            + "\n" + "     ██░█░███ ██░███   ██████    █████  ██ █████ ░    ██████       "
                            + "\n" + "     ██░░██████░ ░░██ ███░░██  ███░░░█  █  ██░░░ █   ██░░░░█       "
                            + "\n" + "    ███░ ██░░█ ██  ░█ █░░ ██░ ███████░ ██  █░   █░  ███████░       "
                            + "\n" + "   ███░  ██░█░██   ██ █   █░ ██░░░░░░  █  ██   ██  ██░░░░░░        "
                            + "\n" + "    ░░  ██░██░██████ ██  ██░ ░██████  ██ ██░  ██░  ░██████         "
                            + "\n" + "        ██░█ █░░░░░░ ░░  ░░   ░░░░░░  ░░ ░░   ░░    ░░░░░░         "
                            + "\n" + "█ █████ █░   ██░ █████████████████████████████████████████████ ██ █"
                            + "\n" + "░ ░░░░░ ██   ██░ ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░ ░░ ░"
                            + "\n" + "        ░█████░                                                    "
                            + "\n" + "         ░░░░                                                      ");



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
                    } else {
                        System.out.println(ConsoleColors.CYAN_UNDERLINED + "wait for the owner to start the game" + RESET);
                    }
                    break;
                case PLAY:
                    if (client.getBoardView().getListOfPlayer().size() == 1) {
                        nb();
                        break;
                    }

                    System.out.println("Your turn!");
                    showBoard();
                    showYourShelf();
                    showOthersShelf();
                    showGoals();
                    System.out.println("Commands you can use:");
                    System.out.println("/add column  -- add tile in the column of your shelf");
                    System.out.println("/remove row column   -- remove tile[row][column] from the board");
                    break;
                case END:
                    String winner = client.getBoardView().getWinner().getNickname();
                    System.out.println("SCORES:");
                    for (Player p : client.getBoardView().getListOfPlayer()) {
                        System.out.println(p.getNickname() + "\t---->\t" + p.getScore());
                    }
                    System.out.println("The winner is......");
                    System.out.println("\t\t\t\t\t"+ ConsoleColors.BLACK_BOLD + ConsoleColors.PURPLE_BACKGROUND_BRIGHT + winner + RESET +"\t\t\t\t\t");
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
                        "\t\t \\/  |/\\____/\\_____/\\____/\\___/\\_|  |_|____/    \\_/  \\___/   \\_|  |_/ \\_/   \\____/\\_| |_|____/\\_____|_|    \\___/\\____/(_)\n" + RESET);
                System.out.print("Insert your nickname:\t");
                break;
            case LOBBY:
                if (client.isOwner()) {
                    System.out.println("COMMANDS AVAILABLE:");
                    System.out.println(ConsoleColors.GREEN_UNDERLINED + "/start"+ RESET + " to start the game");
                    System.out.println(ConsoleColors.GREEN_UNDERLINED + "/firstMatch"+ RESET + " if this is your first match\t\tOR\t\t" + ConsoleColors.GREEN_UNDERLINED +"/notFirstMatch" + RESET + " if you have already played");
                } else
                    System.out.println(ConsoleColors.CYAN_UNDERLINED + "wait for the owner to start the game" + RESET);
                break;
            case PLAY:
                if (client.getBoardView().getListOfPlayer().size() == 1) {
                    nb();
                    break;
                }

                Player currentPlayer = null;
                for(Player p : client.getBoardView().getListOfPlayer()){
                    if(p.getNickname().equals(client.getBoardView().getCurrentPlayer().getNickname())){
                        currentPlayer = p;
                    }
                }
                if (client.getNickname().equals(currentPlayer.getNickname())){
                    System.out.println("Your turn!");
                    showBoard();
                    showYourShelf();
                    showOthersShelf();
                    tileBuffer();
                    showGoals();
                    showYourScore();
                    System.out.println("\t\t\t" + ConsoleColors.GREEN_UNDERLINED + "COMMANDS AVAILABLE:" + RESET);
                    System.out.println(ConsoleColors.GREEN_UNDERLINED + "/add C" + RESET + "  ---> to add a tile in the column C of your shelf");
                    System.out.println(ConsoleColors.GREEN_UNDERLINED + "/remove row column" + RESET + "  ---> to remove the tile[row][column] from the board");
                    System.out.println(ConsoleColors.GREEN_UNDERLINED + "/remove row1 column1 [row2 column2] [row3 column3]" + RESET + "  ---> to remove 2 or 3 tiles from the board\n");
                    chat();
                }
                else{
                    System.out.println(client.getBoardView().getCurrentPlayer().getNickname() + " is playing...Wait your turn!");
                    showBoard();
                    showYourShelf();
                    showOthersShelf();
                    showGoals();
                    showYourScore();
                    chat();
                }
              break;
            case END:
                String winner;
                System.out.println("SCORES:");
                for (Player p : client.getBoardView().getListOfPlayer()) {
                    System.out.println(p.getNickname() + "\t---->\t" + p.getScore());
                }
                if(client.getBoardView().getWinner()!=null)
                {
                    winner = client.getBoardView().getWinner().getNickname();
                    System.out.println("The winner is......");
                    System.out.println("\t\t\t\t\t" + ConsoleColors.BLACK_BOLD + ConsoleColors.PURPLE_BACKGROUND_BRIGHT + winner + RESET + "\t\t\t\t\t");
                }else System.out.println("Error: winner is null");
                break;
            case CLOSE:
                System.out.println("The lobby has been closed, thank you for playing!");
                break;
        }
    }

    private void chat() throws RemoteException {
        System.out.println(ConsoleColors.PURPLE_UNDERLINED + "CHAT:" + RESET);
        for(String s: client.getBoardView().getChatBuffer()){
            System.out.println("--" + s);
        }
        for(Whisper s: client.getBoardView().getPersonalChatBuffer()){
            if(s.getRecipient().equals(client.getNickname())) System.out.println("--" + s.getContent());
        }
    }

    private void showYourScore() throws RemoteException {
        for(Player p: client.getBoardView().getListOfPlayer()) {
            if (p.getNickname().equals(client.getNickname()))
                System.out.println(ConsoleColors.PURPLE_UNDERLINED + "YOUR SCORE:\t" + p.getScore() + RESET);
        }
    }

    private void showGoals() throws RemoteException {
        System.out.print("COMMON GOALS:\t\t");
        client.getBoardView().getSetOfCommonGoal().forEach((goal) -> System.out.println(goal.getName()));
        System.out.print("PRIVATE GOALS:\t\t");
        for (Player p: client.getBoardView().getListOfPlayer()
             ) {
            if (client.getNickname().equals(p.getNickname()))
                System.out.println(p.getGoal().toString() + "\n Adjacent tiles"); //todo find a way to show goals

        }
    }

    private void showOthersShelf() throws RemoteException {
        System.out.println("OTHERS' SHELVES:");
        for (Player p : client.getBoardView().getListOfPlayer()) {
            if (!p.getNickname().equals(client.getNickname())) {
                System.out.println(ConsoleColors.BLUE_BOLD + p.getNickname());
                String tType = null;
                for (int i = 5; i >=0; i--) {
                    for (int j = 0; j < 5; j++) {
                        if(i==5 && j==0){
                            System.out.println("  " + ConsoleColors.BLACK_BOLD + ConsoleColors.RED_BACKGROUND + "=======================" + RESET);
                        }
                        if(j==0)
                            System.out.print(i + " ");
                        Tile tile = p.getShelf().getTile(i, j);
                        if(j==0){
                            System.out.print(ConsoleColors.BLACK_BOLD + ConsoleColors.RED_BACKGROUND +"||" + RESET);
                        }
                        if (tile == (null)) {
                            System.out.print("   " + ConsoleColors.BLACK_BOLD + ConsoleColors.RED_BACKGROUND + "|" + RESET);
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
                        }if(j==4) System.out.print(ConsoleColors.BLACK_BOLD + ConsoleColors.RED_BACKGROUND +"|" + RESET);
                    }
                    System.out.println("\n  " + ConsoleColors.BLACK_BOLD + ConsoleColors.RED_BACKGROUND + "++===+===+===+===+===++" + RESET);
                }System.out.println("\t 0   1   2   3   4   ");
            }
        }

    }

    private void showYourShelf() throws RemoteException {
        for (Player player: client.getBoardView().getListOfPlayer()) {
            if (player.getNickname().equals(client.getNickname())) {
                System.out.println("YOUR SHELF:");
                String tType = null;
                for (int i = 5; i >=0; i--) {
                    for (int j = 0; j < 5; j++) {
                        if(i==5 && j==0){
                            System.out.println("  " + ConsoleColors.BLACK_BOLD + ConsoleColors.RED_BACKGROUND + "=======================" + RESET);
                        }
                        if(j==0)
                            System.out.print(i + " ");
                        Tile tile = player.getShelf().getTile(i, j);
                        if(j==0){
                            System.out.print(ConsoleColors.BLACK_BOLD + ConsoleColors.RED_BACKGROUND +"||" + RESET);
                        }
                        if (tile == (null)) {
                            System.out.print("   " + ConsoleColors.BLACK_BOLD + ConsoleColors.RED_BACKGROUND + "|" + RESET);
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
                        }if(j==4) System.out.print(ConsoleColors.BLACK_BOLD + ConsoleColors.RED_BACKGROUND +"|" + RESET);
                    }
                    System.out.println("\n  " + ConsoleColors.BLACK_BOLD + ConsoleColors.RED_BACKGROUND + "++===+===+===+===+===++" + RESET);
                }System.out.println("\t 0   1   2   3   4   ");
            }
        }
    }

    private void showBoard() {
        String tType = null;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if(i==0 && j==0){
                    System.out.println("\t\t_____________________________________");
                }
                if(j==0)
                    System.out.print("\t" + i + "\t");
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

    public void homePrint(String arg){
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
    }


    public void tileBuffer(){
        System.out.print(ConsoleColors.BLUE_UNDERLINED + "TILE BUFFER:" + RESET + "\t\t");
        String tType = null;
        if(client.getBoardView().getTileBuffer().size() == 0){
            System.out.println("empty");
        } else {
            for (int i = 0; i < client.getBoardView().getTileBuffer().size(); i++) {
                Tile tile = client.getBoardView().getTileBuffer().get(i);
                if (tile == (null)) {
                    System.out.print(" - ");
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
                            System.out.print(ConsoleColors.BLACK_BOLD + ConsoleColors.WHITE_BACKGROUND + tType + RESET + "\t");
                            break;
                        case YELLOW:
                            System.out.print(ConsoleColors.BLACK_BOLD + ConsoleColors.YELLOW_BACKGROUND + tType + RESET + "\t");
                            break;
                        case LIGHTBLUE:
                            System.out.print(ConsoleColors.BLACK_BOLD + ConsoleColors.CYAN_BACKGROUND +   tType + RESET + "\t");
                            break;
                        case GREEN:
                            System.out.print(ConsoleColors.BLACK_BOLD + ConsoleColors.GREEN_BACKGROUND +  tType + RESET + "\t");
                            break;
                        case BLUE:
                            System.out.print(ConsoleColors.BLACK_BOLD + ConsoleColors.BLUE_BACKGROUND + tType + RESET + "\t");
                            break;
                        case PINK:
                            System.out.print(ConsoleColors.BLACK_BOLD + ConsoleColors.PURPLE_BACKGROUND + tType + RESET + "\t");
                            break;
                    }
                }
            }
            System.out.println();
        }
    }

    private void nb(){
        System.out.println(ConsoleColors.RED_BOLD

                + "\n" + "        ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣴⡿⣦⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣰⠟⠻⣤⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀"
                + "\n" + "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣼⠏⠀⠙⠻⣦⠀⠀⢀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣴⠏⠀⠀⠈⠻⣄⠀⠀⠀⠀⠀⢀⣠⣤⣶⣶⣶⣶⣤⡄⠀⠀⠀⠀"
                + "\n" + "⠀⠀⠀⠀⢰⡖⠒⠶⠤⣤⣤⣀⠀⠀⠀⠀⣼⠏⠀⠀⠀⣠⡾⠳⣤⡏⠉⠙⡆⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸⠏⠀⠀⠀⠀⠀⢹⡄⠀⣠⡶⠞⠋⠉⠉⠀⠀⠀⢈⣿⡇⠀⠀⠀⠀"
                + "\n" + "⠀⠀⠀⠠⣿⠀⠀⠀⠀⠀⠈⠉⠛⠷⣤⣠⡿⠀⠀⠀⣰⢿⡀⠀⠈⠀⢀⣤⠿⠒⠛⠛⠛⠛⠛⠛⠛⠓⠲⠶⢄⣼⣀⠀⠀⠀⠀⠀⢈⡿⠚⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⣿⡇⠀⠀⠀⠀"
                + "\n" + " ⠀⠀⠀⠀⣿⠀⠀⠀⠀⠀⠀⠀⠀⠀⠌⠻⣇⠀⠀⣼⠋⠀⣩⡷⠦⠶⠋⠁⠀⠀⠀⠀⠀⠀⠀⣀⡀⠀⠀⠀⠀⠈⠙⠻⠦⣄⠀⣴⠏⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢰⢸⡇⠀⠀⠀⠀"
                + "\n" + "⠀⠀⠀⠀⢻⡆⠀⠀⠀⠀⠀⠀⠀⠀⠀⠹⡽⣧⡀⢁⡼⠛⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠙⣷⠀⠀⠀⠀⠀⠀⠀⠀⠈⠻⣧⡀⠀⠀⣠⡴⠛⣿⠁⠀⠀⠀⠀⢸⣾⠇⠀⠀⠀⠀"
                + "\n" + "⠀⠀⠀⠀⢸⡇⠀⠀⠀⠀⠀⠘⣷⡲⠶⣄⡉⢸⣷⠋⠀⠀⠀⠀⠀⠀⠀⠀⢰⡖⠀⠀⠀⠀⠀⠀⡿⠀⠀⠀⠀⠀⢀⠀⠀⠀⠀⠈⠻⣶⠛⠉⠀⠞⠁⠀⠀⠀⠀⢰⣿⡟⠀⠀⠀⠀⠀"
                + "\n" + "⠀⠀⠀⣠⡾⢿⡆⠀⠀⠀⠀⠀⠈⠛⢶⣤⣭⡟⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⣿⠀⠀⠀⠀⣄⠀⢀⡷⠀⠀⡀⠀⠀⠈⣧⠀⠀⠀⠀⠀⠙⢧⡀⠀⠀⠀⠀⠀⠀⠀⣰⣿⣄⠀⠀⠀⠀⠀"
                + "\n" + "⠀⠴⠿⢿⡶⠃⢻⣄⠀⠀⠀⠀⠀⠀⠀⣼⠟⠀⠀⢀⡀⠀⠀⠀⠀⠀⠀⢠⡟⠀⠀⠀⠀⠀⠀⣸⡇⠀⠀⢳⡀⠀⠀⠸⡄⠀⠀⠀⠀⠀⠈⣿⡟⠓⠀⠀⠀⠀⣴⠏⠰⣭⣷⡦⠄⠀⠀"
                + "\n" + "⠀⠀⢠⡿⠁⠀⠀⠙⣷⡄⠀⢀⣀⣴⣿⠏⠀⠀⠀⢸⡇⠀⠀⠀⠀⠀⠀⣸⣇⠀⠀⠀⠀⢀⠀⣿⡄⣀⣀⣸⣧⠀⠀⠀⣿⠀⠀⠀⠀⠀⠀⠘⣿⡘⠻⢦⡀⠀⣿⠀⠀⠹⣇⠀⠀⠀⠀"
                + "\n" + "⠀⠀⣾⠇⠀⠀⠀⠀⢈⣿⠀⡈⢉⣽⠃⠀⠀⠀⠀⣿⠀⠀⢀⣤⡾⠋⠉⣹⠛⠁⠀⠀⢰⡟⢰⡟⣷⠀⠀⠀⣿⣿⠳⢦⣸⡇⠀⠀⡄⠀⠀⠀⣘⢧⡀⠈⡇⠀⣿⠀⠀⠀⣹⣧⠀⠀⠀"
                + "\n" + "⠀⠀⡿⣀⠀⠀⢀⣾⠛⣿⣆⣸⣿⠇⣶⠃⠀⠀⠀⣿⣦⠞⠁⢸⡇⠀⣼⢿⠀⠀⠀⣰⣿⠀⡸⠀⠘⣧⠀⠀⢸⣿⣆⠀⠘⣿⢆⢀⡇⠀⠀⠀⣽⡜⢿⣴⣶⣴⠏⠀⠀⠀⣷⣜⢳⣄⠀"
                + "\n" + "⢠⢱⣷⣿⠀⠀⣾⢁⣼⠁⣿⡿⡋⢸⡏⠀⠀⢠⠀⢸⡇⠀⠀⣿⠃⢸⠃⢸⠀⠀⣴⠋⡸⣰⠇⠀⠀⠈⢧⡀⠀⡇⠹⣆⠀⣿⡎⣀⡃⠀⠀⠀⡿⣿⢶⣿⣿⡿⡄⠀⠀⠀⢸⡏⠙⠛⠓"
                + "\n" + "⢠⣿⣿⡇⠀⣼⢃⡾⢃⣾⣿⣧⣵⣿⠁⠀⠀⠈⣇⢸⡇⠀⢸⢻⣦⡇⠀⢸⠀⣴⠃⢀⣷⠟⠀⠀⠀⠀⠀⠳⣄⢹⠀⢹⡄⡧⢷⣽⠃⠀⣀⠀⢾⣿⣾⣿⣋⠀⠹⣆⠀⢆⢸⡇⠀⠀⠀"
                + "\n" + "⠈⠉⣿⡇⢸⣏⣼⣅⣼⣿⣟⣩⣾⣿⠀⠀⠀⠀⣿⣾⢷⣀⡿⢸⡿⠀⠀⣧⣼⠃⠀⣸⠏⠀⠀⠀⠀⢠⡤⢄⣙⢿⣇⠀⣿⠁⢸⡿⠀⢀⡟⢀⡾⠿⠿⣿⣿⣆⢀⡙⣦⡌⢸⠃⠀⠀⠀"
                + "\n" + "⠀⠀⢿⡇⣸⡇⠉⠉⢉⠉⠁⢭⣿⣿⡀⠀⡇⠀⢹⡏⠘⣿⡇⠈⠁⣀⣰⣿⣄⡀⠘⠁⠀⠀⠀⠀⠀⢀⣿⠿⠶⠶⣽⣄⡈⠀⢸⡇⠀⣼⣇⣾⡅⠀⠀⠟⠛⠛⣿⣿⣿⢿⡟⠀⠀⠀⠀"
                + "\n" + "⠀⠀⠈⣿⣻⣷⢠⠉⠁⠉⠻⠟⠁⠈⢹⣶⣿⡄⢸⣇⠀⠹⣿⠞⠉⠉⠀⠀⠈⠙⠃⠀⠀⠀⠀⠀⠀⠀⠁⠀⠀⠀⠀⠈⢻⣦⢸⠃⢀⣿⣿⣿⣷⡀⠀⠀⢀⣠⣿⣿⣿⣾⠃⠀⠀⠀⠀"
                + "\n" + "⠀⠀⠀⠹⣿⣿⣾⣧⡀⠀⠀⠀⠀⢠⣾⣿⣿⣧⠀⢿⡀⠀⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡀⠀⠀⠀⠀⠀⠀⠀⠀⣼⠀⢸⣿⣿⣿⣿⡟⠋⠉⣩⣩⡿⠃⡿⠃⠀⠀⠀⠀⠀"
                + "\n" + "⠀⠀⠀⠀⢻⣏⢿⣏⡻⠲⢤⣄⣴⣿⣿⣿⣧⣿⣧⡸⣇⠀⠀⠀⠀⠀⠀⠀⢀⡴⠛⠛⠛⠛⠛⠛⠉⠙⢷⡀⠀⠀⠀⠀⠀⢘⣿⣶⣿⣿⣿⣿⣿⣷⡀⠰⣯⡟⠁⠀⠀⠀⠀⠀⠀⠀⠀"
                + "\n" + "⠀⠀⠀⠀⠀⠉⠉⠻⣷⣠⣤⣿⣿⢿⡟⢿⣿⡿⠿⣿⣿⣄⡀⠀⠀⠀⠀⠀⢸⡇⠀⠀⠀⠀⠀⠀⠀⠀⢘⡇⠀⠀⢀⣠⡴⣿⣻⣿⢿⣿⠃⠘⣿⢻⣄⣼⠋⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀"
                + "\n" + "⠀⠀⠀⠀⠀⠀⠀⠀⠈⢿⣧⣿⣿⣟⠀⣀⣿⣇⠀⠙⣿⣏⣽⣷⣶⣤⣤⣄⣸⣇⣀⡀⠀⠀⠀⣀⣀⣠⣼⡷⠞⠻⢯⣾⣄⢹⣿⠃⠛⢃⡴⢾⡟⣮⡿⠁⠀⠀⣀⠀⠀⠀⠀⠀⠀⠀⠀"
                + "\n" + "⠀⠀⠀⠀⠀⠀⠀⣶⣦⣀⠙⣿⠈⢿⠿⡿⠉⢹⣦⣴⣟⣙⣿⠀⠻⠂⡀⠈⠹⢿⣿⣭⠉⢻⣿⣿⠟⣿⣽⡇⠀⠀⠘⠙⠛⠛⢛⡷⣶⡾⠁⠀⠀⠘⣷⣤⠶⠚⣽⠇⠀⠀⠀⠀⠀⠀⠀"
                + "\n" + "⠀⠀⠀⠀⠀⠀⠀⠹⣆⣉⣿⡿⠀⠘⢦⣄⠀⠀⠉⠹⡍⠉⠉⠱⡄⠠⡇⠀⠀⠛⠛⠓⠂⠘⠿⠟⠁⠉⠛⢷⡞⢇⠀⠀⠀⠀⢀⠁⠀⠀⢀⡴⠀⠀⠉⠁⢠⣾⣇⣀⣀⠀⠀⠀⠀⠀⠀"
                + "\n" + "⠀⠀⠀⠀⠀⠀⣠⡤⠼⠟⠋⠀⠀⠀⠀⢈⣙⣿⠗⠒⠃⠀⣀⡼⢃⣀⣠⣴⣷⠦⣶⠛⡗⠲⡖⠀⢻⢸⡷⣾⣧⣈⠓⠲⢤⣄⣼⣧⡴⣿⣉⣀⠀⠀⠀⠀⠀⢸⣿⣿⡿⠀⠀⠀⠀⠀⠀"
                + "\n" + "⠀⠀⠀⠀⠀⠈⠙⠳⣶⠶⠀⣀⣤⣶⣶⠿⠋⠀⠀⢀⣴⠿⠿⣶⣶⡿⠾⢿⡟⠀⣀⣀⠀⢰⣷⠀⢸⢨⣙⠉⣿⠳⠤⠞⠛⠋⠀⣽⠃⠀⠀⠻⠿⠶⢦⣤⣤⣈⣻⣿⠀⠀⠀⠀⠀⠀⠀"
                + "\n" + "⠀⠀⠀⠀⠀⠀⠀⢹⣿⡶⠟⠛⠻⣶⢶⣤⣀⣤⣿⡉⠀⠀⢀⡾⠟⢷⣄⣼⠁⢀⡁⣹⠀⢸⣿⠀⢸⢀⣼⣄⣿⡆⢀⣤⣄⡀⠀⠈⢹⣿⡗⠀⣠⣤⣀⡟⠁⢻⠛⠉⠀⠀⠀⠀⠀⠀⠀"
                + "\n" + "⠀⠀⠀⠀⢠⣶⡿⠛⠉⠙⠓⠲⣤⣇⠀⠀⠀⠀⠘⣷⣀⣶⠏⠀⢀⡈⠛⠟⠉⠉⠻⣿⠀⢸⢿⠀⢸⣿⠀⠈⠙⣿⠉⠀⠀⠙⣆⣠⡟⠀⠙⠋⠁⠀⠀⠀⣰⡟⠀⢀⠀⠀⠀⠀⠀⠀⠀"
                + "\n" + "⠀⠀⠀⠀⣾⡏⠀⠀⠀⠀⠀⠀⠈⠙⠆⠀⠀⠀⠀⣼⡿⣿⣷⣾⣿⣇⠀⠀⠀⠀⣰⡟⠓⠛⠛⠲⢾⡃⠀⠀⠀⠈⣶⣤⣤⣴⣿⡏⠀⠀⠀⠀⠀⠀⠀⠐⠛⠉⠉⠙⠻⣦⡀⠀⠀⠀⠀"
                + "\n" + "⠀⠀⠀⠀⣿⡇⠀⠀⠀⠀⠀⠀⠀⢀⣤⠞⠛⢧⡾⠋⠀⠸⠷⠾⣿⣏⣻⣦⡤⠴⠋⠀⠀⠀⠀⠀⠀⢷⣵⣤⠶⣾⠿⣿⣿⣿⠏⠻⣦⣀⠤⣄⡀⠀⠀⠀⠀⠀⠀⠀⠀⣽⡇⠀⠀⠀⠀"
                + "\n" + "⠀⠀⠀⠀⠘⢿⣄⠀⠀⠀⠀⠀⠀⢸⣇⣀⣠⠾⢿⡿⠿⢶⣶⣶⣶⣯⡉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠚⠿⢤⣽⣤⣼⣯⣥⣤⣤⣾⣿⠀⣸⣷⡀⠀⠀⠀⠀⠀⠀⣸⣿⠇⠀⠀⠀⠀"
                + "\n" + "⠀⠀⠀⠀⠀⠀⠙⢦⣄⠀⠀⠀⠀⠀⠙⢿⡻⠇⠀⠛⠀⠀⠀⠀⠈⢻⣷⣄⠀⠀⠀⣠⣴⣿⣿⣦⡀⠀⠀⠀⣼⣿⠛⠛⠛⠛⠋⠛⠋⠁⠀⠛⣤⡇⠀⠀⠀⠀⠀⣴⣿⠏⠀⠀⠀⠀⠀"
                + "\n" + "⠀⠀⠀⠀⠀⠀⠀⠀⠉⠻⣶⣄⠀⠀⠀⠈⠙⠻⣇⠀⢀⣆⠐⠲⠶⣤⣹⣿⣷⣶⡿⠟⠋⠁⠙⢿⣿⣶⣤⣾⡿⠋⠀⠀⠀⠀⢰⣄⢀⣸⡿⠋⠉⠀⠀⠀⠀⣠⣾⡿⠃⠀⠀⠀⠀⠀⠀"
                + "\n" + "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠛⠶⣤⣄⡀⠀⠈⠉⠙⢿⣤⣄⠀⠀⠈⡈⠛⠛⠓⠒⠒⢠⠒⠲⠾⡿⠿⠟⠉⠉⠀⣠⣤⣤⠾⠋⠉⠉⠀⠀⠀⠀⣀⣴⡿⠟⠋⠀⠀⠀⠀⠀⠀⠀⠀"
                + "\n" + "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠙⠛⠶⠦⣤⣀⣈⣿⡳⣶⠟⠛⠛⠦⣤⣀⣀⣼⣦⣄⡤⠷⠤⠄⠤⢴⣾⠋⠀⠀⠀⠀⠀⣀⣀⣤⣴⠿⠛⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀"
                + "\n" + "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠀⢸⡆⠀⠀⠀⠀⠀⢸⣿⢸⠀⠀⠀⠀⠀⠀⢸⡏⢀⣀⣠⡤⠶⠾⠛⠋⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀"
                + "\n" + "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢿⡄⠀⠀⠀⠀⢸⣿⣾⡀⠀⠀⠀⠀⢀⣿⠛⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀"
                + "\n" + " ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀"



                + "\n" + "░█▀█░█▀▀░█▀▀░█▀▀░█░█░█▀█░█▀█░░░▀█▀░▀█▀░░░█░█░█░█░█▀█░█░░░█▀▀░░░█▀▄░█▀▀░█▀█░█▀▀"
                + "\n" + "░█░█░█▀▀░▀▀█░▀▀█░█░█░█░█░█░█░░░░█░░░█░░░░▀▄▀░█░█░█░█░█░░░█▀▀░░░█▀▄░█▀▀░█░█░█▀▀"
                + "\n" + "░▀░▀░▀▀▀░▀▀▀░▀▀▀░▀▀▀░▀░▀░▀▀▀░░░░▀░░▀▀▀░░░░▀░░▀▀▀░▀▀▀░▀▀▀░▀▀▀░░░▀▀░░▀▀▀░▀░▀░▀▀▀" );

    }

    //Input checker in state == PLAY

    private boolean adjacentFree(int r, int c) {

        if (client.getBoardView().getCell(r, c).isEmpty()) return false;
        if (r == 0 || c == 0) return true;
        if (r == 8 || c == 8) return true;
        return ((client.getBoardView().getCell(r + 1, c).isEmpty() || client.getBoardView().getCell(r, c + 1).isEmpty()) || (client.getBoardView().getCell(r - 1, c).isEmpty() || client.getBoardView().getCell(r, c - 1).isEmpty()));
    }

    private boolean inLine(String[] input) {
        if(removeSize == 1) return true;


        if (removeSize > 1  && (input[1].charAt(0) - 48 == input[3].charAt(0) - 48)){
            if(removeSize > 2){
                if((input[1].charAt(0) - 48 == input[5].charAt(0) - 48)) return true;
            }else return true;

        }

        if (removeSize > 1  && (input[2].charAt(0) - 48 == input[4].charAt(0) - 48)){
            if(removeSize > 2){
                if((input[2].charAt(0) - 48 == input[6].charAt(0) - 48)) return true;
            }else return true;
        }

        return false;
    }

    private boolean columnAvailable(int c, int size) {
        for (int i = 0; i < client.getBoardView().getListOfPlayer().size(); i++) {
            if (client.getBoardView().getListOfPlayer().get(i).equals(client.getBoardView().getCurrentPlayer())) {
                return client.getBoardView().getListOfPlayer().get(i).getShelf().isEmpty(5 - size, c);
            }
        }
        return false;
    }


    private boolean correctInput(String in) {

        String[] tmpInput = in.split(" ");
        if (in.startsWith("/remove")) {
            switch (tmpInput.length) {
                case 3:
                    removeSize = 1;
                    break;
                case 5:
                    removeSize = 2;
                    break;
                case 7:
                    removeSize = 3;
                    break;
            }

            //A triple AND condition was not used to improve readability
            if (adjacentFree(tmpInput[1].charAt(0) - 48, tmpInput[2].charAt(0) - 48)) {
                if (removeSize < 2 || adjacentFree(tmpInput[3].charAt(0) - 48, tmpInput[4].charAt(0) - 48)) {
                    if (removeSize < 3 || adjacentFree(tmpInput[5].charAt(0) - 48, tmpInput[6].charAt(0) - 48)) {
                        return inLine(tmpInput);
                    }
                }
            }
            return false;
        }
        if(in.startsWith("/add")){
            return columnAvailable(client.getBoardView().getTileBuffer().size(), tmpInput[1].charAt(0) - 48);
        }
        if(in.startsWith("/switch")) return client.getBoardView().getTileBuffer().size() > 1;
        if(in.startsWith("/end")) return true;
        return false;
    }
}
