package Model;

import java.util.Random;

/**
 * represent Tile with attribute Color and TileType
 * @author Clara
 */
public class Tile{
    private Color color;
    private TileType type;

    public Tile(Color c){
        Random rand = new Random();
        switch(rand.nextInt(3)){
            case 0: type= TileType.ONE;
                break;
            case 1: type= TileType.TWO;
                break;
            case 2: type= TileType.THREE;
                break;
        }
        this.color = c;
    }

    public Color getColor(){
        return this.color;
    }

    public TileType getType() {
        return type;
    }
}