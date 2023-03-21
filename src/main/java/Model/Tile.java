package Model;
import Model.Color.*;

public class Tile{
    private Color color;

    public Tile(Color c){

        this.color = c;
    }

    public Color getColor(){
        return this.color;
    }
}