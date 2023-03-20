package Model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BagTest {
    private Bag testBag;
    public BagTest(){
        this.testBag = new Bag();
    }

    @Test
    public void test1(){
        for(int i=0; i<133; i++){
            System.out.println((String)testBag.newTile().getColor());
        }
        assertEquals(testBag.newTile(), null);
        System.out.println("TEST PASSED");
    }
}
}