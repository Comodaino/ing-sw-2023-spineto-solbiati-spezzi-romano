package Model;

import java.io.Serializable;

/**
 * Pgtype is the type defining Personal Goals.
 * Parameters line and col that represent coordinate, and color of Tile
 * @author alessandra
 */
public class Pgtype implements Serializable {
    private int line,col;
    private Color color;

    /**
     * Constructor of Pgtype
     * @param line
     * @param col
     * @param color
     */
    public Pgtype(int line, int col, Color color) {
        this.line = line;
        this.col = col;
        this.color = color;
    }
    public int getLine(){
        return line;
    }
    public void setLine(int line){
        this.line=line;
    }
    public int getCol() {
        return col;
    }
    public void setCol(int col) {
        this.col = col;
    }
    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}

