package View;

import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import javax.swing.*;
import javax.swing.text.Element;
import javax.swing.text.html.ImageView;
import java.awt.*;

public class BoardPanel extends StackPane {
    private ImageView imageView;

    public BoardPanel() {
        super();
        getStyleClass().add("board-panel");
        setOpaqueInsets(new Insets(0));

        Image imageBoard = new Image("images/boards/livingroom.png");
        BackgroundImage backgroundImage = new BackgroundImage(imageBoard, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);


    }

}
