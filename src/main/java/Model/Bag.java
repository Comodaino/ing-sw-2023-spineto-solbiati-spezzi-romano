package Model;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class Bag {
    private List<Tile> containedTiles;

    public Bag Bag() {
        for (int i = 0; i < 133; i++) {
            Random rand = new Random()
            rand.nextInt(6);
            switch (rand.nextInt(6)){
                case 0: containedTiles.insert(new Tile(WHITE))
                    break;
                case 1: containedTiles.insert(new Tile(YELLOW))
                    break;
                case 2: containedTiles.insert(new Tile(PINK))
                    break;
                case 3: containedTiles.insert(new Tile(BLUE))
                    break;
                case 4: containedTiles.insert(new Tile(LIGHTBLUE))
                    break;
                case 5: containedTiles.insert(new Tile(GREEN))
                    break;
            }

        }

        throw new NotImplementedException;
    }
}
