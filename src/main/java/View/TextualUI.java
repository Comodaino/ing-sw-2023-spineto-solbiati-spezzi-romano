package View;

import Distributed.AbstractClient;
import Model.Player;
import Model.Tile;
import Model.Whisper;

import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.util.Comparator;
import java.util.Scanner;

/**
 * This class is the textual user interface of the game.
 *
 * @author Clara
 */
public class TextualUI implements ViewInterface {

    private State state;
    private final Scanner input;
    private AbstractClient client;
    private static final String RESET = "\033[0m";
    private static final int maxMsgLength = 50;
    private int removeSize;

    public TextualUI(AbstractClient client) throws IOException {

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

    private void inputHandler() throws IOException {
        while (state != State.CLOSE) {
            String in = input.nextLine();
            if (in.equals("/exit")) client.println(in);
            else {
                if (in != null && in.length() > 0) {
                    if (state == State.LOBBY || state == State.PLAY) {
                        if (in.startsWith("/") && (state == State.LOBBY && !correctLobbyInput(in)) || ((state == State.PLAY) && !correctInput(in)))
                            System.out.println("Command is invalid, try /help or /h");
                        else {
                            if (in.equals("/h") || in.equals("/help")) help();
                            else {
                                if (in.equals("/cg") || in.equals("/pg")) printGoal(in);
                                else client.println(in);
                            }
                        }
                    }
                    if (state == State.HOME) {
                        if (!client.isOwner()) System.out.println("wait for the owner");
                        if (in.length() > 10) {
                            System.out.println("Nickname too long, please insert a nickname with less than 10 characters");
                        } else
                            client.println(in);
                    }
                }
            }
        }
    }


    private boolean correctLobbyInput(String in) {
        if (in.startsWith("/")) {
            if (in.equals("/start") || in.equals("/firstMatch") || in.equals("/notFirstMatch") || in.equals("/help") || in.equals("/h"))
                return true;
            if (in.startsWith("/set")) {
                String[] set = in.split(" ");
                if (set[1].equals("2") || set[1].equals("3") || set[1].equals("4")) return true;
                else {
                    System.out.println("from 2 to 4 players allowed");
                    return false;
                }
            }
            if (in.startsWith("/whisper")) {
                String[] msg = in.split(" ");
                if (msg.length < 3)
                    System.out.println("whisper failed, addressee or message is missing");
                else {
                    for (int i = 2; i < msg.length; i++)
                        msg[2] += msg[i];
                    if (msg[2].length() > maxMsgLength) {
                        System.out.println(ConsoleColors.RED_BOLD + "message too long, maximum character: " + maxMsgLength + RESET);
                    }

                }
            }
            return false;
        }
        return true;

    }

    private void printGoal(String in) throws RemoteException {
        switch (in) {
            case ("/cg"):
                client.getBoardView().getSetOfCommonGoal().forEach((goal) -> {
                    System.out.println(goal.getName());
                    switch (goal.getName()) {
                        case ("GoalAngles"):
                            System.out.println("Four tiles of the same color in the four corners of the bookshelf");
                            break;
                        case ("GoalColumn"):
                            System.out.println("Three columns each formed by 6 tiles of maximum three different types. One column can show the same or a different combination of another column");
                            break;
                        case ("GoalCouples"):
                            System.out.println("six groups each containing at least 2 tiles of the same type the tiles of one group can be different from those of another group");
                            break;
                        case ("GoalCross"):
                            System.out.println("five tiles of the same type forming an X");
                            break;
                        case ("GoalDiagonal"):
                            System.out.println("five tiles of the same type forming a diagonal");
                            break;
                        case ("GoalDiffColumns"):
                            System.out.println("two columns each formed by 6 different types of tiles");
                            break;
                        case ("GoalDiffRows"):
                            System.out.println("two lines each formed by 5 different types of tiles. One line can show the same or a different combination of the other line");
                            break;
                        case ("GoalEight"):
                            System.out.println("eight tiles of the same type");
                            break;
                        case ("GoalQuartets"):
                            System.out.println("four groups each containing at least 3 tiles of the same type. The tiles of one group can be different from those of another group");
                            break;
                        case ("GoalRow"):
                            System.out.println("four lines each formed by 5 tiles of maximum three different types");
                            break;
                        case ("GoalSquares"):
                            System.out.println("two groups each containing 4 tiles of the same type in a 2x2 square. The tiles of one square can be different from those of the other square.");
                            break;
                        case ("GoalStairs"):
                            System.out.println("five columns of increasing or decreasing height: starting from the first column on the left or on the right, each next column must be made of exactly one more tile.");
                            break;
                    }
                    printGoalShelf(goal.getName());
                });
                break;
            case ("/pg"):
                int index = 0;
                String[][] pgShelf = new String[6][5];
                int row = 0;
                int col = 0;
                for (Player p : client.getBoardView().getListOfPlayer())
                    if (p.getNickname().equals(client.getNickname())) {
                        index = p.getGoal().getNumOfGoal();
                        index = index * 6 + 1;
                        for (int i = 0; i < 6; i++) {
                            row = p.getGoal().getPersGoal(index).get(i).getLine();
                            col = p.getGoal().getPersGoal(index).get(i).getCol();
                            pgShelf[row][col] = p.getGoal().getPersGoal(index).get(i).getColor().toString();
                        }
                        printPGShelf(pgShelf);
                    }
                break;
        }
    }

    private void printPGShelf(String[][] pg) {
        for (int i = 5; i >= 0; i--) {
            for (int j = 0; j < 5; j++) {
                if (j == 0)
                    System.out.print(i + " ");
                if (j == 0) {
                }
                if (pg[i][j] == (null)) {
                    System.out.print("[]");
                } else {
                    switch (pg[i][j]) {
                        case "WHITE":
                            System.out.print(ConsoleColors.BLACK + ConsoleColors.WHITE_BACKGROUND + "[]" + RESET);
                            break;
                        case "YELLOW":
                            System.out.print(ConsoleColors.BLACK + ConsoleColors.YELLOW_BACKGROUND + "[]" + RESET);
                            break;
                        case "LIGHTBLUE":
                            System.out.print(ConsoleColors.BLACK + ConsoleColors.CYAN_BACKGROUND + "[]" + RESET);
                            break;
                        case "GREEN":
                            System.out.print(ConsoleColors.BLACK + ConsoleColors.GREEN_BACKGROUND + "[]" + RESET);
                            break;
                        case "BLUE":
                            System.out.print(ConsoleColors.BLACK + ConsoleColors.BLUE_BACKGROUND + "[]" + RESET);
                            break;
                        case "PINK":
                            System.out.print(ConsoleColors.BLACK + ConsoleColors.PURPLE_BACKGROUND + "[]" + RESET);
                            break;
                    }
                }
            }
            System.out.print("\n");
        }
        System.out.println("   0 1 2 3 4");
    }

    /**
     * method to print changes based on the state and the argument received.
     * if arg is "disconnected" it will print a message of disconnection, if arg is "/nickname" it will print a message of nickname already taken and requires a new nickname
     * if arg is "/commands" it will print the list of commands available only during LOBBY state, other arg will be ignored
     * @param arg can be "disconnected", "/nickname" or "/commands"
     * @throws IOException
     */
    @Override

    public void update(String arg) throws IOException {
        if (arg.equals("disconnected")) {
            System.out.println(ConsoleColors.RED_BOLD + "\n\n\n\n\n\n\n\t\t\tYOU HAVE BEEN DISCONNECTED FROM THE SERVER\n\n" + RESET);
            System.out.println("Press /exit to quit the game");
        } else {

            switch (this.state) {
                case HOME:
                    System.out.println(ConsoleColors.RED_BOLD_BRIGHT + "\n" +

                            "███╗░░░███╗██╗░░░██╗  ░██████╗██╗░░██╗███████╗██╗░░░░░███████╗██╗███████╗\n" +
                            "████╗░████║╚██╗░██╔╝  ██╔════╝██║░░██║██╔════╝██║░░░░░██╔════╝██║██╔════╝\n" +
                            "██╔████╔██║░╚████╔╝░  ╚█████╗░███████║█████╗░░██║░░░░░█████╗░░██║█████╗░░\n" +
                            "██║╚██╔╝██║░░╚██╔╝░░  ░╚═══██╗██╔══██║██╔══╝░░██║░░░░░██╔══╝░░██║██╔══╝░░\n" +
                            "██║░╚═╝░██║░░░██║░░░  ██████╔╝██║░░██║███████╗███████╗██║░░░░░██║███████╗\n" +
                            "╚═╝░░░░░╚═╝░░░╚═╝░░░  ╚═════╝░╚═╝░░╚═╝╚══════╝╚══════╝╚═╝░░░░░╚═╝╚══════╝" + RESET);
                    homePrint(arg);
                    break;
                case LOBBY:
                    if (client.isOwner()) {
                        if (arg != null && arg.equals("/commands"))
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

                    Player currentPlayer = null;
                    for (Player p : client.getBoardView().getListOfPlayer()) {
                        if (p.getNickname().equals(client.getBoardView().getCurrentPlayer().getNickname())) {
                            currentPlayer = p;
                        }
                    }
                    assert currentPlayer != null;
                    if (client.getNickname().equals(currentPlayer.getNickname())) {
                        showBoard();
                        showYourShelf();
                        showOthersShelf();
                        tileBuffer();
                        showGoals();
                        chat();
                        System.out.println("Your turn!");
                    } else {
                        showBoard();
                        showYourShelf();
                        showOthersShelf();
                        chat();
                        System.out.println(client.getBoardView().getCurrentPlayer().getNickname() + " is playing...Wait your turn!");
                    }
                    break;
                case END:
                    String winner = client.getBoardView().getWinner().getNickname();
                    client.getBoardView().getListOfPlayer().sort(new Comparator<Player>() {
                        @Override
                        public int compare(Player o1, Player o2) {
                            if (o1.getScore() == o2.getScore()) {
                                if (o1.getNickname().compareTo(o2.getNickname()) >= 0) {
                                    return 1;
                                }
                            } else return -1;
                            if (o1.getScore() > o2.getScore()) return 1;
                            return -1;
                        }
                    });
                    System.out.println("SCORES:");
                    for (Player p : client.getBoardView().getListOfPlayer()) {
                        System.out.println(p.getNickname() + "\t---->\t" + p.getScore());
                    }
                    if (winner == null) {
                        System.out.println("\t\t\t\t\t" + ConsoleColors.RED_BOLD + "There is no winner");
                    } else {
                        System.out.println("The winner is......");
                        System.out.println("\t\t\t\t\t" + ConsoleColors.BLACK_BOLD + ConsoleColors.PURPLE_BACKGROUND_BRIGHT + winner + RESET + "\t\t\t\t\t");
                    }
                    break;
                case CLOSE:
                    System.out.println("The lobby has been closed, thank you for playing!");
                    break;
            }
        }
    }

    /**
     * Prints all the useful information and requirements about the game to the user, based on the state of the game.
     * In state HOME it will print the home screen and require a nickname.
     * In state LOBBY it will print the lobby screen and require the owner player to choose if it's his first match or not or to start the game.
     * In state PLAY it will print the board, the goals, the players' shelves, the chat and the whispers received(if any), updating any changes taken from the client.
     * In state END it will print the final scores and the winner.
     * In state CLOSE it will print a message of closure of the game.
     *
     * @throws IOException
     */
    public void update() throws IOException {
        switch (this.state) {
            case HOME:
                System.out.println(ConsoleColors.PURPLE_BOLD + "\n" +

                        "███╗░░░███╗██╗░░░██╗  ░██████╗██╗░░██╗███████╗██╗░░░░░███████╗██╗███████╗\n" +
                        "████╗░████║╚██╗░██╔╝  ██╔════╝██║░░██║██╔════╝██║░░░░░██╔════╝██║██╔════╝\n" +
                        "██╔████╔██║░╚████╔╝░  ╚█████╗░███████║█████╗░░██║░░░░░█████╗░░██║█████╗░░\n" +
                        "██║╚██╔╝██║░░╚██╔╝░░  ░╚═══██╗██╔══██║██╔══╝░░██║░░░░░██╔══╝░░██║██╔══╝░░\n" +
                        "██║░╚═╝░██║░░░██║░░░  ██████╔╝██║░░██║███████╗███████╗██║░░░░░██║███████╗\n" +
                        "╚═╝░░░░░╚═╝░░░╚═╝░░░  ╚═════╝░╚═╝░░╚═╝╚══════╝╚══════╝╚═╝░░░░░╚═╝╚══════╝" + RESET);
                System.out.print("Insert your nickname:\t");
                break;
            case LOBBY:
                if (client.isOwner()) {
                    System.out.println("COMMANDS AVAILABLE:");
                    System.out.println(ConsoleColors.GREEN_UNDERLINED + "/start" + RESET + " to start the game");
                    System.out.println(ConsoleColors.GREEN_UNDERLINED + "/firstMatch" + RESET + " if this is your first match\t\tOR\t\t" + ConsoleColors.GREEN_UNDERLINED + "/notFirstMatch" + RESET + " if you have already played");
                    System.out.println(ConsoleColors.GREEN_UNDERLINED + "/set n" + RESET + " to set number of players");
                } else {
                    System.out.println(ConsoleColors.CYAN_UNDERLINED + "wait for the owner to start the game" + RESET);
                }
                System.out.println("Players in the lobby:");
                for (Player p : client.getBoardView().getListOfPlayer()) {
                    System.out.println("---" + ConsoleColors.BLACK + ConsoleColors.PURPLE_BACKGROUND + p.getNickname() + RESET + "---");
                }
                chat();
                break;
            case PLAY:
                if (client.getBoardView().getListOfPlayer().size() == 1) {
                    nb();
                    break;
                }

                Player currentPlayer = null;
                for (Player p : client.getBoardView().getListOfPlayer()) {
                    if (p.getNickname().equals(client.getBoardView().getCurrentPlayer().getNickname())) {
                        currentPlayer = p;
                    }
                }
                assert currentPlayer != null;
                if (client.getNickname().equals(currentPlayer.getNickname())) {
                    showBoard();
                    showYourShelf();
                    showOthersShelf();
                    tileBuffer();
                    showGoals();
                    chat();
                    System.out.println("YOUR TURN!");
                } else {
                    showBoard();
                    showYourShelf();
                    showOthersShelf();
                    chat();
                    System.out.println(client.getBoardView().getCurrentPlayer().getNickname() + " is playing...Wait your turn!");
                }
                break;
            case END:
                String winner;
                System.out.println("SCORES:");
                for (Player p : client.getBoardView().getListOfPlayer()) {
                    System.out.println(p.getNickname() + "\t---->\t" + p.getScore());
                }
                if (client.getBoardView().getWinner() != null) {
                    winner = client.getBoardView().getWinner().getNickname();
                    System.out.println("The winner is......");
                    System.out.println("\t\t\t\t\t" + ConsoleColors.BLACK_BOLD + ConsoleColors.PURPLE_BACKGROUND_BRIGHT + winner + RESET + "\t\t\t\t\t");
                } else System.out.println(ConsoleColors.RED_BOLD + "There is no winner" + ConsoleColors.WHITE);
                break;
            case CLOSE:
                System.out.println("The lobby has been closed, thank you for playing!");
                break;
        }
    }

    private void chat() throws RemoteException {
        if (client.getBoardView().getChatBuffer().size() > 0)
            System.out.println(ConsoleColors.PURPLE_UNDERLINED + "CHAT:" + RESET);
        for (String s : client.getBoardView().getChatBuffer()) {
            System.out.println("\uD83D\uDFE9" + s);
        }
        for (Whisper s : client.getBoardView().getPersonalChatBuffer()) {
            if (s.getRecipient().equals(client.getNickname())) System.out.println("--" + s.getContent());
        }
    }

    private void showGoals() throws RemoteException {
        System.out.print("COMMON GOALS:\t\t");
        client.getBoardView().getSetOfCommonGoal().forEach((goal) -> System.out.println(goal.getName()));
        System.out.println("PERSONAL GOALS:\t\t");
        printGoal("/pg");
        //System.out.println("Adjacent tiles");

    }


    private void showOthersShelf() throws RemoteException {
        System.out.println("OTHERS' SHELVES:");
        for (Player p : client.getBoardView().getListOfPlayer()) {
            if (!p.getNickname().equals(client.getNickname())) {
                System.out.println(ConsoleColors.BLUE_BOLD + "\t" + p.getNickname());
                String tType = null;
                for (int i = 5; i >= 0; i--) {
                    for (int j = 0; j < 5; j++) {
                        if (i == 5 && j == 0) {
                            System.out.println("  " + ConsoleColors.BLACK_BOLD + ConsoleColors.RED_BACKGROUND + "=======================" + RESET);
                        }
                        if (j == 0)
                            System.out.print(i + " ");
                        Tile tile = p.getShelf().getTile(i, j);
                        if (j == 0) {
                            System.out.print(ConsoleColors.BLACK_BOLD + ConsoleColors.RED_BACKGROUND + "||" + RESET);
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
                                    System.out.print(ConsoleColors.BLACK + ConsoleColors.WHITE_BACKGROUND + tType + RESET + ConsoleColors.BLACK_BOLD + ConsoleColors.RED_BACKGROUND + "|" + RESET);
                                    break;
                                case YELLOW:
                                    System.out.print(ConsoleColors.BLACK + ConsoleColors.YELLOW_BACKGROUND + tType + RESET + ConsoleColors.BLACK_BOLD + ConsoleColors.RED_BACKGROUND + "|" + RESET);
                                    break;
                                case LIGHTBLUE:
                                    System.out.print(ConsoleColors.BLACK + ConsoleColors.CYAN_BACKGROUND + tType + RESET + ConsoleColors.BLACK_BOLD + ConsoleColors.RED_BACKGROUND + "|" + RESET);
                                    break;
                                case GREEN:
                                    System.out.print(ConsoleColors.BLACK + ConsoleColors.GREEN_BACKGROUND + tType + RESET + ConsoleColors.BLACK_BOLD + ConsoleColors.RED_BACKGROUND + "|" + RESET);
                                    break;
                                case BLUE:
                                    System.out.print(ConsoleColors.BLACK + ConsoleColors.BLUE_BACKGROUND + tType + RESET + ConsoleColors.BLACK_BOLD + ConsoleColors.RED_BACKGROUND + "|" + RESET);
                                    break;
                                case PINK:
                                    System.out.print(ConsoleColors.BLACK + ConsoleColors.PURPLE_BACKGROUND + tType + RESET + ConsoleColors.BLACK_BOLD + ConsoleColors.RED_BACKGROUND + "|" + RESET);
                                    break;
                            }
                        }
                        if (j == 4)
                            System.out.print(ConsoleColors.BLACK_BOLD + ConsoleColors.RED_BACKGROUND + "|" + RESET);
                    }
                    System.out.println("\n  " + ConsoleColors.BLACK_BOLD + ConsoleColors.RED_BACKGROUND + "++===+===+===+===+===++" + RESET);
                }
                System.out.println("\t 0   1   2   3   4   ");
            }
        }

    }

    private void showYourShelf() throws RemoteException {
        for (Player player : client.getBoardView().getListOfPlayer()) {
            if (player.getNickname().equals(client.getNickname())) {
                System.out.println("YOUR SHELF:");
                String tType = null;
                for (int i = 5; i >= 0; i--) {
                    for (int j = 0; j < 5; j++) {
                        if (i == 5 && j == 0) {
                            System.out.println("  " + ConsoleColors.BLACK_BOLD + ConsoleColors.RED_BACKGROUND + "=======================" + RESET);
                        }
                        if (j == 0)
                            System.out.print(i + " ");
                        Tile tile = player.getShelf().getTile(i, j);
                        if (j == 0) {
                            System.out.print(ConsoleColors.BLACK_BOLD + ConsoleColors.RED_BACKGROUND + "||" + RESET);
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
                                    System.out.print(ConsoleColors.BLACK + ConsoleColors.WHITE_BACKGROUND + tType + RESET + ConsoleColors.BLACK_BOLD + ConsoleColors.RED_BACKGROUND + "|" + RESET);
                                    break;
                                case YELLOW:
                                    System.out.print(ConsoleColors.BLACK + ConsoleColors.YELLOW_BACKGROUND + tType + RESET + ConsoleColors.BLACK_BOLD + ConsoleColors.RED_BACKGROUND + "|" + RESET);
                                    break;
                                case LIGHTBLUE:
                                    System.out.print(ConsoleColors.BLACK + ConsoleColors.CYAN_BACKGROUND + tType + RESET + ConsoleColors.BLACK_BOLD + ConsoleColors.RED_BACKGROUND + "|" + RESET);
                                    break;
                                case GREEN:
                                    System.out.print(ConsoleColors.BLACK + ConsoleColors.GREEN_BACKGROUND + tType + RESET + ConsoleColors.BLACK_BOLD + ConsoleColors.RED_BACKGROUND + "|" + RESET);
                                    break;
                                case BLUE:
                                    System.out.print(ConsoleColors.BLACK + ConsoleColors.BLUE_BACKGROUND + tType + RESET + ConsoleColors.BLACK_BOLD + ConsoleColors.RED_BACKGROUND + "|" + RESET);
                                    break;
                                case PINK:
                                    System.out.print(ConsoleColors.BLACK + ConsoleColors.PURPLE_BACKGROUND + tType + RESET + ConsoleColors.BLACK_BOLD + ConsoleColors.RED_BACKGROUND + "|" + RESET);
                                    break;
                            }
                        }
                        if (j == 4)
                            System.out.print(ConsoleColors.BLACK_BOLD + ConsoleColors.RED_BACKGROUND + "|" + RESET);
                    }
                    System.out.println("\n  " + ConsoleColors.BLACK_BOLD + ConsoleColors.RED_BACKGROUND + "++===+===+===+===+===++" + RESET);
                }
                System.out.println("\t 0   1   2   3   4   ");
            }
        }
    }

