package Model;

public class NearGoal extends Goal {
    private int score;
    private boolean matrix[][];

    public NearGoal() {
        this.score = 0;
        this.matrix = new boolean[6][5];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                this.matrix[i][j] = false;
            }
        }
    }

    public int getScore(Player p) {

        for (int i = 0; i < 6; i++) {
            int counter = 0;
            for (int j = 0; j < 5; j++) {
                matrix[i][j] = true;
                counter = iterator(i, j, p);
                switch (counter) {
                    case 0:
                        score += 0;
                        break;
                    case 1:
                        score += 0;
                        break;
                    case 2:
                        score += 0;
                        break;
                    case 3:
                        score += 2;
                        break;
                    case 4:
                        score += 3;
                        break;
                    case 5:
                        score += 5;
                        break;
                    default:
                        score += 8;
                        break;
                }
            }
        }
        return score;
    }

    public int iterator(int i, int j, Player p) {
        int counter = 0;
        matrix[i][j]=true;
        if (i+1<6) {
            if (p.getShelf().getTile(i + 1, j) != null && !matrix[i+1][j]) {
                if (p.getShelf().getTile(i, j).getColor().equals(p.getShelf().getTile(i + 1, j).getColor())) {
                    counter += iterator(i + 1, j, p);
                }
            }
        }
        if(j+1<5) {
            if (p.getShelf().getTile(i, j + 1) != null && !matrix[i][j+1]) {
                if (p.getShelf().getTile(i, j).getColor().equals(p.getShelf().getTile(i, j + 1).getColor())) {
                    counter += iterator(i, j + 1, p);
                }
            }
        }
        return counter + 1;
    }

}
