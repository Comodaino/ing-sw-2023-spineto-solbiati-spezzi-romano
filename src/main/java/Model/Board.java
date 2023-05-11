package Model;

import Model.CommonGoals.*;

import java.io.File;
import java.io.FileNotFoundException;
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
    private List<Player> listOfPlayer;
    private boolean firstMatch;
    private Set<CommonGoal> setOfCommonGoal;
    private EndGoal endGoal;
    private List<Tile> tileBuffer;
    private GoalFactory goalFactory;
    private Bag bag;
    private Player currentPlayer;
    public final BoardView boardView;
    private final int[] coordBuffer;

    /**
     * Constructor of the board
     * @param fm represents if it's the first match for the players
     * @param pl list of the players
     */
    public Board(boolean fm, List<Player> pl) {

        matrix = new Cell[9][9];
        bag = new Bag();

        try {
            File boardConf = new File("src/main/java/Model/Conf/board_conf");
            Scanner reader = new Scanner(boardConf);

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
        }catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        };

        this.coordBuffer = new int[]{-1, -1, -1, -1, -1, -1};
        this.listOfPlayer = pl;
        this.firstMatch = fm;
        this.endGoal = new EndGoal();
        this.goalFactory = new GoalFactory();
        tileBuffer = new ArrayList<Tile>();
        recharge();

        setOfCommonGoal = new HashSet<CommonGoal>();
        Random rand = new Random();
        setOfCommonGoal.add(goalFactory.getGoal(rand.nextInt(11), listOfPlayer.size()));
        if(fm) setOfCommonGoal.add(goalFactory.getGoal(rand.nextInt(11), listOfPlayer.size()));

        this.boardView = new BoardView(this);
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
            coordBuffer[i] = r;
            coordBuffer[i + 1] = c;
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
                    if(!matrix[i+1][j].isEmpty()){
                        found=true;
                    }
                    if(!matrix[i][j+1].isEmpty()){
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

    public void addPlayer(Player player){ listOfPlayer.add(player); }

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
    public void setCurrentPlayer(Player cp){ this.currentPlayer= cp;}

    public int[] getCoordBuffer() { return coordBuffer;}
}
