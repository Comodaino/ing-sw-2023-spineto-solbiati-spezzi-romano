package View;

import javafx.scene.Node;
import javafx.scene.control.Button;
import java.awt.*;

import Distributed.AbstractClient;
import Model.CellType;
import Model.Player;
import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.RemoteException;

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