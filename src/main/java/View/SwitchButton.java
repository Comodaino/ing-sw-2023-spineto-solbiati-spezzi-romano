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
    public SwitchButton(GUIApp parent, int index, String command ){
        this.index = index;
        this.parent = parent;
        this.firstSwitch = firstSwitch;
        this.command = command;
        this.com = com;
        switchButton = new Button();
        BooleanProperty isSelected2 = new SimpleBooleanProperty(false);
        switchButton.setPrefSize(40,40);
        switchButton.setVisible(true);

        switchButton.setOnMouseClicked(e -> {
            if(isSelected2.get()) {
                if(this.parent.getFirstSwitch()){
                    this.parent.setFirstSwitch(false);

                    com = "/switch";

                }else com = parent.getCommand();
                isSelected2.set(false);
                switchButton.setStyle("-fx-border-color: blue; -fx-border-width: 2px;");
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

    public void setGraphic(ImageView imageView) {
        switchButton.setGraphic(imageView);
    }

}
