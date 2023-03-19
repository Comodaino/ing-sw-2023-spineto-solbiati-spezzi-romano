package Model;


public class Shelf{
    private Tile[][] matrix;

    public Shelf() {

        this.matrix = new Tile[6][5];
    }

    public boolean addTile(int c, Tile t) {

        for (int r = 0; r < 6; r++) {
            if (matrix[r][c] == null) {
                matrix[r][c] = t;
                return true;
            }
        }
        return false;
    }


    public Tile getTile(int r, int c){

        return matrix[r][c];
    }
}

