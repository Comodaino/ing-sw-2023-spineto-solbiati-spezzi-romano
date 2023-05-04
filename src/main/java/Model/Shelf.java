package Model;

/**
 * represent Shelf
 * @author  Clara Spezzi
 */
public class Shelf {
    private Tile[][] matrix;

    public Shelf() {
        this.matrix = new Tile[6][5];
    }

    public void setShelf(Tile[][] m) {
        this.matrix = m;
    }

    /**
     * this method takes a column and a tile and places the tile into the specified column
     * of the Shelf and returns true. If the column is full the method return false
     *
     * @param c Shelf column
     * @param t adding Tile
     * @return true if the Tile is correctly added, false otherwise
     */
    public boolean addTile(int c, Tile t) {
        for (int r = 0; r < 6; r++) {
            if (matrix[r][c] == null) {
                matrix[r][c] = t;
                return true;
            }
        }
        return false;
    }


    public Tile getTile(int r, int c) {
        return matrix[r][c];
    }

    public Tile[][] getShelf() {
        return this.matrix;
    }

    public void setMatrix(Tile[][] m) {
        this.matrix = m;
    }

    /**
     * isFull return true if the specific Shelf is already full, otherwise returns false
     * @return is <strong>true</strong> if the specific Shelf is already full, otherwise it is <strong>false</strong>
     */
    public boolean isFull() {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                if (this.matrix[i][j].equals(null)) {
                    return false;
                }
            }
        }
        return true;
    }
}

