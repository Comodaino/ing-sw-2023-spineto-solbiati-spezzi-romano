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

        for(Color color : Color.values()){
            numOfSquares = 0;
            for (int r=1; r<6 && numOfSquares<2; r++) {
                for (int c=1; c<5 && numOfSquares<2; c++) {
                    if (p.getShelf().getTile(r, c) != null && p.getShelf().getTile(r - 1, c) != null &&
                            p.getShelf().getTile(r, c - 1) != null && p.getShelf().getTile(r - 1, c - 1) != null) {
                        if(p.getShelf().getTile(r, c).getColor().equals(color) &&
                                p.getShelf().getTile(r, c).getColor().equals(p.getShelf().getTile(r-1, c).getColor()) &&
                                p.getShelf().getTile(r, c).getColor().equals(p.getShelf().getTile(r, c-1).getColor()) &&
                                p.getShelf().getTile(r, c).getColor().equals(p.getShelf().getTile(r-1, c-1).getColor())) {

                            numOfSquares++;
                            foundMatrix[r][c] = true;
                            foundMatrix[r - 1][c] = true;
                            foundMatrix[r][c - 1] = true;
                            foundMatrix[r - 1][c - 1] = true;
                        }
                    }
                }
            }
            if(numOfSquares==2 && !this.completed.contains(p)){
                return assignScore(p);
            }
        }
        return 0;
    }
}