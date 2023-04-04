package Model.CommonGoals;

import Model.Color;
import Model.Player;

public class GoalSquares extends CommonGoal{
    public GoalSquares(int numOfPlayer){ super(numOfPlayer); }
    @Override
    public int getScore(Player p) {
        int numOfSquares = 0;
        boolean[][] foundMatrix = new boolean[6][5];
        for(int i=0; i<6; i++){
            for(int j=0; j<5; j++){
                foundMatrix[i][j] = false;
            }
        }

        for (int r=1; r<6; r++){
            for (int c=1; c<5; c++) {
                if (p.getShelf().getTile(r, c) != null && p.getShelf().getTile(r - 1, c) != null &&
                        p.getShelf().getTile(r, c - 1) != null && p.getShelf().getTile(r - 1, c - 1) != null) {
                    if(p.getShelf().getTile(r, c).getColor().equals(p.getShelf().getTile(r-1, c).getColor()) &&
                            p.getShelf().getTile(r, c).getColor().equals(p.getShelf().getTile(r, c-1).getColor()) &&
                            p.getShelf().getTile(r, c).getColor().equals(p.getShelf().getTile(r-1, c-1).getColor())) {

                        if(countTile(r-1, c -1, p, foundMatrix)==4){
                            numOfSquares++;
                        }
                    }
                }
            }
        }
        if(numOfSquares==2 && !this.completed.contains(p)){
                return assignScore(p);
        }
        return 0;
    }

    public int countTile(int i, int j, Player p, boolean[][] foundMatrix) {

        int counter = 0;
        foundMatrix[i][j]=true;
        if (i+1<6) {

            if (p.getShelf().getTile(i + 1, j) != null && !foundMatrix[i+1][j]) {
                if (p.getShelf().getTile(i, j).getColor().equals(p.getShelf().getTile(i + 1, j).getColor())) {
                    counter += countTile(i + 1, j, p, foundMatrix);
                }
            }
        }
        if(j+1<5) {
            if (p.getShelf().getTile(i, j + 1) != null && !foundMatrix[i][j+1]) {
                if (p.getShelf().getTile(i, j).getColor().equals(p.getShelf().getTile(i, j + 1).getColor())) {
                    counter += countTile(i, j + 1, p, foundMatrix);
                }
            }
        }
        return counter + 1;
    }
}