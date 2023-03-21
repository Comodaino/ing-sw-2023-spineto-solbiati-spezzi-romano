package Model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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

    public int getScore(Shelf playerShelf){
        int score=0,point=0;
            for (int i=0;i<PGoal.size();i++) {
                Pgtype pgtype = PGoal.get(i);
                int line = pgtype.getLine();
                int col = pgtype.getCol();
                Color color = pgtype.getColor();
                if (playerShelf.getTile(line, col)!=null && playerShelf.getTile(line, col).getColor() == color) {
                    point+=1;
                }
            }
            if (point==1){
                score=1;
            }
            if(point==2){
                score=2;
            }
            if(point==3){
                score=4;
            }
            if(point==4){
                score=6;
            }
            if(point==5){
                score=9;
            }
            if(point==6){
                score=12;
            }

        return score;
    }
    public ArrayList<Pgtype> getPersGoal(int j) {
        String fileName = "src/main/java/Model/Conf/PersonalGoal.json";
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))){

            for(int k=0;k<j-1;k++)  {
                br.readLine();
            }
            String file = br.readLine();
            for(int l=0; l<6; l++) {
                String st[] = file.split(",");

                    int line = st[0].charAt(0)-48;
                    int col = st[1].charAt(0)-48;
                    Color color = Color.valueOf(st[2]);
                    PGoal.add(new Pgtype(line,col,color));

            }
        }catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return PGoal;
    }

    public ArrayList<Pgtype> getPGoal() {
        return PGoal;
    }
}






