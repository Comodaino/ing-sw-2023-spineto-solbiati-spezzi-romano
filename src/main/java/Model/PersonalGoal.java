package Model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class PersonalGoal extends Goal{
    private Shelf playerShelf;
    private ArrayList<Pgtype> PGoal;

    public PersonalGoal(Shelf ps){

        this.playerShelf= ps;
        this.PGoal= new ArrayList<>();   //ArrayList o List??
        Random rand = new Random();
        rand.nextInt(12);

        switch (rand.nextInt(12)) {

            case 0:  getPersGoal(0);
                break;
            case 1:  getPersGoal(7);
                break;
            case 2:  getPersGoal(13);
                break;
            case 3:  getPersGoal(19);
                break;
            case 4:  getPersGoal(25);
                break;
            case 5:  getPersGoal(31);
                break;
            case 6:  getPersGoal(37);
                break;
            case 7:  getPersGoal(43);
                break;
            case 8:  getPersGoal(49);
                break;
            case 9:  getPersGoal(55);
                break;
            case 10:  getPersGoal(61);
                break;
            case 11:  getPersGoal(67);
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
        try {
            File ListOfPersonalGoal = new File("src/main/java/Model/Conf/PersonalGoal.json");
            Scanner reader = new Scanner(ListOfPersonalGoal);
            if(reader.hasNextLine()){
                for(int k=0;k<j-1;k++)  {
                    String pg= reader.nextLine();
                }
                String pg = reader.nextLine();
                for(int l=0; l<6; l++) {
                    String st[] = pg.split(",");
                        int line = st[0].charAt(0)-48;
                        int col = st[1].charAt(0)-48;
                        Color color = Color.valueOf(st[2]);
                        PGoal.add(new Pgtype(line,col,color));
                            if(l!=5) {
                                pg = reader.nextLine();
                            }
                }
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
    @Override
    public int getScore(Player p) {
        playerShelf =p.getShelf();
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
}