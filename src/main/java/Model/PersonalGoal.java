package Model;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import static Model.Color.WHITE;

public class PersonalGoal {
    private Shelf playerShelf;
    private ArrayList<Pgtype> PGoal;

    public PersonalGoal(Shelf ps){

        this.playerShelf= ps;
        this.PGoal= new ArrayList<Pgtype>();   //ArrayList o List??
        Random rand = new Random();
        rand.nextInt(12);

        switch (rand.nextInt(12)) {

            case 0:  getPersGoal(0);
                break;
            case 1:  getPersGoal(6);
                break;
            case 2:  getPersGoal(12);
                break;
            case 3:  getPersGoal(18);
                break;
            case 4:  getPersGoal(24);
                break;
            case 5:  getPersGoal(30);
                break;
            case 6:  getPersGoal(36);
                break;
            case 7:  getPersGoal(42);
                break;
            case 8:  getPersGoal(48);
                break;
            case 9:  getPersGoal(54);
                break;
            case 10:  getPersGoal(60);
                break;
            case 11:  getPersGoal(66);
                break;
        }
    }

    public int getScore(Shelf shelf){
        for(int i=1;i<=6;i++){
        }
    }
    public void getPersGoal(int j) {
        try {
            File PersonalGoal = new File("PersonalGoal.json");
            Scanner reader = new Scanner(PersonalGoal);
            String Pers = reader.nextLine();
            for(int line=j; j<=j+6; j++) {

                PGoal.add(new Pgtype((int) Pers.charAt(0), (int) Pers.charAt(1), (Color)Pers.lines()));  //non so cosa fare per leggere il colore
            }
        }catch (FileNotFoundException e);
    }

}






