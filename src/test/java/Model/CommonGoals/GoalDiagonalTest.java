package Model.CommonGoals;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class GoalDiagonalTest {

    @Test
    void emptyShelf(){}

    void jhj(){
        File shelfConf = new File(".json");
        Scanner reader = new Scanner(shelfConf);
        String confBuffer = reader.nextLine();
    }
}