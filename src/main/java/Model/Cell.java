package Model;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
public class Cell {
    private int type;
    private Tile containedTile;

    public Cell Cell(int t){
        this.type=t;
        throw new NotImplementedException();
    }
    public boolean isEmpty(){
        throw new NotImplementedException();
    }
    public Tile getTile(){
        return containedTile;
    }
    public void removeTile(){
        containedTile=null;
    }
    public void insertTile(Tile t){
        this.containedTile= t;
    }
}
