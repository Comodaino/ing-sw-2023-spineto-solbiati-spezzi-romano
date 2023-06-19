package Model;

import Model.CommonGoals.CommonGoal;

import java.io.InputStream;
import java.io.Serializable;
import java.util.*;

import static Model.CellType.*;

/**
 * The board represents the entirety of the game model, it contains everything and grants access to everything using getter methods,
 * it contains a set of common goals, a list of player the board itself made of a matrix of cells, a bag and a goal factory
 * @author Alessio
 */
public class Board implements Serializable {
    private Cell[][] matrix;
    private final List<Player> listOfPlayer;
    private List<Player> donePlayers;
    private Player winner;
    private Set<CommonGoal> setOfCommonGoal;
    private EndGoal endGoal;
    private List<Tile> tileBuffer;
    private Bag bag;
    private Player currentPlayer;
    private boolean fm;
    private final BoardView boardView;
    private final List<String> chatBuffer;
    private final List<Whisper> personalChatBuffer;
    /**
     * Constructor of the board
     * @param fm represents if it's the first match for the players
     *
     */
    public Board(boolean fm) {
        chatBuffer = new ArrayList<>();
        personalChatBuffer = new ArrayList<>();
        this.listOfPlayer = new ArrayList<>();
        this.boardView = new BoardView(this);
        this.fm = fm;
    }

    /**
     * initializes the board itself, gives goals and shelf to the player, selects common goals
     */
    public void init(){
        matrix = new Cell[9][9];
        bag = new Bag();


        Random rand = new Random();
        listOfPlayer.get(rand.nextInt(listOfPlayer.size())).setAsChair();


        InputStream is = getClass().getClassLoader().getResourceAsStream("board_conf");
        // File boardConf = new File("src/main/java/Model/Conf/board_conf");
        assert is != null;
        Scanner reader = new Scanner(is);

        for(int i = 0; i<9 && reader.hasNextLine(); i++) {
            String data = reader.nextLine();
            CellType type = null;
            for (int j = 0; j<9; j++){
                switch((int)data.charAt(j) - 48){
                    case 1: type = ONE;
                        break;
                    case 2: type = TWO;
                        break;
                    case 3: type = THREE;
                        break;
                    case 4: type = FOUR;
                        break;

                }
                this.matrix[i][j] = new Cell(type);
            }
        }

        reader.close();

        this.donePlayers = new ArrayList<Player>();
        for(Player p: listOfPlayer){
            System.out.println(":: " + p.getNickname());
        }
        this.endGoal = new EndGoal();
        GoalFactory goalFactory = new GoalFactory();
        tileBuffer = new ArrayList<Tile>();
        recharge();

        setOfCommonGoal = new HashSet<CommonGoal>();
        setOfCommonGoal.add(goalFactory.getGoal(rand.nextInt(11), listOfPlayer.size()));
        if(fm) {
            CommonGoal tmpGoal;
            do {
                tmpGoal = goalFactory.getGoal(rand.nextInt(11), listOfPlayer.size());
            }while(!setOfCommonGoal.contains(tmpGoal));
            setOfCommonGoal.add(tmpGoal);
        }
    }

    /**
     * Adds a player to the model
     * @param player
     */
    public void addPlayer(Player player){
        synchronized (listOfPlayer){
            listOfPlayer.add(player);
        }

    }


    /**
     * removes a single tile from the specified coordinates of the board, it does nothing is the cell is empty
     * @param r row coordinate to remove
     * @param c column coordinate to remove
     * @author Alessio
     */
    public void removeTile(int r, int c){
        if(!matrix[r][c].isEmpty()) {
            tileBuffer.add(matrix[r][c].getTile());
            matrix[r][c].removeTile();
            int i = tileBuffer.size() - 1;
        }
    }

    /**
     * checks if it's not possible for a player to take more than one tile, it also recharges is automatically
     * @author Alessio
     */
    public void checkRecharge(){
        boolean found=false;
        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){
                if(!matrix[i][j].isEmpty()){
                    if(!matrix[i+1][j].isEmpty() && adjacentFree(i+1, j)){
                        found=true;
                    }
                    if(!matrix[i][j+1].isEmpty() && adjacentFree(i, j+1)){
                        found=true;
                    }
                }
            }
        }

        if(!found) recharge();
    }

    /**
     * recharges the board taking tile from the bag
     * @author Alessio
     */
    public void recharge(){
        for(int i=0; i<9; i++) {
            for (int j = 0; j < 9; j++) {
                switch(this.getListOfPlayer().size()){
                    case 2: if((matrix[i][j].isEmpty() && matrix[i][j].getType()!=ONE) && (matrix[i][j].getType()!=FOUR && matrix[i][j].getType()!=THREE)) matrix[i][j].insertTile(bag.newTile());
                        break;
                    case 3: if((matrix[i][j].isEmpty() && matrix[i][j].getType()!=ONE) && (matrix[i][j].getType()!=FOUR)) matrix[i][j].insertTile(bag.newTile());
                        break;
                    case 4: if(matrix[i][j].isEmpty() && matrix[i][j].getType()!=ONE) matrix[i][j].insertTile(bag.newTile());
                        break;
                }
            }
        }
    }

    public Tile getTile(int r, int c){
        return matrix[r][c].getTile();
    }

    public Cell getCell(int r, int c){
        return matrix[r][c];
    }

    public List<Tile> getTileBuffer() {
        return tileBuffer;
    }

    public List<Player> getListOfPlayer() {
        return listOfPlayer;
    }

    public Set<CommonGoal> getSetOfCommonGoal() {
        return setOfCommonGoal;
    }

    public EndGoal getEndGoal() {
        return endGoal;
    }
    public Player getCurrentPlayer() { return currentPlayer; }
    public void setCurrentPlayer(Player cp){ this.currentPlayer= cp; }

    public List<String> getChatBuffer() { return chatBuffer; }
    public List<Whisper> getPersonalChatBuffer(){ return personalChatBuffer;}

    public Player getWinner() {
        return winner;
    }
    public boolean adjacentFree(int r, int c){
        if(this.getCell(r, c).isEmpty()) return false;
        if(r == 0 || c == 0 ) return true;
        if(r == 8 || c == 8) return true;
        return (this.getCell(r + 1, c).isEmpty() || this.getCell(r, c + 1).isEmpty()) || (this.getCell(r + 1, c).isEmpty() || this.getCell(r, c + 1).isEmpty());
    }
    public void addToDone(Player p){ this.donePlayers.add(p);}

    public List<Player> getDonePlayers() {
        return donePlayers;
    }
}
