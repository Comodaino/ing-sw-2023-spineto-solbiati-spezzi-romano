package Model;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * represent PersonalGoal with an ArrayList of type Pgtype
 *
 * @author alessandra
 */
public class PersonalGoal extends Goal implements Serializable {
    private Shelf playerShelf;
    private ArrayList<Pgtype> PGoal;
    private int numOfGoal;

    /**
     * Constructor of the PersonalGoal
     *
     * @param ps, Shelf of player
     */
    public PersonalGoal(Shelf ps) {

        this.playerShelf = ps;
        this.PGoal = new ArrayList<>();
    }

    /**
     * this method create a random Personal goal through the getPersGoal method
     */
    public int CreatePersonalGoal() {
        Random rand = new Random();

        int j = 0;
        switch (rand.nextInt(12)) {

            case 0:
                getPersGoal(1);
                j = 0;
                break;
            case 1:
                getPersGoal(7);
                j = 1;
                break;
            case 2:
                getPersGoal(13);
                j = 2;
                break;
            case 3:
                getPersGoal(19);
                j = 3;
                break;
            case 4:
                getPersGoal(25);
                j = 4;
                break;
            case 5:
                getPersGoal(31);
                j = 5;
                break;
            case 6:
                getPersGoal(37);
                j = 6;
                break;
            case 7:
                getPersGoal(43);
                j = 7;
                break;
            case 8:
                getPersGoal(49);
                j = 8;
                break;
            case 9:
                getPersGoal(55);
                j = 9;
                break;
            case 10:
                getPersGoal(61);
                j = 10;
                break;
            case 11:
                getPersGoal(67);
                j = 11;
                break;
        }
        this.numOfGoal = j;
        return j;
    }


    /**
     * this method calculate the score at the end of the game
     *
     * @param playerShelf
     * @return score
     */
    public int getScore(Shelf playerShelf) {
        int score = 0;
        int point = 0;
        for (int i = 0; i < PGoal.size(); i++) {
            Pgtype pgtype = PGoal.get(i);
            int line = pgtype.getLine();
            int col = pgtype.getCol();
            Color color = pgtype.getColor();
            if (playerShelf.getTile(line, col) != null && playerShelf.getTile(line, col).getColor() == color) {
                point += 1;
            }
        }
        if (point == 1) {
            score = 1;
        }
        if (point == 2) {
            score = 2;
        }
        if (point == 3) {
            score = 4;
        }
        if (point == 4) {
            score = 6;
        }
        if (point == 5) {
            score = 9;
        }
        if (point == 6) {
            score = 12;
        }

        return score;
    }

    /**
     * get a Personal Goal from the document PersonalGoal_conf
     *
     * @param j that represents a random number
     * @return PGoal, the Personal Goal
     */
    public ArrayList<Pgtype> getPersGoal(int j) {
        InputStream is = getClass().getClassLoader().getResourceAsStream("personalGoal_conf");
        assert is != null;
        Scanner reader = new Scanner(is);
        if (reader.hasNextLine()) {
            for (int k = 0; k < j - 1; k++) {
                String pg = reader.nextLine();
            }
            String pg = reader.nextLine();
            for (int l = 0; l < 6; l++) {
                String[] st = pg.split(",");
                int line = st[0].charAt(0) - 48;
                int col = st[1].charAt(0) - 48;
                Color color = Color.valueOf(st[2]);
                PGoal.add(new Pgtype(line, col, color));
                if (l != 5) {
                    pg = reader.nextLine();
                }
            }
        }
        return PGoal;
    }

    public ArrayList<Pgtype> getPGoal() {
        return PGoal;
    }

    @Override
    public int getScore(Player p) {
        playerShelf = p.getShelf();
        int score = 0, point = 0;
        for (int i = 0; i < PGoal.size(); i++) {
            Pgtype pgtype = PGoal.get(i);
            int line = pgtype.getLine();
            int col = pgtype.getCol();
            Color color = pgtype.getColor();
            if (playerShelf.getTile(line, col) != null && playerShelf.getTile(line, col).getColor() == color) {
                point += 1;
            }
        }
        if (point == 1) {
            score = 1;
        }
        if (point == 2) {
            score = 2;
        }
        if (point == 3) {
            score = 4;
        }
        if (point == 4) {
            score = 6;
        }
        if (point == 5) {
            score = 9;
        }
        if (point == 6) {
            score = 12;
        }

        return score;

    }

    public int getNumOfGoal() {
        return numOfGoal;
    }
}