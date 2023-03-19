package Model;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class Tile{
    private Color color;

    public void Tile(Color c){

        this.color = c;
    }

    public Color getColor(){
        return this.color;
    }
    public Color getColor(){ return this.color; }
}