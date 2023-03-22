package Model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ShelfTest {

    private Shelf testShelf;
    public ShelfTest(){
        this.testShelf = new Shelf();
    }
    public void setShelf(Tile[][] m){

          this.testShelf.setMatrix(m);
    }
    @Test
    public void testAddInEmptyColumn(){

        ShelfTest t = new ShelfTest();
        Tile tile = new Tile(Color.WHITE);
        assertTrue(t.testShelf.addTile(0,tile));
        System.out.println("Test Passed");
    }
    @Test
    public void testAddInFullColumn(){
        Tile[][] tMatrix = new Tile[6][5];
        Tile tile = new Tile(Color.WHITE);
        tMatrix[0][0] = tile;
        tMatrix[1][0] = tile;
        tMatrix[2][0] = tile;
        tMatrix[3][0] = tile;
        tMatrix[4][0] = tile;
        tMatrix[5][0] = tile;
        ShelfTest t = new ShelfTest();
        t.testShelf.setShelf(tMatrix);
        assertFalse(t.testShelf.addTile(0, tile));
        System.out.println("Test Passed");
    }
    @Test
    public void testGetNonExistingTile(){

        ShelfTest t = new ShelfTest();
        Tile result = t.testShelf.getTile(0,0);
        assertEquals(result, null);
        System.out.println("Test Passed");

    }

    @Test
    public void testGetExistingTile(){

        ShelfTest t= new ShelfTest();
        Tile tile = new Tile(Color.WHITE);
        t.testShelf.addTile(0, tile);
        Tile result = t.testShelf.getTile(0,0);
        assertEquals(result, tile);
        System.out.println("Test Passed");
    }



}
