package Model;

import Model.CommonGoals.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import static Model.CellType.*;


public class Board{
    private Cell[][] matrix;
    private List<Player> listOfPlayer;
    private boolean firstMatch;
    private Set<CommonGoal> setOfCommonGoal;
    private boolean firstToEnd;
    private List<Tile> tileBuffer;

    private GoalFactory goalFactory;

    private Bag bag;

    public Board(boolean fm, List<Player> pl){

        matrix = new Cell[9][9];
        recharge();
        bag = new Bag();

        try {
            File boardConf = new File("board_conf.json");
            Scanner reader = new Scanner(boardConf);

            for(int i = 0; i<9 && reader.hasNextLine(); i++) {
                String data = reader.nextLine();
                CellType type = null;
                for (int j = 0; j<9; j++){
                    switch((int)data.charAt(j)){
                        case 1: type = ONE;
                        break;
                        case 2: type = TWO;
                            break;
                        case 3: type = THREE;
                            break;
                        case 4: type = FOUR;
                            break;

                    }
                    this.matrix[j][i] = new Cell(type);
                }
            }

            reader.close();
        }catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        };

        this.listOfPlayer = pl;
        this.firstMatch = fm;
        this.firstToEnd = true;
        this.goalFactory = new GoalFactory();
        tileBuffer = new ArrayList<Tile>();

//GOALS ARE MISSING
        setOfCommonGoal = new HashSet<CommonGoal>();
        Random rand = new Random();
        setOfCommonGoal.add(goalFactory.getGoal(rand.nextInt(11)));

        if(fm) setOfCommonGoal.add(goalFactory.getGoal(rand.nextInt(11)));

    }





    public void removeTile(int r, int c){
        tileBuffer.add(matrix[r][c].getTile());
        matrix[r][c].removeTile();
    }
    public void checkRecharge(){
        boolean found=false;
        for(int i=0; i<9; i++){
            for(int j=0; j<9; j++){

                for(int k=-1; k<1; k++){
                    for(int h=-1; h<1; h++){

                        for(int a=-1; a<1; a++){
                            for(int b=-1; b<1; b++){
                                if(matrix[i][j]!=matrix[i+k][j+h] && matrix[i+k][j+h]!=matrix[i+k+a][j+h+b]) {
                                    if (!matrix[i][j].isEmpty() && (!matrix[i + k][j + h].isEmpty() && !matrix[i + k + a][j + h + b].isEmpty()))
                                        found = true;
                                }
                            }
                        }
                    }
                }
            }
        }

        if(found) recharge();
    }
    public void recharge(){
        for(int i=0; i<9; i++) {
            for (int j = 0; j < 9; j++) {
                if(matrix[i][j].isEmpty() && matrix[i][j].getType()!=ONE) matrix[i][j].insertTile(bag.newTile());
            }
        }
    }

    public Tile getTile(int r, int c){
        return matrix[r][c].getTile();
    }

    public Cell getCell(int r, int c){
        return matrix[r][c];
    }

    public void endMatch(){
        return;
    }


}
