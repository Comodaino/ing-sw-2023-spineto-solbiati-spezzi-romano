package Model;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class Shelf{
    private Tile matrix[6][5];

    public Shelf Shelf(){


        throw new NotImplementedException;
    }

    public void addTile(int c, Tile t){

        matrix[c] = t;
        throw new NotImplementedException;
    }

    public Tile getTile(int r, int c){

        return matrix[r][c];
        throw new NotImplementedException;
    }
}

