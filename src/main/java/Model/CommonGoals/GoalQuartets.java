package Model.CommonGoals;

import Model.Player;

/**
 * GoalQuartets is a class which extends the class CommonGoal.
 * It represents the "quartets" common goal card: four groups each containing at least 4 tiles of the same type;
 * the tiles of one group can be different from those of another group.
 * @author Nicolò
 */
public class GoalQuartets extends CommonGoal{
    public GoalQuartets(int numOfPlayer){
        super(numOfPlayer);
    }
    private static final String name ="GoalQuartets";
    @Override
    public int getScore(Player p){
        int numOfQuartets = 0, n = 0;
        boolean[][] foundMatrix = new boolean[6][5];
        for(int i=0; i<6; i++){
            for(int j=0; j<5; j++){
                foundMatrix[i][j] = false;
            }
        }

        for(int r=0; r<6; r++){
            n = 0;
            for(int c=0; c<4; c++){
                if(p.getShelf().getTile(r, c)!=null && p.getShelf().getTile(r, c+1)!=null &&
                        p.getShelf().getTile(r, c).getColor().equals(p.getShelf().getTile(r, c+1).getColor()) &&
                        !foundMatrix[r][c] && !foundMatrix[r][c+1]){
                    n++;
                } else {
                    n = 0;
                }
                if(n==3){
                    numOfQuartets++;
                    search(r, c-2, p, foundMatrix);
                    break;
                }
            }
        } //search "horizontal quartets"

        for(int c=0; c<5; c++){
            n = 0;
            for(int r=0; r<5; r++){
                if(p.getShelf().getTile(r, c)!=null && p.getShelf().getTile(r+1, c)!=null &&
                        p.getShelf().getTile(r, c).getColor().equals(p.getShelf().getTile(r+1, c).getColor()) &&
                        !foundMatrix[r][c] && !foundMatrix[r+1][c]){
                    n++;
                } else {
                    n = 0;
                }
                if(n==3){
                    numOfQuartets++;
                    search(r-2, c, p, foundMatrix);
                    break;
                }
            }
        } //search "vertical quartets"

        //System.out.println(numOfQuartets);
        if(numOfQuartets==4 && !this.completed.contains(p)){
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