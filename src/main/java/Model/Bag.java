package Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static Model.Color.*;

/**
 * The bag generates and distributes a finite arraylist of tiles, for each board a single bag is associated
 * @author Alessio
 */
public class Bag {
    private List<Tile> containedTiles;
    public Bag() {
        containedTiles = new ArrayList<Tile>();
        for (int i = 0; i < 133; i++) {

            Random rand = new Random();
            switch (rand.nextInt(6)){
                case 0: containedTiles.add(new Tile(WHITE));
                    break;
                case 1: containedTiles.add(new Tile(YELLOW));
                    break;
                case 2: containedTiles.add(new Tile(PINK));
                    break;
                case 3: containedTiles.add(new Tile(BLUE));
                    break;
                case 4: containedTiles.add(new Tile(LIGHTBLUE));
                    break;
                case 5: containedTiles.add(new Tile(GREEN));
                    break;
            }

        }
    }

    /**
     * Removes a tile from the bag and returns it
     * @return returns a tile removing it from the initial arraylist
     * @author Alessio
     */
    public Tile newTile(){
        if(containedTiles.size()>0) {
            return containedTiles.remove(0);
        }else return null;
    }
}
