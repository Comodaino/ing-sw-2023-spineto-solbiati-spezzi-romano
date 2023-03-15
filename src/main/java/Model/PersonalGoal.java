package Model;

import java.util.ArrayList;
import java.util.Random;

public class PersonalGoal {
    private Shelf playerShelf;
    private ArrayList<Pgtype> PGoal;

    public PersonalGoal(Shelf ps){
        this.playerShelf= ps;
        this.PGoal= new ArrayList<Pgtype>();   //ArrayList o List??
        Random rand = new Random();
        rand.nextInt(12);

    }

    public int getScore(Shelf shelf){
        for(int i=1;i<=6;i++){
            if()
        }
    }

}
