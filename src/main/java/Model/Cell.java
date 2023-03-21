package Model;
public class Cell {
    private CellType type;
    private Tile containedTile;

    public Cell(CellType t){
        this.type =t;
    }
    public boolean isEmpty(){
        if (containedTile==null) return false;
            else return true;
    }
    public Tile getTile(){
        if(this.isEmpty()) return null;
        return containedTile;
    }
    public void removeTile(){
        containedTile=null;
    }
    public void insertTile(Tile t){
        this.containedTile= t;
    }
    public CellType getType(){ return this.type; }
}
