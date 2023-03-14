package Model;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;


public class Board {
    private Cell matrix[9][9];
    private List<Player> listOfPlayer;
    private boolean firstMatch;
    private Set<CommonGoal> setOfCommonGoal;
    private boolean firstToEnd;
    private List<Tile> tileBuffer;


    public Board Board(int np, boolean fm, List<Player> pl){



        try {
            File boardConf = new File("board.conf");
            Scanner reader = new Scanner(boardConf);

            for(int i = 0; i<9 && reader.hasNextLine; i++) {
                String data = reader.nextLine();
                for (int j = 0; j<9; j++){
                    this.matrix[j][i] = new Cell(data[j]);
                }
            }

            reader.close();
        } catch (FileNotFoundException e);

        this.listOfPlayer = pl;
        this.firstMatch = fm;
        this.firstToEnd = true;
        tileBuffer = new List<Tile>;

//GOALS ARE MISSING
        setOfCommonGoal = new Set<CommonGoal>;
        tempCG = null;
        Random rand = new Random()
        if(fm = true){
            rand.nextInt(10);
            switch rand.nextInt(10){
                case 0: tempCG = setOfCommonGoal.insert(new GoalAngles);
                    break;
            }
        }else{
            rand.nextInt(10);
            switch rand.nextInt(10){
                case 0: tempCG = setOfCommonGoal.insert(new GoalAngles);
                    break;
            }
            rand.nextInt(10);
            switch rand.nextInt(10){
                case 0: tempCG = setOfCommonGoal.insert(new GoalAngles);
                    break;
            }
            setOfCommonGoal.insert(tempCG);
        }

        throw new NotImplementedException;
    }




    public void endMatch(){
        throw new NotImplementedException;
    }
    public void removeTile(Tile tile){
        throw new NotImplementedException;
    }
    public void checkRecharge(){
        throw new NotImplementedException;
    }
}
