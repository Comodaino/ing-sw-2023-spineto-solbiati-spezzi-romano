package Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static Model.Color.*;


public class Bag {
    private List<Tile> containedTiles;

    public Bag() {
        containedTiles = new ArrayList<Tile>();
        for (int i = 0; i < 133; i++) {

            Random rand = new Random();
            rand.nextInt(6);
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
    public Tile newTile(){
        Tile tmp = containedTiles.get(0);
        containedTiles.remove(0);
        return tmp;
    }
}
