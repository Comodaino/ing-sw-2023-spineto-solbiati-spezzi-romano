package Model.CommonGoals;

import Model.Player;

public class GoalSquares extends CommonGoal{
    public GoalSquares(){ super(); }
    @Override
    public int getScore(Player p) {
        int counter = 0;
        boolean[][] foundMatrix = new boolean[6][5];
        for(int i=0; i<6; i++){
            for(int j=0; j<5; j++){
                foundMatrix[i][j] = false;
            }
        }

        for(int i=0; i<5; i++){
            for(int j=0; j<4; j++){
                if(((!foundMatrix[i][j] && !foundMatrix[i+1][j+1])&&(!foundMatrix[i+1][j] && !foundMatrix[i][j+1]))){
                    if(p.getShelf().getTile(i,j)!=null && p.getShelf().getTile(i+1,j+1)!=null &&
                            p.getShelf().getTile(i+1,j)!=null && p.getShelf().getTile(i,j+1)!=null){

                        if(((p.getShelf().getTile(i,j).getColor().equals(p.getShelf().getTile(i+1,j+1).getColor())) &&
                                (p.getShelf().getTile(i+1,j).getColor().equals(p.getShelf().getTile(i,j+1).getColor()))) &&
                                p.getShelf().getTile(i,j).getColor().equals(p.getShelf().getTile(i,j+1).getColor())){
                            counter++;
                            foundMatrix[i][j] = true;
                            foundMatrix[i + 1][j] = true;
                            foundMatrix[i + 1][j + 1] = true;
                            foundMatrix[i][j + 1] = true;
                        }
                    }
                }
            }
        }
        if(counter>=4 && !this.completed.contains(p)) return assignScore(p);
        return 0;
    }
}
