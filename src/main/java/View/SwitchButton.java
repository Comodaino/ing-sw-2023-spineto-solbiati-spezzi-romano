package View;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Button;

/**
 * The SwitchButton class represents a button that is used to change the order of tiles in the buffer.
 * @author alessandra
 */
public class SwitchButton {
    Boolean firstSwitch;
    Button switchButton;
    int index;
    GUIApp parent;
    String command;
    String com;
    BooleanProperty isSelected2 = new SimpleBooleanProperty(false);
    String path;
    /**
     * Constructs a new SwitchButton object.
     *
     * @param parent  The parent GUIApp instance.
     * @param index   The index of the button.
     * @param command The initial command value
     * @param path    The path to the image for the button.
     */
    public SwitchButton(GUIApp parent, int index, String command, String path ){
        this.index = index;
        this.parent = parent;
        this.firstSwitch = firstSwitch;
        this.command = command;
        this.com = com;
        this.isSelected2 = isSelected2;
        this.path = path;
        switchButton = new Button();
        switchButton.setPrefSize(40,40);
        switchButton.setVisible(true);


        switchButton.setOnMouseClicked(e -> {
            if(isSelected2.get()) {
                if(this.parent.getFirstSwitch()){
                    this.parent.setFirstSwitch(false);

                    com = "/switch";

                }else com = parent.getCommand();
                isSelected2.set(false);
                switchButton.setStyle("-fx-background-image: url('"+path+"');-fx-border-color: blue; -fx-border-width: 2px;");

                switchButton.setDisable(true);
                switchButton.setOpacity(0.8);
                this.command =  com + " " + index;
                this.parent.setCommand(this.command);

            }else{
                isSelected2.set(true);
                switchButton.setDisable(false);
                switchButton.setStyle("");
                switchButton.setOpacity(1);
            }
        });
    }

    /**
     * Returns the switch button.
     *
     * @return The switch button.
     */
    public Button getButton() {
        return switchButton;
    }


    /**
     * Returns the index of the button.
     *
     * @return The index of the button.
     */
    public int getIndex() {
        return index;
    }
    /**
     * Returns the selected state of the button.
     *
     * @return The selected state of the button.
     */
    public boolean getIsSelected2() {
        return isSelected2.get();
    }
    /**
     * Sets the graphic of the switch button.
     *
     * @param path The path to the image for the button.
     */
    public void setGraphic(String path) {
        switchButton.setStyle("-fx-background-image: url('"+ path +"'); -fx-background-size: 40 40; -fx-background-repeat: no-repeat; -fx-background-position: center; ");
    }

    /**
     * Sets the opacity of the switch button.
     *
     * @param i opacity value to set. The value should be in the range [0.0, 1.0],
     */

    public void setOpacity(int i) {
        switchButton.setOpacity(i);
    }

    /**
     * Sets the style of the switch button.
     *
     * @param s The CSS style string to set for the button.
     */

    public void setStyle(String s) {
        switchButton.setStyle(s);
    }

    /**
     * Sets the selected state of the button.
     *
     * @param b The boolean value representing the selected state of the button.
     *
     */
    public void setIsSelected2(boolean b) {
        isSelected2.set(b);
    }
}
