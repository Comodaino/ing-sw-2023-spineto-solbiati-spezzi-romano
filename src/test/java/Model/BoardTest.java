package Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNull;

class BoardTest {
    private List<Player> playerList;
    @BeforeEach
    public void reset(){
        playerList=null;
    }
    @Test
    public void testTwoPlayers() {
        playerList=new ArrayList<Player>();
        playerList.add(new Player("player1", true));
        playerList.add(new Player("player2", false));
        Board testBoard = new Board(false, playerList);
        for(int i=0; i<9; i++){
            for(int j=0; j<9; j++){
                System.out.print(testBoard.getCell(i,j).getType() + " ");
            }
            System.out.println();
        }
        testBoard.removeTile(5,5);
        assertNull(testBoard.getTile(5, 5));
        System.out.println("SEGMENT PASSED");
        for(int i=0; i<9; i++){
            for(int j=0; j<9; j++){
                if(!testBoard.getCell(i,j).isEmpty()) testBoard.removeTile(i,j);
            }
        }
        testBoard.checkRecharge();
        for(int i=0; i<9; i++){
            for(int j=0; j<9; j++){
                if(testBoard.getCell(i,j).isEmpty() && testBoard.getCell(i,j).getType()==CellType.TWO) {
                    System.out.println("RECHARGE FAIL");
                    return;
                }
            }
        }
        System.out.println("SEGMENT PASSED");
        for(int i=0; i<9; i++){
            for(int j=0; j<9; j++){
                if(!testBoard.getCell(i,j).isEmpty() && !((i==5 && j==6) || (i==5 && j==7))) testBoard.removeTile(i,j);
            }
        }

        System.out.println("TEST PASSED");
    }
    @Test
    public void testThreePlayers() {
        playerList=new ArrayList<Player>();
        playerList.add(new Player("player1", true));
        playerList.add(new Player("player2", false));
        playerList.add(new Player("player3", false));
        Board testBoard = new Board(false, playerList);
        for(int i=0; i<9; i++){
            for(int j=0; j<9; j++){
                System.out.print(testBoard.getCell(i,j).getType() + " ");
            }
            System.out.println();
        }
        testBoard.removeTile(5,5);
        assertNull(testBoard.getTile(5, 5));
        System.out.println("SEGMENT PASSED");
        for(int i=0; i<9; i++){
            for(int j=0; j<9; j++){
                if(!testBoard.getCell(i,j).isEmpty()) testBoard.removeTile(i,j);
            }
        }
        testBoard.checkRecharge();
        for(int i=0; i<9; i++){
            for(int j=0; j<9; j++){
                if(testBoard.getCell(i,j).isEmpty() && testBoard.getCell(i,j).getType()!=CellType.THREE) {
                    System.out.println("RECHARGE FAIL");
                    return;
                }
            }
        }
        System.out.println("SEGMENT PASSED");
        for(int i=0; i<9; i++){
            for(int j=0; j<9; j++){
                if(!testBoard.getCell(i,j).isEmpty() && !((i==5 && j==6) || (i==5 && j==7))) testBoard.removeTile(i,j);
            }
        }

        System.out.println("TEST PASSED");
    }
    @Test
    public void testFourPlayers() {
        playerList=new ArrayList<Player>();
        playerList.add(new Player("player1", true));
        playerList.add(new Player("player2", false));
        playerList.add(new Player("player3", false));
        playerList.add(new Player("player4", false));
        Board testBoard = new Board(false, playerList);
        for(int i=0; i<9; i++){
            for(int j=0; j<9; j++){
                System.out.print(testBoard.getCell(i,j).getType() + " ");
            }
            System.out.println();
        }
        testBoard.removeTile(5,5);
        assertNull(testBoard.getTile(5, 5));
        System.out.println("SEGMENT PASSED");
        for(int i=0; i<9; i++){
            for(int j=0; j<9; j++){
                if(!testBoard.getCell(i,j).isEmpty()) testBoard.removeTile(i,j);
            }
        }
        testBoard.checkRecharge();
        for(int i=0; i<9; i++){
            for(int j=0; j<9; j++){
                if(testBoard.getCell(i,j).isEmpty() && (testBoard.getCell(i,j).getType()==CellType.TWO || testBoard.getCell(i,j).getType()==CellType.THREE)) {
                    System.out.println("RECHARGE FAIL");
                    return;
                }
            }
        }
        System.out.println("SEGMENT PASSED");
        for(int i=0; i<9; i++){
            for(int j=0; j<9; j++){
                if(!testBoard.getCell(i,j).isEmpty() && !((i==5 && j==6) || (i==5 && j==7))) testBoard.removeTile(i,j);
            }
        }

        System.out.println("TEST PASSED");
    }

}