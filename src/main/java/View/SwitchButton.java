package View;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

public class SwitchButton {
    Boolean firstSwitch;
    Button switchButton;
    int index;
    GUIApp parent;
    String command;
    String com;
    BooleanProperty isSelected2 = new SimpleBooleanProperty(false);
    String path;
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
    public Button getButton() {
        return switchButton;
    }

    public int getIndex() {
        return index;
    }
    public boolean getIsSelected2() {
        return isSelected2.get();
    }

    public void setGraphic(String path) {
        switchButton.setStyle("-fx-background-image: url('"+ path +"'); -fx-background-size: 40 40; -fx-background-repeat: no-repeat; -fx-background-position: center; ");
    //    switchButton.setGraphic(imageView);
    }

    public void setOpacity(int i) {
        switchButton.setOpacity(i);
    }

    public void setStyle(String s) {
        switchButton.setStyle(s);
    }

    public void setIsSelected2(boolean b) {
        isSelected2.set(b);
    }
}
