package Model.CommonGoals;

import Model.Player;

/**
 * GoalCouples is a class which extends the class CommonGoal.
 * It represents the "couples" common goal card: six groups each containing at least 2 tiles of the same type;
 * the tiles of one group can be different from those of another group.
 * @author Nicolò
 */
public class GoalCouples extends CommonGoal{
    public GoalCouples (int numOfPlayer){
        super(numOfPlayer);
    }
    @Override
    public int getScore(Player p){
        int numOfCouples = 0;
        boolean[][] foundMatrix = new boolean[6][5];
        for(int i=0; i<6; i++){
            for(int j=0; j<5; j++){
                foundMatrix[i][j] = false;
            }
        }

        for(int r=0; r<6; r++){
            for(int c=0; c<4; c++){
                if(p.getShelf().getTile(r, c)!=null && p.getShelf().getTile(r, c+1)!=null &&
                        p.getShelf().getTile(r, c).getColor().equals(p.getShelf().getTile(r, c+1).getColor()) &&
                        !foundMatrix[r][c] && !foundMatrix[r][c+1]){
                    numOfCouples++;
                    search(r, c, p, foundMatrix);
                }
            }
        } //search "horizontal couples"

        for(int c=0; c<5; c++){
            for(int r=0; r<5; r++){
                if(p.getShelf().getTile(r, c)!=null && p.getShelf().getTile(r+1, c)!=null &&
                        p.getShelf().getTile(r, c).getColor().equals(p.getShelf().getTile(r+1, c).getColor()) &&
                        !foundMatrix[r][c] && !foundMatrix[r+1][c]){
                    numOfCouples++;
                    search(r, c, p, foundMatrix);
                }
            }
        } //search "vertical couples"

        //System.out.println(numOfCouples);
        if(numOfCouples==6 && !this.completed.contains(p)){
            return assignScore(p);
        }

        return 0;
    }

    public void search(int r, int c, Player p, boolean[][] foundMatrix) {
        foundMatrix[r][c]=true;
        if(r+1<6){
            if(p.getShelf().getTile(r + 1, c) != null && !foundMatrix[r+1][c]){
                if(p.getShelf().getTile(r, c).getColor().equals(p.getShelf().getTile(r + 1, c).getColor())){
                    search(r + 1, c, p, foundMatrix);
                }
            }
        }

        if(c+1<5){
            if(p.getShelf().getTile(r, c + 1) != null && !foundMatrix[r][c+1]) {
                if(p.getShelf().getTile(r, c).getColor().equals(p.getShelf().getTile(r, c + 1).getColor())){
                    search(r, c + 1, p, foundMatrix);
                }
            }
        }
    }
}