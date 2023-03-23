package Model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CellTest {

    @Test
    void TestIsEmpty(){
        Tile contTile = new Tile(Color.BLUE);
        Cell cell = new Cell(CellType.ONE);

        Assertions.assertEquals(true, cell.isEmpty());
        cell.insertTile(contTile);
        Assertions.assertEquals(false, cell.isEmpty());
        cell.removeTile();
        Assertions.assertEquals(true, cell.isEmpty());
        System.out.println("TEST PASSED");
    }

    @Test
    void TestGetType(){
        Tile tileTest = new Tile(Color.LIGHTBLUE);
        Cell cellTest = new Cell(CellType.THREE);
        Assertions.assertEquals(true,cellTest.isEmpty());
        cellTest.insertTile(tileTest);
        String Test = String.valueOf(cellTest.getType());
        Assertions.assertEquals("THREE",Test);
        System.out.println("TEST PASSED");

    }
    @Test
    void TestGetTile(){
        Tile contTile = new Tile(Color.BLUE);
        Cell cell = new Cell(CellType.ONE);
        Assertions.assertNull(cell.getTile());
        cell.insertTile(contTile);
        Assertions.assertEquals(contTile,cell.getTile());
        System.out.println("TEST PASSED");
    }

}