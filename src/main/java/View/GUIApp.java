package View;


import Distributed.AbstractClient;

import Model.CommonGoals.*;
import Model.Player;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
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
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

public class GUIApp extends Application implements ViewInterface {
    private boolean firstLaunch;
    private static AbstractClient client;
    private static State state;
    private static Stage primaryStage;
    private Boolean firstRemove;

    private String command;
    private boolean firstSwitch;


    public GUIApp() {
        this.firstLaunch = true;
        this.client = PassParameters.getClient();
        this.state = PassParameters.getState();
        firstRemove = true;
    }

    public void start(Stage primaryStage) throws RemoteException {


        this.primaryStage = primaryStage;
        try {
            update(null);
        } catch (IOException e) {
        }

        primaryStage.show();

    }


    public void play(AbstractClient client, Stage primaryStage) throws RemoteException {
        GridPane mainPane = new GridPane();
        Scene scene = new Scene(mainPane);
        primaryStage.setScene(scene);

        Image imageBackgroundShelf = new Image("images/misc/sfondoparquet.jpg");
        ImageView imageViewShelf = new ImageView(imageBackgroundShelf);
        BoxBlur blur = new BoxBlur(3, 3, 3);
        imageViewShelf.setEffect(blur);

        imageViewShelf.setPreserveRatio(true);
        imageViewShelf.fitWidthProperty().bind(mainPane.widthProperty());
        imageViewShelf.fitHeightProperty().bind(mainPane.heightProperty());
        mainPane.setBackground(new Background(new BackgroundImage(imageBackgroundShelf, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));

        GridPane commonGoal = getCommonGoal(client);
        mainPane.add(commonGoal, 0, 1);

        mainPane.add(createShelf(client, mainPane), 1, 0);
        mainPane.add(createBoard(client), 0, 0);

        primaryStage.setTitle("Play");
        primaryStage.setOnCloseRequest(e -> {
            // Gestire l'evento di chiusura
        });
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setX(bounds.getMinX());
        primaryStage.setY(bounds.getMinY());
        primaryStage.setWidth(bounds.getWidth());
        primaryStage.setHeight(bounds.getHeight());


        HBox hBox = new HBox();
        mainPane.add(hBox, 0, 2);


        primaryStage.sizeToScene();
        primaryStage.show();
    }

    public Pane createBoard(AbstractClient client) {
        Pane boardPane = new Pane();

        Image imageBoard = new Image("images/boards/livingroom.png");
        ImageView imageViewBoard = new ImageView(imageBoard);

        imageViewBoard.setPreserveRatio(true);
        imageViewBoard.setFitHeight(650);
        imageViewBoard.setFitWidth(650);

        boardPane.getChildren().addAll(imageViewBoard);
        GridPane fillBoardGridPane = fillBoard(client);
        fillBoardGridPane.setPrefSize(650, 650);
        fillBoardGridPane.setAlignment(Pos.CENTER);
        boardPane.getChildren().add(fillBoardGridPane);

        return boardPane;
    }

    public Button resetCommand() {

        Button reset = new Button("Reset");
        reset.setPrefSize(100, 60);
        reset.setStyle("");
        reset.setText("Execute move");
        reset.setOnMouseClicked(e -> {
            if(command!=null) client.println(command);
            command= null;
        });
        return reset;
    }
    public Button cancel(){
        Button reset = new Button("cancel");
        reset.setPrefSize(100, 60);
        reset.setStyle("");
        reset.setText("Cancel move");
        reset.setOnMouseClicked(e -> {
            command= null;
        });
        return reset;
    }
    public Button endGame(){
        Button reset = new Button("end");
        reset.setPrefSize(100, 60);
        reset.setStyle("");
        reset.setText("DEBUG END GAME");
        reset.setOnMouseClicked(e -> {
            client.println("/end");
        });
        return reset;

    }
    public GridPane fillBoard(AbstractClient client){
        GridPane fillBoardPane = new GridPane();

        //TODO add control
     //   Pane emptyComponent = new Pane();
  //      emptyComponent.setPrefSize(65, 65);
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                Pane emptyPane = new Pane();
                fillBoardPane.add(emptyPane, i, j);
            }
        }
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (!client.getBoardView().getCell(i, j).isEmpty()) {
                    fillBoardPane.add(getTile(i,j), j, i);
                    mouseClicked(getTile(i,j),fillBoardPane);
                }
            }
        }

        return fillBoardPane;
    }

    public Button getTile(int row, int column) {
        Button tileButton = new Button();
        String imageTilePath;
        Constant tile = new Constant();
        int i = 0;
        String imageTileName = null;
        switch (client.getBoardView().getTile(row, column).getColor()) {
            case BLUE:
                imageTileName = "Cornici1.";
                break;
            case GREEN:
                imageTileName = "Gatti1.";
                break;
            case YELLOW:
                imageTileName = "Giochi1.";
                break;
            case WHITE:
                imageTileName = "Libri1.";
                break;
            case PINK:
                imageTileName = "Piante1.";
                break;
            case LIGHTBLUE:
                imageTileName = "Trofei1.";
                break;

        }
        switch (client.getBoardView().getTile(row, column).getType()) {
            case ONE:
                i = 1;
                break;
            case TWO:
                i = 2;
                break;
            case THREE:
                i = 3;
                break;
        }
        imageTilePath = tile.getConstantTile() + imageTileName + i + ".png";

        tileButton.setPrefSize(54, 54);
        tileButton.setStyle(" -fx-border-width: 0; -fx-border-height: 0 ");
        Image imageTile = new Image(imageTilePath);
        ImageView imageView = new ImageView(imageTile);
        imageView.setPreserveRatio(true);
        imageView.setFitHeight(54);
        imageView.setFitWidth(54);
        tileButton.setGraphic(imageView);


        GaussianBlur blur = new GaussianBlur(2);
        BooleanProperty isSelected = new SimpleBooleanProperty(false);
        AtomicInteger count = new AtomicInteger(0);
        if(count.get() < 3) {
            tileButton.setOnMouseClicked(e -> {
                try {
                    if(!client.getBoardView().getCurrentPlayer().getNickname().equals(client.getNickname())) return;
                    if(!client.getBoardView().getCurrentPlayer().getNickname().equals(client.getNickname())) return;

                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
                count.incrementAndGet();
                System.out.println("Problem: " + firstRemove);
                if (firstRemove) {
                    firstRemove = false;
                    command = "/remove ";
                }
                if(command!=null && command.startsWith("/remove")) {
                    if (isSelected.get() && count.get() < 3) {
                        isSelected.set(false);
                        tileButton.setStyle("-fx-border-color: yellow; -fx-border-width: 2;");
                        tileButton.setEffect(blur);
                        tileButton.setDisable(true);
                        tileButton.setOpacity(0.8);
                        firstRemove = false;
                        command = command + row + " " + column + " ";
                        System.out.println(command);
                    } else {
                        isSelected.set(true);
                        tileButton.setStyle("-fx-border-width: 0; -fx-border-height: 0");
                        tileButton.setEffect(null);
                        tileButton.setDisable(false);
                        tileButton.setOpacity(1);
                    }
                }
        });
        }

        return tileButton;
    }

    void mouseClicked (Button tile, GridPane fillBoardPane){

    }

    /**
     * This method is used to create the common goal
     *
     * @param client is the client
     * @return the common goal
     */

    public GridPane getCommonGoal(AbstractClient client) {
        GridPane commonGoalPane = new GridPane();

        int size = client.getBoardView().getListOfPlayer().size();
        int photo =0;
        Iterator cmIterator = client.getBoardView().getSetOfCommonGoal().iterator();
        for (int j = 0; j < client.getBoardView().getSetOfCommonGoal().size(); j++) {
            Constant commonGoal = new Constant();
            CommonGoal cg = (CommonGoal) cmIterator.next();

            switch (cg.getName()) {
                case "GoalAngles":
                    new GoalAngles(size);
                    photo=8;
                    break;
                case "GoalColumn":
                    new GoalColumn(size);
                    photo=5;
                    break;
                case "GoalCouples":
                    new GoalCouples(size);
                    photo=4;
                    break;
                case "GoalCross":
                    new GoalCross(size);
                    photo=10;
                    break;
                case "GoalDiagonal":
                    new GoalDiagonal(size);
                    photo=11;
                    break;
                case "GoalDiffColumns":
                    new GoalDiffColumns(size);
                    photo=2;
                    break;
                case "GoalDiffRows":
                     new GoalDiffRows(size);
                    photo=6;
                    break;
                case "GoalEight":
                    new GoalEight(size);
                    photo=9;
                    break;
                case "GoalQuartets":
                    new GoalQuartets(size);
                    photo=3;
                    break;
                case "GoalRow":
                    new GoalRow(size);
                    photo=7;
                    break;
                case "GoalSquares":
                    new GoalSquares(size);
                    photo=1;
                    break;
                case "GoalStair":
                    new GoalStair(size);
                    photo=12;
                    break;
            }
            if (j == 0) {
                System.out.println("PATH: " + commonGoal.getConstantGoal() + photo + ".jpg");
                System.out.println("PATH: " + commonGoal.getConstantGoal() + photo + ".jpg");
                Image imageGoal1 = new Image(commonGoal.getConstantGoal() + photo + ".jpg");

                ImageView imageView1 = new ImageView(imageGoal1);
                imageView1.setPreserveRatio(true);
                imageView1.setFitHeight(150);
                imageView1.setFitWidth(150);
                commonGoalPane.add(imageView1, 0, 0);
            } else {
                Image imageGoal2 = new Image(commonGoal.getConstantGoal() + photo + ".jpg");
                ImageView imageView2 = new ImageView(imageGoal2);
                imageView2.setPreserveRatio(true);
                imageView2.setFitHeight(150);
                imageView2.setFitWidth(150);
                commonGoalPane.add(imageView2, 1, 0);
            }
        }
        commonGoalPane.add(resetCommand(), 2, 0);
        commonGoalPane.add(cancel(),3,0);
        commonGoalPane.add(endGame(), 4,0);
        return commonGoalPane;
    }

    public GridPane createShelf(AbstractClient client, GridPane mainPane) throws RemoteException {
        GridPane shelfPane = new GridPane();
        GridPane shelfGridPane = new GridPane();
        Image imageShelf = new Image("images/boards/bookshelf.png");
        ImageView shelfImageView = new ImageView(imageShelf);
        shelfImageView.setFitWidth(300);
        shelfImageView.setFitHeight(300);
        shelfPane.add(shelfImageView, 0, 0);
        shelfPane.add(shelfGridPane, 0, 0);
        shelfPane.add(chat(), 1,2 );

        shelfPane.add(showOtherShelf(client), 1, 0);
        shelfImageView.setPreserveRatio(true);
        int nPlayer = 0;
        for (Player p: client.getBoardView().getListOfPlayer()) {

            if (p.getNickname().equals(client.getNickname())) {
                break;
            }else{
                nPlayer ++;
            }
        }
        for (int row = 0; row <6; row++) {
            for (int col = 0; col <5 ; col++) {
                if(client.getBoardView().getListOfPlayer().get(nPlayer).getShelf().getTile(row,col) != null) {
                        shelfGridPane.add(printShelf(nPlayer, row, col), col, 5-row);
                    } else {
                    Label label = new Label();
                    label.setStyle("-fx-background-color: transparent");
                    label.setPrefSize(40,40);
                    shelfGridPane.add(label, col, row);
                }

                }

        }

        for (int i = 0; i < 5; i++) {
            ColButton colButton = new ColButton(this, i);
            shelfGridPane.add(colButton.getButton(), i, 6);
        }


        shelfGridPane.setAlignment(Pos.CENTER);
        GridPane persGoal = new GridPane();

        String chairNick = null;
        for(Player p: client.getBoardView().getListOfPlayer()){
            if(p.getChair()){
                chairNick = p.getNickname();
                break;
            }
        }

        if (client.getNickname().equals(chairNick)){
            Image chairImage = new Image("images/misc/firstplayertoken.png");
            ImageView chairImageView = new ImageView(chairImage);
            chairImageView.setFitWidth(100);
            chairImageView.setFitHeight(100);
            chairImageView.setPreserveRatio(true);
            persGoal.add(chairImageView, 0, 0);
            //    shelfPane.add(chairImageView, 0, 1);
        } else {
            Label label = new Label();
            label.setPrefSize(100, 100);
            label.setStyle("-fx-background-color: transparent;");
            persGoal.add(label, 0, 0);
        }


        Image personalGoalImage = createPersonalGoal(client);
        ImageView personalGoalImageView = new ImageView(personalGoalImage);
        personalGoalImageView.setFitWidth(150);
        personalGoalImageView.setFitHeight(150);
        personalGoalImageView.setPreserveRatio(true);
        //    shelfPane.add(personalGoalImageView, 1, 1);
        persGoal.add(personalGoalImageView, 1, 0);

        persGoal.setAlignment(Pos.CENTER);

        shelfPane.add(persGoal, 0, 2);
        if(client.getBoardView().getCurrentPlayer().getNickname().equals(client.getNickname())) {
            Label label = new Label();
            label.setText("Choose the order of the tiles");
            label.setStyle("-fx-background-color: white;");
            shelfPane.add(label, 0, 3);
            GridPane bufferPane = new GridPane();
           for(int i =0; i<client.getBoardView().getTileBuffer().size();i++) {
               bufferPane.add(bufferTile(i), i, 4);
           }
            shelfPane.add(bufferPane, 0, 4);
        }

        return shelfPane;
    }

    public ImageView printShelf(int nPlayer, int row, int col){
        String imageTilePath;
        Constant tile = new Constant();
        ImageView imageView = new ImageView();
        int i = 0;
        String imageTileName = null;
            switch (client.getBoardView().getListOfPlayer().get(nPlayer).getShelf().getTile(row, col).getColor()) {
                case BLUE:
                    imageTileName = "Cornici1.";
                    break;
                case GREEN:
                    imageTileName = "Gatti1.";
                    break;
                case YELLOW:
                    imageTileName = "Giochi1.";
                    break;
                case WHITE:
                    imageTileName = "Libri1.";
                    break;
                case PINK:
                    imageTileName = "Piante1.";
                    break;
                case LIGHTBLUE:
                    imageTileName = "Trofei1.";
                    break;

            }
            switch (client.getBoardView().getListOfPlayer().get(nPlayer).getShelf().getTile(row, col).getType()) {
                case ONE:
                    i = 1;
                    break;
                case TWO:
                    i = 2;
                    break;
                case THREE:
                    i = 3;
                    break;
            }
            imageTilePath = tile.getConstantTile() + imageTileName + i + ".png";

            Image imageTile = new Image(imageTilePath);
            imageView.setImage(imageTile);
            imageView.setPreserveRatio(true);
            imageView.setFitHeight(40);
            imageView.setFitWidth(40);

        return imageView;
    }
    public Pane chat(){
        Pane chatPane = new Pane();

        VBox chatBox = new VBox();
        chatBox.setPrefSize(200, 200);
        TextField chatField = new TextField();
        VBox chatBox2 = new VBox();
        for(int i=0; i<client.getBoardView().getChatBuffer().size() ;i++) {
            if( client.getBoardView().getChatBuffer().size()==0 || client.getBoardView().getChatBuffer().get(i)==null)
                break;
            Label chatLabel = new Label();
            chatLabel.setText(client.getBoardView().getChatBuffer().get(i));
          //  chatBox.getChildren().add(chatLabel);

            chatBox2.getChildren().add(chatLabel);
            chatLabel.setStyle("-fx-background-color: white;");
            chatBox2.setStyle("-fx-background-color: white;");
        }
        chatField.setPromptText("Enter your message");

        chatField.setOnAction(e -> {
            client.println(chatField.getText());
            chatField.clear();
        });

        VBox chatBox3 = new VBox();
        Label chatLabel = null;
        for(int i=0; i<client.getBoardView().getPersonalChatBuffer().size() ;i++) {
            if (client.getBoardView().getPersonalChatBuffer().size() == 0 || client.getBoardView().getPersonalChatBuffer().get(i) == null)
                break;

            try {
                if (client.getBoardView().getPersonalChatBuffer().get(i).getRecipient().equals(client.getNickname())) {
                    chatLabel = new Label();
                    chatLabel.setText(client.getBoardView().getPersonalChatBuffer().get(i).getContent());
                }

            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
            //  chatBox.getChildren().add(chatLabel);
            if (chatLabel != null) {
                chatBox2.getChildren().add(chatLabel);
                chatLabel.setStyle("-fx-background-color: white;");
                chatBox2.setStyle("-fx-background-color: white;");
            }
        }
        chatBox.getChildren().addAll(chatField,chatBox2);
        chatPane.getChildren().addAll(chatBox3);
        chatPane.getChildren().addAll(chatBox);
        return chatPane;
    }

    public Button bufferTile(int i) {
//TODO add control
        BooleanProperty isSelected2 = new SimpleBooleanProperty(false);
            if(client.getBoardView().getTileBuffer().get(i)==null)
                return null;
            Button button = new Button();
            button.setPrefSize(40, 40);
            button.setDisable(true);
            Image image = new Image(createTile(i));
            ImageView imageView = new ImageView(image);
            imageView.setPreserveRatio(true);
            imageView.setOpacity(1);
            imageView.setFitHeight(40);
            imageView.setFitWidth(40);
            button.setGraphic(imageView);
            button.setOpacity(1);
            if(firstSwitch){
                firstSwitch=false;
                command="/switch";
            }

       //     int finalI = i;
            button.setOnAction(e -> {
                if(isSelected2.get()) {
                    isSelected2.set(false);
                    button.setStyle("-fx-border-color: blue; -fx-border-width: 2px;");
                    button.setDisable(true);
                    button.setOpacity(0.8);
                    command= command + " " + i;
                    System.out.println(command);

                }else{
                    isSelected2.set(true);
                    button.setDisable(false);
                    button.setStyle("");
                    button.setOpacity(1);

            }
        });
        return button;
    }

    public VBox showOtherShelf(AbstractClient client) throws RemoteException {

        VBox otherShelf = new VBox();
        GridPane shelfPlayer2 = new GridPane();
        GridPane shelfPlayer3 = new GridPane();
        GridPane shelfPlayer4 = new GridPane();
        Image imageShelf = new Image("images/boards/bookshelf.png");
        ImageView shelfImageView = new ImageView(imageShelf);
        shelfImageView.setFitWidth(250);
        shelfImageView.setFitHeight(250);
        shelfImageView.setPreserveRatio(true);
        GridPane shelf2 = new GridPane();
        GridPane shelf3 = new GridPane();
        GridPane shelf4 = new GridPane();
        shelf2.setAlignment(Pos.CENTER);
        shelf3.setAlignment(Pos.CENTER);
        shelf4.setAlignment(Pos.CENTER);
        int j=0;
        for(int i= 0; i < client.getBoardView().getListOfPlayer().size(); i++){
            if(!client.getBoardView().getListOfPlayer().get(i).getNickname().equals(client.getNickname())){
                for (int row=0; row<6; row++){
                    for(int col=0; col<5; col++){
                        switch(j){
                            case 0:
                                shelf2.add(printOtherShelf(row, col,i), col, 5-row);
                                break;
                            case 1:
                                shelf3.add(printOtherShelf(row, col,i), col, 5-row);
                                break;
                            case 2:
                                shelf4.add(printOtherShelf(row, col,i), col, 5-row);
                                break;
                        }

                    }
                }
                j++;
            }
        }




        shelfPlayer2.add(shelfImageView, 0, 0);

        shelfPlayer2.add(shelf2, 0, 0);
        otherShelf.getChildren().addAll(shelfPlayer2, shelfPlayer3, shelfPlayer4);

        if(client.getBoardView().getListOfPlayer().size()==3){
            shelfPlayer3.add(shelfImageView, 0, 0);
            shelfPlayer3.add(shelf3, 0, 0);
        }
        if(client.getBoardView().getListOfPlayer().size()==4){
            shelfPlayer4.add(shelfImageView, 0, 0);
            shelfPlayer4.add(shelf4, 0, 0);
        }

    return otherShelf;
    }
    public ImageView printOtherShelf(int row, int col, int playerNumber){
        String imageTilePath;
        Constant tile = new Constant();
        ImageView imageView = new ImageView();
        int i = 0;
        String imageTileName = null;
        if(client.getBoardView().getListOfPlayer().get(playerNumber).getShelf().getTile(row,col) != null) {
            switch (client.getBoardView().getListOfPlayer().get(playerNumber).getShelf().getTile(row, col).getColor()) {
                case BLUE:
                    imageTileName = "Cornici1.";
                    break;
                case GREEN:
                    imageTileName = "Gatti1.";
                    break;
                case YELLOW:
                    imageTileName = "Giochi1.";
                    break;
                case WHITE:
                    imageTileName = "Libri1.";
                    break;
                case PINK:
                    imageTileName = "Piante1.";
                    break;
                case LIGHTBLUE:
                    imageTileName = "Trofei1.";
                    break;

            }
            switch (client.getBoardView().getListOfPlayer().get(playerNumber).getShelf().getTile(row, col).getType()) {
                case ONE:
                    i = 1;
                    break;
                case TWO:
                    i = 2;
                    break;
                case THREE:
                    i = 3;
                    break;
            }
            imageTilePath = tile.getConstantTile() + imageTileName + i + ".png";

            Image imageTile = new Image(imageTilePath);
            imageView.setImage(imageTile);

            imageView.setPreserveRatio(true);
            imageView.setFitHeight(30);
            imageView.setFitWidth(30);
        }else{
            imageView.setStyle("-fx-border-color: black;");
            imageView.setImage(null);
            imageView.setFitHeight(30);
            imageView.setFitWidth(30);
        }
        return imageView;
    }
    public String createTile(int index) {
        String imageTileName = null;
        Constant tile = new Constant();
        switch (client.getBoardView().getTileBuffer().get(index).getColor()) {
            case BLUE:
                imageTileName = "Cornici1.";
                break;
            case GREEN:
                imageTileName = "Gatti1.";
                break;
            case YELLOW:
                imageTileName = "Giochi1.";
                break;
            case WHITE:
                imageTileName = "Libri1.";
                break;
            case PINK:
                imageTileName = "Piante1.";
                break;
            case LIGHTBLUE:
                imageTileName = "Trofei1.";
                break;

        }
        int i = 0;
        switch (client.getBoardView().getTileBuffer().get(index).getType()) {
            case ONE:
                i = 1;
                break;
            case TWO:
                i = 2;
                break;
            case THREE:
                i = 3;
                break;
        }
        return tile.getConstantTile() + imageTileName + i + ".png";

    }

    public Image createPersonalGoal(AbstractClient client) {
        Player p = new Player("Ale");
        //   Player p= client.getBoardView().getListOfPlayer().get(0);
        Constant c = new Constant();
        int persGoal = p.getGoal().CreatePersonalGoal();
        String imagePath = null;

        switch (persGoal) {
            case 0:
                imagePath = c.getCostantPersGoal() + "12.png";
                break;
            case 1:
                imagePath = c.getCostantPersGoal() + "9.png";
                break;
            case 2:
                imagePath = c.getCostantPersGoal() + "2.png";
                break;
            case 3:
                imagePath = c.getCostantPersGoal() + "3.png";
                break;
            case 4:
                imagePath = c.getCostantPersGoal() + "4.png";
                break;
            case 5:
                imagePath = c.getCostantPersGoal() + "11.png";
                break;
            case 6:
                imagePath = c.getCostantPersGoal() + "5.png";
                break;
            case 7:
                imagePath = c.getCostantPersGoal() + "10.png";
                break;
            case 8:
                imagePath = c.getCostantPersGoal() + ".png";
                break;
            case 9:
                imagePath = c.getCostantPersGoal() + "6.png";
                break;
            case 10:
                imagePath = c.getCostantPersGoal() + "8.png";
                break;
            case 11:
                imagePath = c.getCostantPersGoal() + "7.png";
                break;
        }

        return new Image(imagePath);
    }

    public void home(Stage stage, String nickname) {
        Image imageHome = new Image("images/Publisher material/Display_3.jpg");
        ImageView imageView = new ImageView(imageHome);
        BoxBlur blur = new BoxBlur(3, 4, 3);
        imageView.setEffect(blur);

        imageView.fitWidthProperty().bind(stage.widthProperty());
        imageView.fitHeightProperty().bind(stage.heightProperty());

        Label nickname1 = new Label("Enter your nickname");
        nickname1.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: black; -fx-font-family: 'Comic Sans MS';");
        TextField nicknameField = new TextField();
        nicknameField.setPromptText("Enter your nickname");
        if (nickname != null) {
            nickname1.setText("Nickname already taken, choose another one");
        }
        nicknameField.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: black; -fx-font-family: 'Comic Sans MS';");

        Button button = new Button("Play");
        VBox contentBox = new VBox(10);
        contentBox.getChildren().addAll(nickname1, nicknameField, button);
        contentBox.setStyle("-fx-alignment: center; -fx-padding: 100px; -fx-background-color: white; -fx-opacity: 0.5;");

        String regex = "^[a-zA-Z0-9 ]+$";


        button.setOnAction(e -> {
            String inp = nicknameField.getText();
            inp = inp.replace(" ", "");
            if (inp.length() <= 10 && inp.length() > 0 && inp.matches(regex)) {
                client.println(inp);
            } else {
                nicknameField.clear();
                nickname1.setText("Error, retype your nickname");
            }
        });

        contentBox.setTranslateY(360);
        contentBox.setTranslateX(500);

        contentBox.prefHeight(stage.getHeight());
        contentBox.prefWidth(stage.getWidth());

        Pane root = new Pane(new Region());

        root.getChildren().addAll(imageView, contentBox);
        stage.setTitle("Welcome to the game");
        Scene scene = new Scene(root);
        stage.setScene(scene);

        stage.sizeToScene();
      //  stage.setFullScreen(true);
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        root.setPrefHeight(bounds.getHeight());
        root.setPrefWidth(bounds.getWidth());
        stage.show();



    }

    public void update(String arg) throws IOException {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                //Update your GUI here

                switch (state) {

                    case HOME:
                        home(primaryStage, arg);
                        break;
                    case LOBBY:
                        lobby(primaryStage, arg);
                        break;
                    case PLAY:
                        try {
                            if(client.getBoardView().getListOfPlayer().size()<2){
                                playAlone(client, primaryStage);
                            }else{
                                play(client, primaryStage);
                            }
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    case END:
                         end(primaryStage, arg);
                         break;
                }
            }
        });
    }

    private void lobby(Stage primaryStage, String arg) {
        GridPane root = new GridPane();
        Scene scene1 = new Scene(root);
        Image imageLobby = new Image("images/Publisher material/Display_5.jpg");
        ImageView imageViewLobby = new ImageView(imageLobby);
        BoxBlur blur = new BoxBlur(3, 4, 3);

        imageViewLobby.setEffect(blur);
        imageViewLobby.fitWidthProperty().bind(primaryStage.widthProperty());
        imageViewLobby.fitHeightProperty().bind(primaryStage.heightProperty());


        try {
            if (client.isOwner()) {
                Button start = new Button("Start");
                start.setPrefSize(150, 80);
                start.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: black; -fx-font-family: 'Comic Sans MS';");
                start.setOnAction(e -> {
                    client.println("/start");

                });

                Button firstMatch = new Button("First match");
                firstMatch.setPrefSize(150, 80);
                firstMatch.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: black; -fx-font-family: 'Comic Sans MS';");
                firstMatch.setOnAction(e -> {
                    client.println("/firstMatch");
                });

                Button notFirstMatch = new Button("Not first match");
                notFirstMatch.setPrefSize(150, 80);
                notFirstMatch.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: black; -fx-font-family: 'Comic Sans MS';");
                notFirstMatch.setOnAction(e -> {
                    client.println("/notFirstMatch");
                });

                HBox hBox = new HBox(5);
                hBox.getChildren().addAll(start, firstMatch, notFirstMatch);
                hBox.setAlignment(Pos.CENTER);

                root.add(hBox, 2, 0);



                Screen screen = Screen.getPrimary();
                Rectangle2D bounds = screen.getVisualBounds();
                root.setPrefHeight(bounds.getHeight());
                root.setPrefWidth(bounds.getWidth());
            } else {
                Label label = new Label("Waiting for the owner...");
                label.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: black; -fx-font-family: 'Comic Sans MS';");

                root.add(label, 5, 5);

                Screen screen = Screen.getPrimary();
                Rectangle2D bounds = screen.getVisualBounds();
                root.setPrefHeight(bounds.getHeight());
                root.setPrefWidth(bounds.getWidth());

            }
            root.add(chat(), 10, 10);
            root.setStyle("-fx-background-image: url('images/Publisher%20material/Display_5.jpg'); " + "-fx-background-size: cover; " + " -fx-background-repeat: no-repeat;");
            root.setAlignment(Pos.CENTER);



        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        primaryStage.setScene(scene1);
        primaryStage.show();

    }



    @Override
    public void update() throws IOException {
        update(null);
    }


    @Override
    public void setState(State state) {
        this.state = state;
    }

    @Override
    public void setClient(AbstractClient c) {
        if (c != null) this.client = c;
        if (firstLaunch) {
            firstLaunch = false;
            launch();
        }
    }

    public void setCommand(String command) {
        this.command = command;
        System.out.println(command);
    }

    public void playAlone(AbstractClient client, Stage primaryStage) throws RemoteException {
        GridPane mainPane = new GridPane();
        Scene scene = new Scene(mainPane);
        primaryStage.setScene(scene);

        Image imageBackgroundShelf = new Image("images/misc/sfondoparquet.jpg");
        ImageView imageViewShelf = new ImageView(imageBackgroundShelf);
        BoxBlur blur = new BoxBlur(3, 3, 3);
        imageViewShelf.setEffect(blur);

        imageViewShelf.setPreserveRatio(true);
        imageViewShelf.fitWidthProperty().bind(mainPane.widthProperty());
        imageViewShelf.fitHeightProperty().bind(mainPane.heightProperty());
        mainPane.setBackground(new Background(new BackgroundImage(imageBackgroundShelf, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));


        primaryStage.setTitle("Play");
        primaryStage.setOnCloseRequest(e -> {
            // Gestire l'evento di chiusura
        });
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setX(bounds.getMinX());
        primaryStage.setY(bounds.getMinY());
        primaryStage.setWidth(bounds.getWidth());
        primaryStage.setHeight(bounds.getHeight());


        primaryStage.sizeToScene();
        primaryStage.show();
    }

    public void setRemove(Boolean firstRemove) {
        this.firstRemove = firstRemove;
    }
    public void end(Stage primaryStage, String arg){
        GridPane root = new GridPane();
        Scene sceneEnd = new Scene(root);
        Image imageEnd = new Image("images/Publisher material/Display_5.jpg");
        ImageView imageViewEnd = new ImageView(imageEnd);
        imageViewEnd.setPreserveRatio(true);
        BoxBlur blur = new BoxBlur(3, 4, 3);
        imageViewEnd.setEffect(blur);
        imageViewEnd.fitWidthProperty().bind(primaryStage.widthProperty());
        imageViewEnd.fitHeightProperty().bind(primaryStage.heightProperty());
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-image: url('images/Publisher%20material/Display_5.jpg'); " + "-fx-background-size: cover; " + " -fx-background-repeat: no-repeat;");
        Image imageLogo = new Image("images/Publisher material/Title 2000x618px.png");
        ImageView imageViewLogo = new ImageView(imageLogo);
        imageViewLogo.setPreserveRatio(true);
        imageViewLogo.setFitWidth(500);
        imageViewLogo.setFitHeight(154);
        root.add(imageViewLogo, 0, 0);
        Label labelWinner = new Label();
        if(client.getBoardView().getWinner() == null || client.getBoardView().getWinner().getNickname() == null){
            labelWinner.setText("There's no winner");
            labelWinner.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: black; -fx-font-family: 'Times New Roman';");
        }else {
            labelWinner.setText(client.getBoardView().getWinner().getNickname());
            labelWinner.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: black; -fx-font-family: 'Times New Roman';");
            labelWinner.setAlignment(Pos.CENTER);
            root.add(labelWinner, 0, 1);
        }


        client.getBoardView().getListOfPlayer().sort(new Comparator<Player>() {
            @Override
            public int compare(Player o1, Player o2) {
                if (o1.getScore()== o2.getScore()){
                    if(o1.getNickname().compareTo(o2.getNickname())>=0){
                        return 1;
                    }
                }else return -1;
                if(o1.getScore()> o2.getScore()) return  1;
                return -1;
            }
        });


        for(Player p: client.getBoardView().getListOfPlayer()){
            Label label = new Label();
            label.setText(p.getNickname() + "  " + p.getScore());
            label.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: black; -fx-font-family: 'Times New Roman';");
            label.setAlignment(Pos.CENTER);
            root.add(label, 0, 2);
        }

        Button button = new Button("Lobby");
        button.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: black; -fx-font-family: 'Times New Roman';");
        button.setOnMouseClicked(e -> {
            client.println("lobbbb");
        });

        primaryStage.setScene(sceneEnd);
        primaryStage.show();
    }
}
