package Model;

public class NearGoal extends Goal{
    private int score;
    private boolean matrix[][];
    public NearGoal(){
        this.score=0;
        this.matrix = new boolean[6][5];
        for(int i=0; i<6; i++){
            for(int j=0; j<5; j++){
                this.matrix[i][j]=false;
            }
        }
    }
    public int getScore(Player p){
        int counter=0;
        for(int i=0; i<6; i++){
            for(int j=0; j<6; j++) {
                matrix[i][j]=true;
                counter=iterator(i,j,p);
                switch (counter){
                    case 0: score+=2;
                        break;
                    case 1: score+=2;
                        break;
                    case 2: score+=2;
                        break;
                    case 3: score+=2;
                    break;
                    case 4: score+=3;
                        break;
                    case 5: score+=5;
                        break;
                    default: score+=8;
                        break;

                }
            }
        }
        return score;
    }
    public int iterator(int i, int j, Player p){
        int counter=0;
        if( i < 6 && p.getShelf().getTile(i+1,j)!=null) {
            if (p.getShelf().getTile(i, j).getColor().equals(p.getShelf().getTile(i + 1, j).getColor())) {
                counter += 1;
                matrix[i][j] = true;
                counter += iterator(i + 1, j, p);
            }
        }
        if( j < 5  && p.getShelf().getTile(i+1,j)!=null) {
            if (p.getShelf().getTile(i, j).getColor().equals(p.getShelf().getTile(i, j + 1).getColor())) {
                counter += 1;
                matrix[i][j] = true;
                counter += iterator(i, j + 1, p);
            }
        }
            return counter;
    }
    @Override
    public boolean isCompleted() {
        return false;
    }
}
