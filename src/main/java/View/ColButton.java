package View;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;


public class ColButton extends Button {
    Button button;
    int index;
    GUIApp parent;

public ColButton(GUIApp parent, int index){
    this.index = index;
    this.parent = parent;
    button = new Button();
    button.setStyle("-fx-background-color: green;");

    button.setPrefSize(45,20);
    button.setVisible(true);
    BooleanProperty isSelected = new SimpleBooleanProperty(false);

    //TODO add botton
    this.button.setOnMouseClicked (e -> {
        if(isSelected.get()){
            isSelected.set(false);
        //    button.setStyle("-fx-background-color: Red;");
            this.parent.setCommand("/add " + index);
            this.parent.setRemove(true);
        }
        else{
            isSelected.set(true);

        }

    });
}

    public Button getButton() {
        return button;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public Node getStyleableNode() {
        return super.getStyleableNode();
    }
}