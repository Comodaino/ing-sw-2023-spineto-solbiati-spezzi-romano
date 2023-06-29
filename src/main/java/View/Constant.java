package View;

public class Constant {
    private String persGoal;
    private String goal;
    private String tile;
    private String display3;
    private String display5;
    /**
     * Constructs a new Constant object and initializes the paths for different resources.
     * @author Alessandra Romano
     */
    public Constant(){

        this.persGoal = "images/personal%20goal%20cards/Personal_Goals";
        this.goal = "images/common%20goal%20cards/";
        this.tile = "images/item%20tiles/";
        this.display3 = "images/Publisher%20material/Display_3.jpg";
        this.display5 = "images/Publisher%20material/Display_5.jpg";
    }
    /**
     * Returns the constant path for personal goal cards.
     *
     * @return The constant path for personal goal cards.
     */
    public String getCostantPersGoal(){
        return persGoal;
    }
    /**
     * Returns the constant path for common goal cards.
     *
     * @return The constant path for common goal cards.
     */
    public String getConstantGoal(){
        return goal;
    }
    /**
     * Returns the constant path for item tiles.
     *
     * @return The constant path for item tiles.
     */

    public String getConstantTile(){
        return tile;
    }

    /**
     * Returns the constant path for Display_3 image.
     *
     * @return The constant path for Display_3 image.
     */
    public String getDisplay3() {
        return display3;
    }
    /**
     * Returns the constant path for Display_5 image.
     *
     * @return The constant path for Display_5 image.
     */
    public String getDisplay5() {
        return display5;
    }
}
