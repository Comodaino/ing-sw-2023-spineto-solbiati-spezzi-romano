package View;

import Distributed.AbstractClient;
import Model.*;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Comparator;
import java.util.Scanner;

public class TextualUI implements ViewInterface {

    private State state;
    private final Scanner input;
    private AbstractClient client;
    private static final String RESET = "\033[0m";
    private static final int maxMsgLength = 50;
    private int removeSize;
    private boolean added, removed;

    public TextualUI(AbstractClient client) throws IOException {

        this.state = State.HOME;
        this.input = new Scanner(System.in);
        this.client = client;
        this.added = false;
        this.removed = false;
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
            if(in!=null && in.length()>0) {
                if (state != State.HOME) {
                    if (state == State.LOBBY && (in.equals("/cg") || in.equals("/pg"))) printGoal(in);
                    if (in.equals("/h") || in.equals("/help")) help();
                    if (in.startsWith("/whisper")) {
                        String[] msg = in.split(" ");
                        if (msg.length < 3) System.out.println("whisper failed, addressee or message is missing");
                        else {
                            for (int i = 2; i < msg.length; i++) msg[2] += msg[i];
                            if (msg[2].length() > maxMsgLength)
                                System.out.println(ConsoleColors.RED_BOLD + "message too long, maximum character: " + maxMsgLength + RESET);
                        }
                    }
                    if ((state == State.LOBBY && !correctLobbyInput(in)) || ((state == State.PLAY) && !correctInput(in)))
                        System.out.println("Command is invalid, try /help or /h");
                    else if (state == State.HOME && !client.isOwner()) System.out.println("Please wait for the owner");
                    else client.println(in);


                } else {
                    if (in != null && in.length() > 10) {
                        System.out.println("Nickname too long, please insert a nickname with less than 10 characters");
                    } else if (in != null)
                        client.println(in);
                }
            }
        }
    }

    private boolean correctLobbyInput(String in) {
        if(in.startsWith("/")){
            if (in.equals("/start") || in.equals("/firstMatch") || in.equals("/notFirstMatch") || in.equals("/help") || in.equals("/h") || in.startsWith("/whisper")) return true;
            return false;
        }
        return true;

    }

    private void printGoal(String in) {
        switch (in){
            case ("/cg"):
                client.getBoardView().getSetOfCommonGoal().forEach((goal) ->{
                    System.out.println(goal.getName());
                    switch (goal.getName()){
                        case ("GoalAngles"):
                            System.out.println("Four tiles of the same color in the four corners of the bookshelf");
                            Shelf g = crateGoalShelf(goal.getName());
                            printGoalShelf(g);
                            break;
                        case ("GoalColumns"):
                            System.out.println("Three columns each formed by 6 tiles of maximum three different types. One column can show the same or a different combination of another column");
                            for(int i=0; i<6; i++){
                                System.out.print("|");
                                for(int j=0; j<5; j++){
                                    if(j==1 || j==3 || j==4) {
                                        if (i == 2)
                                            System.out.print(ConsoleColors.YELLOW_BACKGROUND_BRIGHT + " " + RESET + "|");
                                        else if (i == 3)
                                                System.out.print(ConsoleColors.GREEN_BACKGROUND + " " + RESET + "|");
                                                else if (i == 5)
                                                        System.out.print(ConsoleColors.BLUE_BACKGROUND + " " + RESET + "|");
                                                else System.out.print(ConsoleColors.RED_BACKGROUND_BRIGHT + " " + RESET + "|");

                                    }else System.out.print(" |");
                                }
                                System.out.print("\n");
                            }
                            break;
                        case ("GoalCouples"):
                            System.out.println("six groups each containing at least 2 tiles of the same type the tiles of one group can be different from those of another group");
                            break;
                        case ("GoalCross"):
                            System.out.println("five tiles of the same type forming an X");
                            break;
                        case ("GoalDiagonals"):
                            System.out.println("five tiles of the same type forming a diagonal");
                            break;
                        case ("GoalFullShelf"):
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
                        case ("GoalRows"):
                            System.out.println("four lines each formed by 5 tiles of maximum three different types");
                            break;
                        case ("GoalSquares"):
                            System.out.println("two groups each containing 4 tiles of the same type in a 2x2 square");
                            break;
                        case("GoalStairs"):
                            System.out.println("five columns of increasing or decreasing height: starting from the first column on the left or on the right, each next column must be made of exactly one more tile.");
                            break;
                    }
                });
                break;
            case ("/pg"):


        }
    }

    @Override
    public void update(String arg) throws IOException {
        //System.out.println("update: " + this.state);
            switch (this.state) {
                case HOME:
                    System.out.println(ConsoleColors.RED_BOLD + "\n" +
                                    " _    _ _____ _     _____ ________  ________   _____ _____   ___  ____   __  _____ _   _ _____ _    ______ _____ _____ _ \n" +
                                    "| |  | |  ___| |   /  __ \\  _  |  \\/  |  ___| |_   _|  _  |  |  \\/  \\ \\ / / /  ___| | | |  ___| |   |  ___|_   _|  ___| |\n" +
                                    "| |  | | |__ | |   | /  \\/ | | | .  . | |__     | | | | | |  | .  . |\\ V /  \\ `--.| |_| | |__ | |   | |_    | | | |__ | |\n" +
                                    "| |/\\| |  __|| |   | |   | | | | |\\/| |  __|    | | | | | |  | |\\/| | \\ /    `--. \\  _  |  __|| |   |  _|   | | |  __|| |\n" +
                                    "\\  /|  / |___| |___| \\__/| \\_/ / |  | | |___    | | \\ \\_/ /  | |  | | | |   /\\__/ / | | | |___| |___| |    _| |_| |___|_|\n" +
                                    " \\/  |/\\____/\\_____/\\____/\\___/\\_|  |_|____/    \\_/  \\___/   \\_|  |_/ \\_/   \\____/\\_| |_|____/\\_____|_|    \\___/\\____/(_)\n" + RESET);
                    System.out.print(ConsoleColors.WHITE);

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
                    client.getBoardView().getListOfPlayer().sort(new Comparator<Player>() {
                        @Override
                        public int compare(Player o1, Player o2) {
                            if (o1.getScore()== o2.getScore()){
                                if(o1.getNickname().compareTo(o2.getNickname())>=0){
                                    return 1;
                                }
                            }else return -1;
                            if(o1.getScore()> o2.getScore()) return  1;
                            return -1;
                        }
                    });
                    System.out.println("SCORES:");
                    for (Player p : client.getBoardView().getListOfPlayer()) {
                        System.out.println(p.getNickname() + "\t---->\t" + p.getScore());
                    }
                    if(winner==null) {
                        System.out.println("\t\t\t\t\t" + ConsoleColors.RED_BOLD + "There is no winner" );
                    }else{
                        System.out.println("The winner is......");
                        System.out.println("\t\t\t\t\t" + ConsoleColors.BLACK_BOLD + ConsoleColors.PURPLE_BACKGROUND_BRIGHT + winner + RESET + "\t\t\t\t\t");
                    }
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
                        " _    _ _____ _     _____ ________  ________   _____ _____   ___  ____   __  _____ _   _ _____ _    ______ _____ _____ _ \n" +
                        "| |  | |  ___| |   /  __ \\  _  |  \\/  |  ___| |_   _|  _  |  |  \\/  \\ \\ / / /  ___| | | |  ___| |   |  ___|_   _|  ___| |\n" +
                        "| |  | | |__ | |   | /  \\/ | | | .  . | |__     | | | | | |  | .  . |\\ V /  \\ `--.| |_| | |__ | |   | |_    | | | |__ | |\n" +
                        "| |/\\| |  __|| |   | |   | | | | |\\/| |  __|    | | | | | |  | |\\/| | \\ /    `--. \\  _  |  __|| |   |  _|   | | |  __|| |\n" +
                        "\\  /|  / |___| |___| \\__/| \\_/ / |  | | |___    | | \\ \\_/ /  | |  | | | |   /\\__/ / | | | |___| |___| |    _| |_| |___|_|\n" +
                        " \\/  |/\\____/\\_____/\\____/\\___/\\_|  |_|____/    \\_/  \\___/   \\_|  |_/ \\_/   \\____/\\_| |_|____/\\_____|_|    \\___/\\____/(_)\n" + RESET);
                System.out.print("Insert your nickname:\t");
                break;
            case LOBBY:
                if (client.isOwner()) {
                    System.out.println("COMMANDS AVAILABLE:");
                    System.out.println(ConsoleColors.GREEN_UNDERLINED + "/start"+ RESET + " to start the game");
                    System.out.println(ConsoleColors.GREEN_UNDERLINED + "/firstMatch"+ RESET + " if this is your first match\t\tOR\t\t" + ConsoleColors.GREEN_UNDERLINED +"/notFirstMatch" + RESET + " if you have already played");
                } else
                    System.out.println(ConsoleColors.CYAN_UNDERLINED + "wait for the owner to start the game" + RESET);

                for(Player p: client.getBoardView().getListOfPlayer()){
                    System.out.println("///" + p.getNickname());
                }
                chat();
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
                    //showYourScore();
                    chat();
                }
                else{
                    System.out.println(client.getBoardView().getCurrentPlayer().getNickname() + " is playing...Wait your turn!");
                    showBoard();
                    showYourShelf();
                    showOthersShelf();
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
                }else System.out.println(ConsoleColors.RED_BOLD + "There is no winner" + ConsoleColors.WHITE);
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

    /*private void showYourScore() throws RemoteException {
        for(Player p: client.getBoardView().getListOfPlayer()) {
            if (p.getNickname().equals(client.getNickname()))
                System.out.println(ConsoleColors.PURPLE_UNDERLINED + "YOUR SCORE:\t" + p.getScore() + RESET);
        }
    }*/

    private void showGoals() throws RemoteException {
        System.out.print("COMMON GOALS:\t\t");
        client.getBoardView().getSetOfCommonGoal().forEach((goal) -> System.out.println(goal.getName()));
        System.out.print("PERSONAL GOALS:\t\t");
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

    public void help() throws RemoteException {
        System.out.println("\t\t\t" + ConsoleColors.GREEN_UNDERLINED + "COMMANDS AVAILABLE:" + RESET);
        if(this.state.equals(State.PLAY)) {
            if (!removed) {
                System.out.println(ConsoleColors.GREEN_UNDERLINED + "/remove row column" + RESET + "  ---> to remove the tile[row][column] from the board");
                System.out.println(ConsoleColors.GREEN_UNDERLINED + "/remove row1 column1 [row2 column2] [row3 column3]" + RESET + "  ---> to remove 2 or 3 tiles from the board\n");
            } else {
                if (!added) {
                    System.out.println(ConsoleColors.GREEN_UNDERLINED + "/switch t1 t2" + RESET + "  ---> to switch tile t1 and t2 in the tile buffer (the first tile from left is tile number 0)");
                    System.out.println(ConsoleColors.GREEN_UNDERLINED + "/add C" + RESET + "  ---> to add tiles from the buffer to the column C of your shelf");
                }
            }
            System.out.println(ConsoleColors.GREEN_UNDERLINED + "/cg of /pg" + RESET + "  ---> to see common goals or private goals");
        }
        if(this.state.equals(State.LOBBY) && client.isOwner()) System.out.println(ConsoleColors.GREEN_UNDERLINED + "/start" + RESET + "  ---> to start the game");
        System.out.println(ConsoleColors.GREEN_UNDERLINED + "/chat message" + RESET + "  ---> to send a message to everyone");
        System.out.println(ConsoleColors.GREEN_UNDERLINED + "/whisper addressee message" + RESET + "  ---> to send a private message to another player");
        System.out.println(ConsoleColors.GREEN_UNDERLINED + "/exit" + RESET + "  ---> close the app, if a match is still going you can rejoin");
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
            if (!removed) {
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
                            removed = inLine(tmpInput);
                            if (removed) added = false;
                            return inLine(tmpInput);
                        }
                    }
                }
            }
            else System.out.println("command /remove already used");
            return false;
        }
        if(in.startsWith("/add")){
                return columnAvailable(client.getBoardView().getTileBuffer().size(), tmpInput[1].charAt(0) - 48);
        }
        if(in.startsWith("/switch")) return client.getBoardView().getTileBuffer().size() > 1;
        if(in.startsWith("/end")) return true;
        if(in.equals("/help") || in.equals("/h")) return true;
        if(in.startsWith("/whisper")) return true;
        if(in.startsWith("/chat")) return true;
        if(in.startsWith("/cg") || in.startsWith("/pg")) return true;
        return false;
    }

    void printGoalShelf(Shelf gShelf){
        for (int i=0; i<6; i++){
            System.out.println("----------");
            System.out.print("|");
            for(int j=0; j<5; j++){
                if (gShelf.isEmpty(i, j)) System.out.print(" |");
                else {
                    if(gShelf.getTile(i,j).getColor().equals(Color.LIGHTBLUE))
                        System.out.print(ConsoleColors.BLUE_BACKGROUND_BRIGHT + " " + RESET + "|");
                    if(gShelf.getTile(i, j).getColor().equals(Color.GREEN))
                        System.out.print(ConsoleColors.GREEN_BACKGROUND + " " + RESET + "|");
                    if(gShelf.getTile(i, j).getColor().equals(Color.PINK))
                        System.out.println(ConsoleColors.PURPLE_BACKGROUND_BRIGHT + " " + RESET + "|");
                }

            }
        }
    }
   /* Shelf crateGoalShelf(String g){
        Shelf gShelf = new Shelf();
        switch (g){
            case("")
        }
        return gShelf;

    }*/
}
