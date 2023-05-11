package Model;

import java.io.Serializable;

/**
 * Cell represents a single cell that contained a Tile
 * @author alessandra
 */
public class Cell implements Serializable {
    private CellType type;
    private Tile containedTile;

    /**
     * Constructor of Cell
     * @param t, the type of Cell based on players' number
     */
    public Cell(CellType t){
        this.type =t;
        this.containedTile = null;
    }

    /**
     * this method checks if the Cell is Empty
     * @return true if is Empty, false if not
     */
    public boolean isEmpty(){
        if (containedTile==null) return true;
            else return false;
    }
    public Tile getTile(){
        if(this.isEmpty()) return null;
        return containedTile;
    }

    /**
     * remove Tile from Cell
     */
    public void removeTile(){
        containedTile=null;
    }

    /**
     * insert Tile in a Cell
     * @param t, Tile
     */
    public void insertTile(Tile t){
        this.containedTile= t;
    }
    public CellType getType(){ return this.type; }
}
