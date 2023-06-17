package View;


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
import java.sql.SQLOutput;
import java.util.concurrent.atomic.AtomicInteger;

public class GUIApp extends Application implements ViewInterface{
    private boolean firstLaunch;
    private AbstractClient client;
    private State state;
    private Stage primaryStage;
    private Boolean firstRemove ;

    private String command;

    public GUIApp(){
        this.firstLaunch = true;
        this.client = PassParameters.getClient();
        this.state = PassParameters.getState();
    }

    public void start(Stage primaryStage) throws RemoteException {


        this.primaryStage = primaryStage;
        try {
            update(null);
        } catch (IOException e) {
        }

        primaryStage.show();

    }


    public void play(AbstractClient client,Stage primaryStage) throws RemoteException {
        GridPane mainPane = new GridPane();
        Scene scene = new Scene(mainPane);
        primaryStage.setScene(scene);

        Image imageBackgroungShelf = new Image("images/misc/sfondoparquet.jpg");
        ImageView imageViewShelf = new ImageView(imageBackgroungShelf);
        BoxBlur blur = new BoxBlur(3, 3, 3);
        imageViewShelf.setEffect(blur);

        imageViewShelf.setPreserveRatio(true);
        imageViewShelf.fitWidthProperty().bind(mainPane.widthProperty());
        imageViewShelf.fitHeightProperty().bind(mainPane.heightProperty());
        mainPane.setBackground(new Background(new BackgroundImage(imageBackgroungShelf, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));

        GridPane commonGoal = getCommonGoal(client);
        mainPane.add(commonGoal, 0, 1);

        mainPane.add(createShelf(client,mainPane), 1, 0);
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


        primaryStage.show();
    }
    public Pane createBoard(AbstractClient client){
        Pane boardPane = new Pane();

        Image imageBoard = new Image("images/boards/livingroom.png");
        ImageView imageViewBoard = new ImageView(imageBoard);

        imageViewBoard.setPreserveRatio(true);
        imageViewBoard.setFitHeight(650);
        imageViewBoard.setFitWidth(650);

        boardPane.getChildren().addAll(imageViewBoard);
        GridPane fillBoardGridPane = fillBoard(CellType.TWO);
        fillBoardGridPane.setPrefSize(650, 650);
        fillBoardGridPane.setAlignment(Pos.CENTER);
        fillBoard(CellType.TWO);
        boardPane.getChildren().add(fillBoardGridPane);

        return boardPane;
    }
    public Button resetCommand(){

        Button reset = new Button("Reset");
        reset.setPrefSize(100, 60);
        reset.setStyle("");
        reset.setText("Save move");
        reset.setOnAction(e -> {

        });
        return reset;
    }
//    public GridPane fillBoard(AbstractClient client){
      public GridPane fillBoard(CellType cellType){
        GridPane fillBoardPane = new GridPane();
        Pane emptyComponent = new Pane();

        emptyComponent.setMinSize(65, 65);
       for (int i = 0; i < 9; i++) {
           for (int j = 0; j < 9; j++) {
              // switch (client.getBoardView().getCell(i, j).getType()){
               switch (cellType){
                   case ONE:
                       emptyComponent.setPrefSize(65, 65);
                       fillBoardPane.add(emptyComponent, i, j);
                       break;
                   case TWO:
                       if((i==8 || j==3) || (i==5 && j==8)){
                           Pane emptyComponent2 = new Pane();
                           emptyComponent2.setPrefSize(65, 65);
                           fillBoardPane.add(emptyComponent2, i, j);

                       }else {
                           fillBoardPane.add(getTile(i, j), i, j);
                       }
                       break;
                   case THREE:
                       if (client.getBoardView().getListOfPlayer().size()>2){
                           fillBoardPane.add(getTile(i,j),i,j);
                       }else {
                           emptyComponent.setPrefSize(65, 65);
                           fillBoardPane.add(emptyComponent, i, j);
                       }
                       break;
                   case FOUR:
                       if (client.getBoardView().getListOfPlayer().size()==4) {
                           fillBoardPane.add(getTile(i, j), i, j);
                       }else {
                           emptyComponent.setPrefSize(65, 65);
                           fillBoardPane.add(emptyComponent, i, j);
                       }
                       break;
               }

                 /*    Image imageBoard = new Image("images/item tiles/Gatti1.2.png");
                     ImageView imageView = new ImageView(imageBoard);
                     imageView.setPreserveRatio(true);
                     imageView.setFitHeight(50);
                     imageView.setFitWidth(50);
                     fillBoardPane.add(imageView, i, j);

                  */
               }
           }

        return fillBoardPane;
    }

    public Button getTile(int row, int column){
        Button tileButton = new Button();
        String imageTilePath = null;
        Constant tile = new Constant();
        int i = 0;
        String imageTileName = null;
        switch (client.getBoardView().getTile(row, column).getColor()){
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

        tileButton.setPrefSize(55, 55);
        Image imageTile = new Image(imageTilePath);
        ImageView imageView = new ImageView(imageTile);
        imageView.setPreserveRatio(true);
        imageView.setFitHeight(55);
        imageView.setFitWidth(55);
        tileButton.setGraphic(imageView);


        GaussianBlur blur = new GaussianBlur(2);
        BooleanProperty isSelected = new SimpleBooleanProperty(false);
        firstRemove=true;
        tileButton.setOnAction(e -> {
            if(firstRemove){
                firstRemove = false;
                command = "/remove ";
            }
            if(isSelected.get()){
                isSelected.set(false);
                tileButton.setStyle("-fx-border-color: yellow; -fx-border-width: 4;");
                tileButton.setEffect(blur);
                tileButton.setDisable(true);
                tileButton.setOpacity(0.8);
                firstRemove=false;
                command = command + column + " " + row + " ";
                System.out.println(command);
            }else {
                isSelected.set(true);
                tileButton.setStyle("");
                tileButton.setEffect(null);
                tileButton.setDisable(false);
                tileButton.setOpacity(1);
            }
        });

        return tileButton;
    }

    /**
     * This method is used to create the common goal
     * @param client is the client
     * @return the common goal
        */

    public GridPane getCommonGoal(AbstractClient client){
        GridPane commonGoalPane = new GridPane();
        Constant commonGoal = new Constant();
        String imageGoalPath = null;
        // come prendo il numero dei giocatori?
        // come viene creato i
        //client.getPlayer

       // int size = 2;
        int i = 0;

        for(int j=0;j<2;j++) {
            i=2+j;
            switch (i) {
                case 0:
                   // new GoalAngles(size);
                    imageGoalPath = commonGoal.getConstantGoal() + "8.jpg";
                    break;
                case 1:
                    //new GoalColumn(size);
                    imageGoalPath = commonGoal.getConstantGoal() + "2.jpg";
                case 2:
                   // new GoalCouples(size);
                    imageGoalPath = commonGoal.getConstantGoal() + "4.jpg";
                    break;
                case 3:
                    //new GoalCross(size);
                    imageGoalPath = commonGoal.getConstantGoal() + "10.jpg";
                    break;
                case 4:
                    //new GoalDiagonal(size);
                    imageGoalPath = commonGoal.getConstantGoal() + "11.jpg";
                    break;
                case 5:
                    //new GoalDiffColumns(size);
                    imageGoalPath = commonGoal.getConstantGoal() + "5.jpg";
                    break;
                case 6:
                   // new GoalDiffRows(size);
                    imageGoalPath = commonGoal.getConstantGoal() + "6.jpg";
                    break;
                case 7:
                    //new GoalEight(size);
                    imageGoalPath = commonGoal.getConstantGoal() + "9.jpg";
                    break;
                case 8:
                    //new GoalQuartets(size);
                    imageGoalPath = commonGoal.getConstantGoal() + "3.jpg";
                    break;
                case 9:
                    //new GoalRow(size);
                    imageGoalPath = commonGoal.getConstantGoal() + "7.jpg";
                    break;
                case 10:
                    //new GoalSquares(size);
                    imageGoalPath = commonGoal.getConstantGoal() + "1.jpg";
                    break;
                case 11:
                    //new GoalStair(size);
                    imageGoalPath = commonGoal.getConstantGoal() + "12.jpg";
                    break;
            }
            if(j==0) {
                Image imageGoal1 = new Image(imageGoalPath);
                ImageView imageView1 = new ImageView(imageGoal1);
                imageView1.setPreserveRatio(true);
                imageView1.setFitHeight(150);
                imageView1.setFitWidth(150);
                commonGoalPane.add(imageView1, 0, 0);
            }else {
                Image imageGoal2 = new Image(imageGoalPath);
                ImageView imageView2 = new ImageView(imageGoal2);
                imageView2.setPreserveRatio(true);
                imageView2.setFitHeight(150);
                imageView2.setFitWidth(150);
                commonGoalPane.add(imageView2, 1, 0);
            }
        }
        commonGoalPane.add(resetCommand(), 2, 0);

        return commonGoalPane;
    }
    public GridPane createShelf(AbstractClient client, GridPane mainPane) throws RemoteException {
        GridPane shelfPane = new GridPane();
        GridPane shelfGridPane = new GridPane();
        Image imageShelf = new Image("images/boards/bookshelf.png");
        ImageView shelfImageView = new ImageView(imageShelf);
        shelfImageView.setFitWidth(300);
        shelfImageView.setFitHeight(300);
        shelfPane.add(shelfImageView, 0, 1);
        shelfPane.add(shelfGridPane, 0, 1);

        shelfImageView.setPreserveRatio(true);


        for(int col=0; col<5; col++){
            for(int row=0; row<6; row++){
                Button emptyButton = new Button();
                emptyButton.setPrefSize(40, 40);
                emptyButton.setDisable(true);
                emptyButton.setStyle("-fx-background-color: red;");
                shelfGridPane.add(emptyButton, col, row);
            }
        }


        for(int i=0; i<5; i++) {
            ColButton colButton = new ColButton(this, i);
            shelfGridPane.add(colButton.getButton(), i, 6);

        }

        shelfGridPane.setAlignment(Pos.CENTER);
        GridPane persGoal = new GridPane();
        if (client.isOwner()) {
            Image chairImage = new Image("images/misc/firstplayertoken.png");
            ImageView chairImageView = new ImageView(chairImage);
            chairImageView.setFitWidth(100);
            chairImageView.setFitHeight(100);
            chairImageView.setPreserveRatio(true);
            persGoal.add(chairImageView, 0, 0);
        //    shelfPane.add(chairImageView, 0, 1);
         }else {
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
        persGoal.add(personalGoalImageView,1,0);

        persGoal.setAlignment(Pos.CENTER);

        shelfPane.add(persGoal,0,2);
        shelfGridPane.add(bufferTile(),0,0);

        return shelfPane;
    }


public GridPane bufferTile(){
        GridPane bufferTile = new GridPane();
        if(client.getBoardView().getTileBuffer().size()>=1){
            Button button1 = new Button();
            button1.setPrefSize(40, 40);
            button1.setDisable(true);
            Image image = new Image(createTile(0));
            button1.setGraphic(new ImageView(image));
            bufferTile.add(button1, 0, 0);
        }
        if(client.getBoardView().getTileBuffer().size()>=2){
            Button button2 = new Button();
            button2.setPrefSize(40, 40);
            button2.setDisable(true);
            Image image = new Image(createTile(1));
            button2.setGraphic(new ImageView(image));
            bufferTile.add(button2, 0, 1);
        }
        if(client.getBoardView().getTileBuffer().size()>=3){
            Button button3 = new Button();
            button3.setPrefSize(40, 40);
            button3.setDisable(true);
            Image image = new Image(createTile(2));
            button3.setGraphic(new ImageView(image));
            bufferTile.add(button3, 0, 2);
        }


        return bufferTile;
}
public String createTile(int index) {
      String imageTileName = null;
        Constant tile = new Constant();
    switch (client.getBoardView().getTileBuffer().get(index).getColor()){
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
Player p = new Player("Ale",true);
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
                break; case 2:
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

    public void home(Stage stage, String nickname){
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
        if(nickname!=null){
            nickname1.setText("Nickname already taken, choose another one");
        }
        nicknameField.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: black; -fx-font-family: 'Comic Sans MS';");

        Button button = new Button("Play");
        VBox contentBox = new VBox(10);
        contentBox.getChildren().addAll(nickname1, nicknameField, button);
        contentBox.setStyle("-fx-alignment: center; -fx-padding: 100px; -fx-background-color: white; -fx-opacity: 0.5;");
        final String[] nicknameString = new String[1];
        String regex = "^[a-zA-Z0-9 ]+$";



        button.setOnAction(e -> {
            String inp = nicknameField.getText();
            inp= inp.replace(" ", "");
            if (inp.length() <= 10 && inp.length() > 0 && inp.matches(regex)){
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

        Pane root = new Pane(new Region( ));
        Scene scene = new Scene(root);
        root.getChildren().addAll(imageView, contentBox);
        stage.setTitle("Welcome to the game");
        stage.setScene(scene);
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        root.setPrefHeight(bounds.getHeight());
        root.setPrefWidth(bounds.getWidth());
        stage.show();


    }

        public void update(String arg) throws IOException {
        switch (this.state){

            case HOME:
                home(primaryStage,arg);
                break;
            case LOBBY:
                lobby(primaryStage,arg);
                break;
            case PLAY:
                play(client,primaryStage);
                break;
           // case END:
             //   end();
               // break;
        }
        }

    private void lobby(Stage primaryStage, String arg) {
        Image imageLobby = new Image("images/Publisher material/Display_5.jpg");
        ImageView imageViewLobby = new ImageView(imageLobby);
        BoxBlur blur = new BoxBlur(3, 4, 3);
        imageViewLobby.setEffect(blur);
        imageViewLobby.fitWidthProperty().bind(primaryStage.widthProperty());
        imageViewLobby.fitHeightProperty().bind(primaryStage.heightProperty());
        try {
            if(client.isOwner()){
                Button start = new Button("Start");
                start.setPrefSize(100, 50);
                start.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: black; -fx-font-family: 'Comic Sans MS';");
                start.setOnAction(e -> {
                        client.println("/start");
                });

                Button firstMatch = new Button("First match");
                firstMatch.setPrefSize(100, 50);
                firstMatch.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: black; -fx-font-family: 'Comic Sans MS';");
                firstMatch.setOnAction(e -> {
                    client.println("/firstMatch");
                });

                Button notFirstMatch = new Button("Not first match");
                notFirstMatch.setPrefSize(100, 50);
                notFirstMatch.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: black; -fx-font-family: 'Comic Sans MS';");
                notFirstMatch.setOnAction(e -> {
                    client.println("/notFirstMatch");
                });

                HBox hBox = new HBox(5);
                hBox.getChildren().addAll(start, firstMatch, notFirstMatch);
                hBox.setAlignment(Pos.CENTER);


                Pane root = new Pane(new Region());
                Scene scene = new Scene(root);
                root.getChildren().addAll(imageViewLobby, hBox);
                primaryStage.setScene(scene);
                Screen screen = Screen.getPrimary();
                Rectangle2D bounds = screen.getVisualBounds();
                root.setPrefHeight(bounds.getHeight());
                root.setPrefWidth(bounds.getWidth());
            }else {
                Label label = new Label("Waiting for the owner...");
                label.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: black; -fx-font-family: 'Comic Sans MS';");
                TextField wait = new TextField();
                wait.setPromptText("Enter your nickname");
                wait.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: black; -fx-font-family: 'Comic Sans MS';");

                Pane root = new Pane(new Region());
                Scene scene = new Scene(root);
                root.getChildren().addAll(imageViewLobby, label);
                primaryStage.setScene(scene);
                Screen screen = Screen.getPrimary();
                Rectangle2D bounds = screen.getVisualBounds();
                root.setPrefHeight(bounds.getHeight());
                root.setPrefWidth(bounds.getWidth());

            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        primaryStage.show();
    }

    @Override
    public void update() throws IOException {

    }


    @Override
        public void setState(State state) {
            this.state = state;
        }

    @Override
    public void setClient(AbstractClient client) {
        this.client = client;
        System.out.println("pappapero: " + this.client);
        if(firstLaunch){
            firstLaunch = false;
            launch();
        }
    }

    public void setCommand(String command){
        this.command = command;
        System.out.println(command);
    }


}
