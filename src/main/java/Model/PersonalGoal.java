package Model;
import java.io.File;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.StringTokenizer;

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
      /*  int score=0;
        EndGoal endGoal=null;
        if(endGoal.getStatus()==false){
            for (int r=0;r<6;r++){
                for(int c=0;c<5;c++){
                    playerShelf[r][c]=;
                }
            }
        }
        return score;*/
    }
    public void getPersGoal(int j) {
        String fileName = "PersonalGoal.json";
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))){
            String file = br.readLine();

            for(int l=j; l<=j+6; l++) {
                StringTokenizer st = new StringTokenizer(file,",");
                while (st.hasMoreTokens()){
                    String line = st.nextToken();
                    String col = st.nextToken();
                    Color color = Color.valueOf(st.nextToken());
                    PGoal.add(new Pgtype(Integer.parseInt(line),Integer.parseInt(col),color));
                }

            }
        }catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ;
    }

}






