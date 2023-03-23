package Controller;

import Model.Player;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class GameControllerTest {
    @Test
    public void inputTest(){
        List<Player> playerList=new ArrayList<Player>();
        playerList.add(new Player("player1", true));
        playerList.add(new Player("player2", false));
    }
}