    private void showBoard() {
        String tType = null;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (i == 0 && j == 0) {
                    System.out.println("\t\t_____________________________________");
                }
                if (j == 0)
                    System.out.print("\t" + i + "\t");
                Tile tile = client.getBoardView().getCell(i, j).getTile();
                if (j == 0) {
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
                            System.out.print(ConsoleColors.BLACK + ConsoleColors.CYAN_BACKGROUND + tType + RESET + "|");
                            break;
                        case GREEN:
                            System.out.print(ConsoleColors.BLACK + ConsoleColors.GREEN_BACKGROUND + tType + RESET + "|");
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
        }
        System.out.println("\t\t  0   1   2   3   4   5   6   7   8");
    }

    private void homePrint(String arg) {
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


    private void tileBuffer() {
        System.out.print(ConsoleColors.BLUE_UNDERLINED + "TILE BUFFER:" + RESET + "\t\t");
        String tType = null;
        if (client.getBoardView().getTileBuffer().size() == 0) {
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
                            System.out.print(ConsoleColors.BLACK_BOLD + ConsoleColors.WHITE_BACKGROUND + tType + RESET + " ");
                            break;
                        case YELLOW:
                            System.out.print(ConsoleColors.BLACK_BOLD + ConsoleColors.YELLOW_BACKGROUND + tType + RESET + " ");
                            break;
                        case LIGHTBLUE:
                            System.out.print(ConsoleColors.BLACK_BOLD + ConsoleColors.CYAN_BACKGROUND + tType + RESET + " ");
                            break;
                        case GREEN:
                            System.out.print(ConsoleColors.BLACK_BOLD + ConsoleColors.GREEN_BACKGROUND + tType + RESET + " ");
                            break;
                        case BLUE:
                            System.out.print(ConsoleColors.BLACK_BOLD + ConsoleColors.BLUE_BACKGROUND + tType + RESET + " ");
                            break;
                        case PINK:
                            System.out.print(ConsoleColors.BLACK_BOLD + ConsoleColors.PURPLE_BACKGROUND + tType + RESET + " ");
                            break;
                    }
                }
            }
            System.out.println();
        }
    }

    private void help() throws RemoteException {
        System.out.println("\t\t\t" + ConsoleColors.GREEN_UNDERLINED + "COMMANDS AVAILABLE:" + RESET);
        if (this.state.equals(State.PLAY)) {
            System.out.println(ConsoleColors.GREEN_UNDERLINED + "/remove row column" + RESET + "  ---> to remove the tile[row][column] from the board");
            System.out.println(ConsoleColors.GREEN_UNDERLINED + "/remove row1 column1 [row2 column2] [row3 column3]" + RESET + "  ---> to remove 2 or 3 tiles from the board\n");
            System.out.println(ConsoleColors.GREEN_UNDERLINED + "/switch t1 t2" + RESET + "  ---> to switch tile t1 and t2 in the tile buffer (the first tile from left is tile number 0)");
            System.out.println(ConsoleColors.GREEN_UNDERLINED + "/add C" + RESET + "  ---> to add tiles from the buffer to the column C of your shelf");
            System.out.println(ConsoleColors.GREEN_UNDERLINED + "/cg of /pg" + RESET + "  ---> to see common goals or private goals");
        }
        if (this.state.equals(State.LOBBY) && client.isOwner())
            System.out.println(ConsoleColors.GREEN_UNDERLINED + "/start" + RESET + "  ---> to start the game");
        System.out.println(ConsoleColors.GREEN_UNDERLINED + "/whisper addressee message" + RESET + "  ---> to send a private message to another player");
        System.out.println(ConsoleColors.GREEN_UNDERLINED + "/exit" + RESET + "  ---> close the app, if a match is still going you can rejoin");
    }

    private void nb() {
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
                + "\n" + "░▀░▀░▀▀▀░▀▀▀░▀▀▀░▀▀▀░▀░▀░▀▀▀░░░░▀░░▀▀▀░░░░▀░░▀▀▀░▀▀▀░▀▀▀░▀▀▀░░░▀▀░░▀▀▀░▀░▀░▀▀▀");

    }

    private boolean adjacentFree(int r, int c) {

        if (client.getBoardView().getCell(r, c).isEmpty()) return false;
        if (r == 0 || c == 0) return true;
        if (r == 8 || c == 8) return true;
        return ((client.getBoardView().getCell(r + 1, c).isEmpty() || client.getBoardView().getCell(r, c + 1).isEmpty()) || (client.getBoardView().getCell(r - 1, c).isEmpty() || client.getBoardView().getCell(r, c - 1).isEmpty()));
    }

    private boolean inLine(String[] input) {
        if (removeSize == 1) return true;


        if (removeSize > 1 && (input[1].charAt(0) - 48 == input[3].charAt(0) - 48)) {
            if (removeSize > 2) {
                if ((input[1].charAt(0) - 48 == input[5].charAt(0) - 48)) return true;
            } else return true;

        }

        if (removeSize > 1 && (input[2].charAt(0) - 48 == input[4].charAt(0) - 48)) {
            if (removeSize > 2) {
                if ((input[2].charAt(0) - 48 == input[6].charAt(0) - 48)) return true;
            } else return true;
        }

        return false;
    }

    private boolean columnAvailable(int c, int size) throws RemoteException {
        for (int i = 0; i < client.getBoardView().getListOfPlayer().size(); i++) {
            if (client.getBoardView().getListOfPlayer().get(i).getNickname().equals(client.getNickname())) {
                return client.getBoardView().getListOfPlayer().get(i).getShelf().isEmpty(5 - size, c);
            }
        }
        return false;
    }


    private boolean correctInput(String in) throws RemoteException {
        if (!in.startsWith("/")) return true;
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
        if (in.startsWith("/add")) {
            return columnAvailable(client.getBoardView().getTileBuffer().size(), tmpInput[1].charAt(0) - 48);
        }
        if (in.startsWith("/switch")) return client.getBoardView().getTileBuffer().size() > 1;
        if (in.startsWith("/end")) return true;
        if (in.equals("/help") || in.equals("/h")) return true;
        if (in.equals("/cg") || in.equals("/pg")) return true;
        if (in.startsWith("/whisper")) {
            String[] msg = in.split(" ");
            if (msg.length < 3) {
                System.out.println("whisper failed, addressee or message is missing");
                return false;
            } else {
                for (int i = 2; i < msg.length; i++)
                    msg[2] += msg[i];
                if (msg[2].length() > maxMsgLength) {
                    System.out.println(ConsoleColors.RED_BOLD + "message too long, maximum character: " + maxMsgLength + RESET);
                    return false;
                }
                return true;

            }
        }
        return false;
    }

    private void printGoalShelf(String goal) {
        String conf = goal + "_conf";
        InputStream is = getClass().getClassLoader().getResourceAsStream("CommonGoalsTUI/" + conf);
        assert is != null;
        Scanner reader = new Scanner(is);
        while (reader.hasNextLine()) {
            System.out.println(reader.nextLine());
        }
    }
}
