package Model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BagTest {
     Bag testBag;
     BagTest(){
        this.testBag = new Bag();
    }

    @Test
     void test1(){
        for(int i=0; i<133; i++){
            Tile newTile = testBag.newTile();
            System.out.println(newTile.getColor().toString() + " " + newTile.getType().toString() + " " + i);
        }
        assertNull(testBag.newTile());
        System.out.println("TEST PASSED");
    }
}
