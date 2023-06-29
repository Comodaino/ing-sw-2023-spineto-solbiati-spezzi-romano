package View;


import Distributed.AbstractClient;

import Model.CommonGoals.*;
import Model.PersonalGoal;
import Model.Player;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.RemoteException;
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
    private boolean firstSwitch=true;
    BooleanProperty cancel = new SimpleBooleanProperty(false);


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
        primaryStage.getIcons().add(new Image("images/Publisher material/Icon 50x50px.png"));

        primaryStage.setMaximized(true);
        primaryStage.show();

    }


    public void play(AbstractClient client, Stage primaryStage) throws RemoteException {
        GridPane mainPane = new GridPane();
        Group root = new Group();
    //    ScrollPane scrollPane = new ScrollPane();
        Scene scene = new Scene(mainPane);
        primaryStage.setScene(scene);

   //     scrollPane.setContent(mainPane);
   //     scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
     //   scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        mainPane.setStyle("-fx-background-image: url('images/misc/sfondoparquet.jpg'); -fx-background-size: 100% 100%; -fx-background-repeat: no-repeat; -fx-background-position: center; ");
 /*   Image imageBackgroundShelf = new Image("");
        ImageView imageViewShelf = new ImageView(imageBackgroundShelf);
        BoxBlur blur = new BoxBlur(3, 3, 3);
        imageViewShelf.setEffect(blur);
        imageViewShelf.setPreserveRatio(true);
        imageViewShelf.fitWidthProperty().bind(mainPane.widthProperty());
        imageViewShelf.fitHeightProperty().bind(mainPane.heightProperty());
        mainPane.setBackground(new Background(new BackgroundImage(imageBackgroundShelf, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));


  */

        GridPane commonGoal = getCommonGoal(client);

        root.getChildren().add(commonGoal);

        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);

        vBox.getChildren().addAll(createBoard(client),commonGoal);
        mainPane.add(createShelf(client, mainPane), 1, 0);
        mainPane.add(showOtherShelf(client), 2, 0);

        mainPane.add(vBox, 0, 0);
        primaryStage.setTitle("Play");
        primaryStage.setOnCloseRequest(e -> {
            // Manage closing event
        });
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        mainPane.setPrefHeight(bounds.getHeight());
        mainPane.setPrefWidth(bounds.getWidth());

        HBox hBox = new HBox();
        mainPane.add(hBox, 0, 2);

        primaryStage.sizeToScene();
        primaryStage.setMaximized(true);

        primaryStage.show();
    }

    private Pane createBoard(AbstractClient client) {
        Pane boardPane = new Pane();


        boardPane.setPrefSize(650, 650);

        boardPane.setStyle("-fx-background-image: url('images/boards/livingroom.png');-fx-background-size: 650 650; -fx-background-repeat: no-repeat; -fx-background-position: center;");

     /*   Image imageBoard = new Image("images/boards/livingroom.png");
        ImageView imageViewBoard = new ImageView(imageBoard);

        imageViewBoard.setPreserveRatio(true);
        imageViewBoard.setFitHeight(650);
        imageViewBoard.setFitWidth(650);
        boardPane.getChildren().addAll(imageViewBoard);

      */

        GridPane fillBoardGridPane = fillBoard(client);
        fillBoardGridPane.setHgap(0);
        fillBoardGridPane.setVgap(0);
        fillBoardGridPane.setPrefSize(650, 650);
        fillBoardGridPane.setAlignment(Pos.CENTER);
        fillBoardGridPane.setLayoutX(-2);
        fillBoardGridPane.setLayoutY(-1);
        boardPane.getChildren().add(fillBoardGridPane);

        return boardPane;
    }

    private Button resetCommand() {
        Button reset = new Button("Reset");
        reset.setPrefSize(100, 60);
        reset.setStyle("");
        reset.setText("Execute move");
        reset.setOnMouseClicked(e -> {
            if(command!=null) client.println(command);
            command= null;
            firstRemove = true;
            firstSwitch = true;
        });
        return reset;
    }
    private Button cancel(){
        Button reset = new Button("cancel");
        reset.setPrefSize(100, 60);
        reset.setStyle("");
        reset.setText("Cancel move");
        reset.setOnMouseClicked(e -> {
            cancel.set(true);
            command= null;
            firstRemove = true;
            firstSwitch = true;
            cancel.set(false);
        });
        return reset;
    }
    private Button endGame(){
        Button reset = new Button("end");
        reset.setPrefSize(100, 60);
        reset.setStyle("");
        reset.setText("DEBUG END GAME");
        reset.setOnMouseClicked(e -> {
            client.println("/end");
        });
        return reset;

    }
    private GridPane fillBoard(AbstractClient client){
        GridPane fillBoardPane = new GridPane();
        fillBoardPane.setHgap(0);
        fillBoardPane.setVgap(0);

        //TODO add control

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
               Pane emptyPane = new Pane();
                emptyPane.setPrefSize(65, 65);
                emptyPane.setStyle("-fx-background-size: 0;-fx-border-width: 0; -fx-background-color: transparent;-fx-alignment: center;  ");
               fillBoardPane.add(emptyPane, i, j);
            }
        }

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (!client.getBoardView().getCell(i, j).isEmpty()) {
                    fillBoardPane.add(getTile(i,j), j, i);
                }
            }
        }
        return fillBoardPane;
    }

    private Button getTile(int row, int column) {
        Button tileButton = new Button();
        tileButton.setPrefSize(65, 65);
        tileButton.setMaxSize(65, 65);
      //  tileButton.setStyle(" -fx-border-radius:  1; -fx-border-height: 0; -fx-alignment: center;");

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
        tileButton.setStyle("-fx-background-image: url('"+imageTilePath+"') ; -fx-background-size: 65 65; -fx-background-repeat: no-repeat; -fx-background-position: center; -fx-border-width: 2; -fx-alignment: center;  ");
   /*   Image imageTile = new Image(imageTilePath);
        ImageView imageView = new ImageView(imageTile);
        imageView.setPreserveRatio(true);
        imageView.setFitHeight(50);
        imageView.setFitWidth(50);


        tileButton.setGraphic(imageView);
*/

        GaussianBlur blur = new GaussianBlur(2);
        BooleanProperty isSelected = new SimpleBooleanProperty(false);
        AtomicInteger count = new AtomicInteger(0);

        cancel.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(oldValue == true) return;
                tileButton.setEffect(null);
                tileButton.setDisable(false);
                tileButton.setOpacity(1);
                tileButton.setStyle("-fx-background-image: url('"+imageTilePath+"') ;-fx-background-size: 65 65; -fx-background-repeat: no-repeat; -fx-background-position: center; -fx-border-width: 0; -fx-alignment: center; ");

                if(!isSelected.get()){
                    isSelected.set(true);
                    count.decrementAndGet();
                }
            }
        });

        if(count.get() < 3) {
            tileButton.setOnMouseClicked(e -> {
                try {
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
                        tileButton.setStyle("-fx-background-image: url('"+imageTilePath+"') ;-fx-border-width: 2; -fx-border-radius: 0;  -fx-opacity: 0.8; -fx-alignment: center;-fx-background-repeat: no-repeat; -fx-border-color: lightblue;");
                  //      tileButton.setEffect(blur);
                        tileButton.setDisable(true);
                  //      tileButton.setOpacity(0.8);
                        firstRemove = false;
                        command = command + row + " " + column + " ";
                        System.out.println(command);
                    } else {
                        isSelected.set(true);
                        tileButton.setStyle("-fx-background-image: url('"+imageTilePath+"'); -fx-border-width: 0; -fx-opacity: 1; -fx-alignment: center;-fx-background-repeat: no-repeat;");
                    //    tileButton.setEffect(null);
                        tileButton.setDisable(false);
                    //    tileButton.setOpacity(1);
                    }
                }
        });
        }
        return tileButton;
    }



    /**
     * This method is used to create the common goal
     *
     * @param client is the client
     * @return the common goal
     * @author Alessandra Romano
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
                Pane pane = new Pane();
                String url = commonGoal.getConstantGoal() + photo + ".jpg";
            /*    Image imageGoal1 = new Image(commonGoal.getConstantGoal() + photo + ".jpg");
                ImageView imageView1 = new ImageView(imageGoal1);
                imageView1.setPreserveRatio(true);
                imageView1.setFitHeight(150);
                imageView1.setFitWidth(150);


             */
                pane.setPrefSize(150, 100);
                pane.setStyle("-fx-background-image: url('"+url+"') ;-fx-background-size: 150 100; -fx-background-repeat: no-repeat; -fx-background-position: center;");
                ImageView imageView2 = new ImageView();
            //    pane.getChildren().add(imageView1);
                if(client.getBoardView().getListOfPlayer().size()==2){
                    if(cg.getCompleted().size() >= client.getBoardView().getListOfPlayer().size()){
                        imageView2.setImage(null);
                    }else {
                        if (cg.getCompleted().size() == 0) {
                            imageView2 = getImageOfScoreOfCommonGoal(8);
                        }
                        if (cg.getCompleted().size() == 1) {
                            imageView2 = getImageOfScoreOfCommonGoal(4);
                        }
                    }
                }
                if(cg.getCompleted().size() >= client.getBoardView().getListOfPlayer().size()){
                    imageView2.setImage(null);
                }else {
                    if (cg.getCompleted().size() == 0) {
                        imageView2 = getImageOfScoreOfCommonGoal(8);
                    }
                    if (cg.getCompleted().size() == 1) {
                        imageView2 = getImageOfScoreOfCommonGoal(6);
                    }
                    if (cg.getCompleted().size()==2 ){
                        imageView2 = getImageOfScoreOfCommonGoal(4);
                    }
                    if (cg.getCompleted().size()==3 ){
                        imageView2 = getImageOfScoreOfCommonGoal(2);
                    }

                }
                pane.getChildren().add(imageView2);
                commonGoalPane.add(pane, 0, 0);


            } else {
                Pane pane2 = new Pane();
                String url2 = commonGoal.getConstantGoal() + photo + ".jpg";
                pane2.setPrefSize(150, 100);
                pane2.setStyle("-fx-background-image: url('"+url2+"') ; -fx-background-size: 150 100; -fx-background-repeat: no-repeat; -fx-background-position: center center;");

             /*   Image imageGoal2 = new Image(commonGoal.getConstantGoal() + photo + ".jpg");
                ImageView imageView3 = new ImageView(imageGoal2);
                imageView3.setPreserveRatio(true);
                imageView3.setFitHeight(150);
                imageView3.setFitWidth(150);


              */
                ImageView imageView4 = new ImageView();
            //    pane2.getChildren().add(imageView3);
                if(client.getBoardView().getListOfPlayer().size()==2){
                    if(cg.getCompleted().size() >= client.getBoardView().getListOfPlayer().size()){
                        imageView4.setImage(null);
                    }else {
                        if (cg.getCompleted().size() == 0) {
                            imageView4 = getImageOfScoreOfCommonGoal(8);
                        }
                        if (cg.getCompleted().size() == 1) {
                            imageView4 = getImageOfScoreOfCommonGoal(4);
                        }
                    }
                }
                if(cg.getCompleted().size() >= client.getBoardView().getListOfPlayer().size()){
                    imageView4 = null;
                }else {
                    if (cg.getCompleted().size() == 0) {
                        imageView4 = getImageOfScoreOfCommonGoal(8);
                    }
                    if (cg.getCompleted().size() == 1) {
                        imageView4 = getImageOfScoreOfCommonGoal(6);
                    }
                    if (cg.getCompleted().size()==2 ){
                        imageView4 = getImageOfScoreOfCommonGoal(4);
                    }
                    if (cg.getCompleted().size()==3 ){
                        imageView4 = getImageOfScoreOfCommonGoal(2);
                    }

                }

                pane2.getChildren().add(imageView4);
                commonGoalPane.add(pane2, 1, 0);
            }
        }
        commonGoalPane.add(resetCommand(), 2, 0);
        commonGoalPane.add(cancel(),3,0);
        commonGoalPane.add(endGame(), 4,0);
        return commonGoalPane;
    }
    private ImageView getImageOfScoreOfCommonGoal(int score){
        Image imageScore = new Image("images/scoring tokens/scoring_"+score+".jpg");
        ImageView imageViewScore = new ImageView(imageScore);
        imageViewScore.setPreserveRatio(true);
        imageViewScore.setFitHeight(55);
        imageViewScore.setFitWidth(55);
        imageViewScore.setTranslateX(82);
        imageViewScore.setTranslateY(20);
        return imageViewScore;
    }

    private VBox createShelf(AbstractClient client, GridPane mainPane) throws RemoteException {
        GridPane shelfPane = new GridPane();
        GridPane shelfGridPane = new GridPane();
        GridPane gridPane = new GridPane();
      //  shelfGridPane.setStyle("-fx-background-image: url('images/boards/bookshelf.png') ;-fx-background-size: 300 300; -fx-background-repeat: no-repeat; -fx-background-position: center;");
        Image imageShelf = new Image("images/boards/bookshelf.png");
        ImageView shelfImageView = new ImageView(imageShelf);
        shelfImageView.setFitWidth(300);
        shelfImageView.setFitHeight(300);

        shelfPane.add(shelfImageView, 0, 0);
        shelfPane.add(shelfGridPane, 0, 0);
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
                    Pane pane = new Pane();
                    String url = printShelf(nPlayer, row, col);
                    pane.setPrefSize(40, 40);
                    pane.setStyle("-fx-background-image: url('"+url+"'); -fx-background-size: 40 40; -fx-background-repeat: no-repeat; -fx-background-position: center;");
                        shelfGridPane.add(pane, col, 5-row);
                    } else {
                    Pane pane = new Pane();
                    pane.setPrefSize(40, 40);
                    pane.setStyle("-fx-background-color: transparent; -fx-background-size: 40 40; -fx-background-repeat: no-repeat; -fx-background-position: center;");
                    shelfGridPane.add(pane, col, 5-row);
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
        } else {
            Label label = new Label();
            label.setPrefSize(100, 100);
            label.setStyle("-fx-background-color: transparent;");
            persGoal.add(label, 0, 0);
        }


        String personalGoalImage = createPersonalGoal(client);
        Pane paneImagePersGoal = new Pane();
        paneImagePersGoal.setPrefSize(150, 150);
        paneImagePersGoal.setStyle("-fx-background-image: url('"+personalGoalImage+"') ; -fx-background-size: 98 150; -fx-background-repeat: no-repeat; -fx-background-position: center ;");
    /*    ImageView personalGoalImageView = new ImageView(personalGoalImage);
        personalGoalImageView.setFitWidth(150);
        personalGoalImageView.setFitHeight(150);
        personalGoalImageView.setPreserveRatio(true);


     */
        persGoal.add(paneImagePersGoal, 1, 0);
        persGoal.setAlignment(Pos.CENTER);
        gridPane.add(persGoal, 0, 0);

        gridPane.add(chat(),0,1);


        for(int i=0; i<client.getBoardView().getListOfPlayer().size();i++) {
            Label turn = new Label();
            if (!client.getBoardView().getCurrentPlayer().getNickname().equals(client.getNickname())) {

                turn.setText("It's " + client.getBoardView().getCurrentPlayer().getNickname() + "'s turn");
                turn.setStyle("-fx-background-color: white; -fx-opacity: 0.8; -fx-font-size: 15px; -fx-font-family: Times New Roman;");
                gridPane.add(turn, 0, 2);
            }
        }

        if(client.getBoardView().getCurrentPlayer().getNickname().equals(client.getNickname())) {
            VBox vBox = new VBox();
            vBox.setStyle("-fx-background-color: white;-fx-opacity: 0.8; -fx-font-size: 15 px;");
            Label label = new Label();
            Label yourTurn = new Label();
            yourTurn.setText("It's your turn");
            yourTurn.setStyle("-fx-background-color: white; -fx-opacity: 1; -fx-font-size: 15px; -fx-font-family: 'Times New Roman';");
            vBox.getChildren().add(yourTurn);

            label.setText("Choose the order of the tiles");
            label.setStyle("-fx-background-color: white;-fx-opacity: 1; -fx-font-size: 15px; -fx-font-family: 'Times New Roman';");
            vBox.getChildren().add(label);

            GridPane bufferPane = new GridPane();
           for(int i =0; i<client.getBoardView().getTileBuffer().size();i++) {
               SwitchButton switchButton = new SwitchButton(this, i,command,createTile(i));
            /*   Image image = new Image(createTile(i));
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(40);
                imageView.setFitHeight(40);
                imageView.setPreserveRatio(true);


             */
               switchButton.setGraphic(createTile(i));
               bufferPane.add(switchButton.getButton(), i, 0);
               int finalI = i;
               cancel.addListener(new ChangeListener<Boolean>() {
                   @Override
                   public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                       if(oldValue == true) return;

                       switchButton.setOpacity(1);
                       switchButton.setGraphic(createTile(finalI));
                       switchButton.setGraphic(createTile(finalI));
                       switchButton.setStyle(" -fx-border-radius:  1; -fx-border-height: 0; -fx-alignment: center;");

                       setFirstSwitch(true);
                       if(!switchButton.getIsSelected2()){
                           switchButton.setIsSelected2(true);
                       }
                   }
               });
           }
           vBox.getChildren().add(bufferPane);
           gridPane.add(vBox, 0, 2);
        }
        ;
        VBox createShelf = new VBox();
        createShelf.getChildren().addAll(shelfPane,gridPane);

        return createShelf;
    }

    private String printShelf(int nPlayer, int row, int col){
        String imageTilePath;
        Constant tile = new Constant();
   //     ImageView imageView = new ImageView();
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

     /*       Image imageTile = new Image(imageTilePath);
            imageView.setImage(imageTile);
            imageView.setPreserveRatio(true);
            imageView.setFitHeight(40);
            imageView.setFitWidth(40);


      */
        return imageTilePath;
    }
    private Pane chat(){
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

        for(int i=0; i<client.getBoardView().getPersonalChatBuffer().size() ;i++) {
            if (client.getBoardView().getPersonalChatBuffer().size() == 0 || client.getBoardView().getPersonalChatBuffer().get(i) == null)
                break;

            try {
                if (client.getBoardView().getPersonalChatBuffer().get(i).getRecipient().equals(client.getNickname())) {
                    Label chatLabel = new Label();

                    chatLabel.setText(client.getBoardView().getPersonalChatBuffer().get(i).getContent());
                    chatBox2.getChildren().add(chatLabel);
                    chatLabel.setStyle("-fx-background-color: white;");
                    chatBox2.setStyle("-fx-background-color: white;");
                }

            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
        chatBox.getChildren().addAll(chatField,chatBox2);
        chatPane.getChildren().addAll(chatBox3);
        chatPane.getChildren().addAll(chatBox);
        return chatPane;
    }

   private VBox showOtherShelf(AbstractClient client) throws RemoteException {

       VBox otherShelf = new VBox();
       GridPane shelfPlayer2 = new GridPane();
       GridPane shelfPlayer3 = new GridPane();
       GridPane shelfPlayer4 = new GridPane();

       shelfPlayer2.setPrefSize(200, 200);
     //  shelf2.setStyle("-fx-background-image: url('images/boards/bookshelf.png') ; -fx-background-size: 200 200; -fx-background-repeat: no-repeat; -fx-background-position: center center;");

    /*   Image imageShelf = new Image("images/boards/bookshelf.png");
       ImageView shelfImageView = new ImageView(imageShelf);
       shelfImageView.setFitWidth(200);
       shelfImageView.setFitHeight(200);
       shelfImageView.setPreserveRatio(true);

     */
       GridPane shelf2 = new GridPane();
       shelf2.setPrefSize(200, 200);
       shelf2.setStyle("-fx-background-image: url('images/boards/bookshelf.png') ; -fx-background-size: 200 200; -fx-background-repeat: no-repeat; -fx-background-position: center;");

       GridPane shelf3 = new GridPane();
       GridPane shelf4 = new GridPane();
       shelf2.setAlignment(Pos.CENTER);
       shelf3.setAlignment(Pos.CENTER);
       shelf4.setAlignment(Pos.CENTER);



       for (int i = 0; i < 5; i++) {
           Button emptyButton = new Button();
           emptyButton.setPrefSize(31, 1);
           emptyButton.setMaxSize(31, 1);
           emptyButton.setPrefWidth(31);
           emptyButton.setPrefHeight(1);
           emptyButton.setDisable(true);
           emptyButton.setStyle(" -fx-border-width: 0;-fx-border-height: 0;-fx-border-color: transparent; -fx-background-color: transparent; -fx-min-height: 10; -fx-max-width: 31;");
           shelf2.add(emptyButton, i, 6);
       }


    //   shelf2.setTranslateY(10);
       shelf2.setTranslateX(2);
       for (int i = 0; i < 5; i++) {
           Button emptyButton = new Button();
           emptyButton.setPrefSize(31, 1);
           emptyButton.setPrefWidth(31);
           emptyButton.setPrefHeight(1);
           emptyButton.setDisable(true);
           emptyButton.setStyle(" -fx-border-width: 0;-fx-border-height: 0;-fx-border-color: transparent; -fx-background-color: transparent; -fx-min-height: 10; -fx-max-width: 31;");

           shelf3.add(emptyButton, i, 6);
       }

       shelf3.setTranslateY(10);
       shelf3.setTranslateX(2);

       for (int i = 0; i < 5; i++) {
           Button emptyButton = new Button();
           emptyButton.setPrefSize(31, 1);
           emptyButton.setPrefWidth(31);
           emptyButton.setPrefHeight(1);
           emptyButton.setDisable(true);
           emptyButton.setStyle(" -fx-border-width: 0;-fx-border-height: 0;-fx-border-color: transparent; -fx-background-color: transparent; -fx-min-height: 10; -fx-max-width: 31;");
           shelf4.add(emptyButton, i, 6);
       }

       shelf4.setTranslateY(10);
       shelf4.setTranslateX(2);


       int j = 0;
       for (int i = 0; i < client.getBoardView().getListOfPlayer().size(); i++) {

           if (!client.getBoardView().getListOfPlayer().get(i).getNickname().equals(client.getNickname())) {
               for (int row = 0; row < 6; row++) {
                   for (int col = 0; col < 5; col++) {
                       switch (j) {
                           case 0:
                               Label namePlayer2 = new Label();
                               Pane pane2 = new Pane();
                               pane2.setPrefSize(25,25);
                               String url2 = printOtherShelf(row, col, i);
                               pane2.setStyle("-fx-background-image: url('"+url2+"'); -fx-background-size: 25 25; -fx-background-repeat: no-repeat; -fx-background-position: center center; ");
                               shelf2.add(pane2, col, 5 - row);
                               namePlayer2.setText("Player: " + client.getBoardView().getListOfPlayer().get(i).getNickname());
                               shelfPlayer2.add(namePlayer2, 0, 1);
                               break;

                           case 1:
                               Pane pane3 = new Pane();
                               pane3.setPrefSize(25,25);
                               String url3 = printOtherShelf(row, col, i);
                               pane3.setStyle("-fx-background-image: url('"+url3+"'); -fx-background-size: 25 25; -fx-background-repeat: no-repeat; -fx-background-position: center center;");
                               Label namePlayer3 = new Label();
                               shelf3.add(pane3, col, 5 - row);
                               namePlayer3.setText("Player: " + client.getBoardView().getListOfPlayer().get(i).getNickname());
                               shelfPlayer3.add(namePlayer3, 1, 2);
                               break;
                           case 2:
                               Pane pane4 = new Pane();
                               pane4.setPrefSize(25,25);
                               String url4 = printOtherShelf(row, col, i);
                               pane4.setStyle("-fx-background-image: url('"+url4+"'); -fx-background-size: 25 25; -fx-background-repeat: no-repeat; -fx-background-position: center center;");
                               Label namePlayer4 = new Label();
                               shelf4.add(pane4, col, 5 - row);
                               namePlayer4.setText("Player: " + client.getBoardView().getListOfPlayer().get(i).getNickname());
                               shelfPlayer4.add(namePlayer4, 2, 3);
                               break;
                       }

                   }
               }
               j++;
           }
       }

    //   shelfPlayer2.add(shelfImageView, 0, 0);
       shelfPlayer2.add(shelf2, 0, 0);

       if (client.getBoardView().getListOfPlayer().size() >= 3) {
           shelfPlayer3.setStyle("-fx-background-image: url('images/boards/bookshelf.png') ; -fx-background-size: 200 200; -fx-background-repeat: no-repeat; -fx-background-position: center center;");

       /*    ImageView shelfImageView3 = new ImageView(imageShelf);
           shelfImageView3.setFitWidth(200);
           shelfImageView3.setFitHeight(200);
           shelfImageView3.setPreserveRatio(true);
           shelfPlayer3.add(shelfImageView3, 1, 1);

        */
           shelfPlayer3.add(shelf3, 1, 1);
       }
       if (client.getBoardView().getListOfPlayer().size() == 4) {
           shelfPlayer4.setStyle("-fx-background-image: url('images/boards/bookshelf.png') ; -fx-background-size: 200 200; -fx-background-repeat: no-repeat; -fx-background-position: center center;");
/*
           ImageView shelfImageView4 = new ImageView(imageShelf);
           shelfImageView4.setFitWidth(200);
           shelfImageView4.setFitHeight(200);
           shelfImageView4.setPreserveRatio(true);
           shelfPlayer4.add(shelfImageView4, 2, 2);

 */
           shelfPlayer4.add(shelf4, 2, 2);
       }

       otherShelf.getChildren().addAll(shelfPlayer2, shelfPlayer3, shelfPlayer4);

       return otherShelf;
   }
    private String printOtherShelf(int row, int col, int playerNumber){
        String imageTilePath= null;
        Constant tile = new Constant();
   //     ImageView imageView = new ImageView();
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

        /*    Image imageTile = new Image(imageTilePath);
            imageView.setImage(imageTile);

            imageView.setPreserveRatio(true);
            imageView.setFitHeight(25);
            imageView.setFitWidth(25);

         */
        }else{
            imageTilePath = null;
        //    imageView.setFitHeight(25);
         //   imageView.setFitWidth(25);
        }
        return imageTilePath;
    }
    private String createTile(int index) {
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
        return tile.getConstantTile()+ imageTileName + i + ".png";

    }

    private String createPersonalGoal(AbstractClient client) {

        Player p= null;
        for(int playerIndex=0; playerIndex<client.getBoardView().getListOfPlayer().size(); playerIndex++){
            try {
                if(client.getBoardView().getListOfPlayer().get(playerIndex).getNickname().equals(client.getNickname())){
                    p = client.getBoardView().getListOfPlayer().get(playerIndex);
                }
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }

        Constant c = new Constant();
        int index = p.getGoal().getNumOfGoal();
        String imagePath = null;

        switch (index) {
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

        return imagePath;
    }

    public void home(Stage stage, String nickname) {
        Image imageHome = new Image("images/Publisher material/Display_3.jpg");
        ImageView imageView = new ImageView(imageHome);

        BoxBlur blur = new BoxBlur(3, 4, 3);
        imageView.setEffect(blur);

        imageView.fitWidthProperty().bind(stage.widthProperty());
        imageView.fitHeightProperty().bind(stage.heightProperty());

        Label nickname1 = new Label("Enter your nickname");
        nickname1.setStyle("-fx-font-size: 25px; -fx-font-weight:bold; -fx-text-fill: black; -fx-font-family: 'Times New Roman';");
        TextField nicknameField = new TextField();
        nicknameField.setPromptText("Enter your nickname");
        if (nickname != null) {
            nickname1.setText("Nickname already taken, choose another one");
        }
        nicknameField.setStyle("-fx-font-size: 25px; -fx-font-weight:bold ; -fx-text-fill: black; -fx-font-family: 'Times New Roman';");

        Button button = new Button("Play");
        VBox contentBox = new VBox();
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

        contentBox.prefHeight(stage.getHeight());
        contentBox.prefWidth(stage.getWidth());

        GridPane root = new GridPane();

        Constant c = new Constant();
        String urlDisplay3 = c.getDisplay3();
        root.setStyle("-fx-background-image: url('"+urlDisplay3+"'); -fx-background-size: cover; " + " -fx-background-repeat: no-repeat; ");

        root.add(contentBox, 1, 1);

        root.setAlignment(Pos.CENTER);
        stage.setTitle("Welcome to the game");
        Scene scene = new Scene(root);
        stage.setScene(scene);

        stage.setMaximized(true);
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        root.setPrefHeight(bounds.getHeight());
        root.setPrefWidth(bounds.getWidth());
        stage.show();

    }

    public void update(String arg) throws IOException {
        if( arg!=null && arg.equals("disconnected")) {
            disconnected(primaryStage, arg);
        }
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

    private void disconnected(Stage primaryStage, String arg){
        GridPane root = new GridPane();
        Scene scene1 = new Scene(root);
        Image imageDisconnected = new Image("images/Publisher material/Display_5.jpg");
        ImageView imageViewDisconnected = new ImageView(imageDisconnected);
        BoxBlur blur = new BoxBlur(3, 4, 3);
        imageViewDisconnected.setEffect(blur);
        imageViewDisconnected.fitWidthProperty().bind(primaryStage.widthProperty());
        imageViewDisconnected.fitHeightProperty().bind(primaryStage.heightProperty());
        VBox contentBox = new VBox();
        Label text = new Label();
        text.setText("Unable to communicate with the server");
        text.setStyle("-fx-font-size: 20px; -fx-text-fill: black; -fx-font-family: 'Times New Roman'; -fx-background-color: white; -fx-opacity: 1;" );

        contentBox.getChildren().addAll(text);
        contentBox.setStyle("-fx-alignment: center;  -fx-background-color: white; -fx-opacity: 0.9;");

        Button button = new Button("Exit");
        button.setStyle("-fx-font-size: 20px;  -fx-text-fill: black; -fx-font-family: 'Times New Roman';");
        button.setPrefSize(150, 80);
        button.setOnAction(e -> {
            client.println("/exit");
        });

        contentBox.getChildren().addAll(button);
        contentBox.setAlignment(Pos.CENTER);

        Constant c = new Constant();
        String urlDisplay5 = c.getDisplay5();
        root.setStyle("-fx-background-image: url('"+urlDisplay5+"'); " + "-fx-background-size: cover; " + " -fx-background-repeat: no-repeat;");

        root.add(contentBox, 2, 1);
        primaryStage.setMaximized(true);
        primaryStage.setScene(scene1);
        primaryStage.show();


    }
    private void lobby(Stage primaryStage, String arg) {
        GridPane root = new GridPane();

        VBox contentBox = new VBox();
        contentBox.setStyle("-fx-alignment: center;  -fx-background-color: white; -fx-opacity: 0.9;");

        Scene scene1 = new Scene(root);

        BoxBlur blur = new BoxBlur(3, 4, 3);

        try {
            if (client.isOwner()) {
                Button start = new Button("Start");
                start.setPrefSize(150, 80);
                start.setStyle("-fx-font-size: 20px;  -fx-text-fill: black; -fx-font-family: 'Times New Roman';");
                start.setOnAction(e -> {
                    client.println("/start");
                });

                Button firstMatch = new Button("First match");
                firstMatch.setPrefSize(150, 80);
                firstMatch.setStyle("-fx-font-size: 20px; -fx-text-fill: black; -fx-font-family: 'Times New Roman';");
                firstMatch.setOnAction(e -> {
                    client.println("/firstMatch");
                });

                Button notFirstMatch = new Button("Not first match");
                notFirstMatch.setPrefSize(150, 80);
                notFirstMatch.setStyle("-fx-font-size: 20px; ; -fx-text-fill: black; -fx-font-family: 'Times New Roman';");
                notFirstMatch.setOnAction(e -> {
                    client.println("/notFirstMatch");
                });

                HBox hBox = new HBox(5);
                hBox.getChildren().addAll(start, firstMatch, notFirstMatch);
                hBox.setAlignment(Pos.CENTER);

                contentBox.getChildren().addAll(hBox);

            } else {
                Label label = new Label("Waiting for the owner...");
                label.setStyle("-fx-font-size: 20px; -fx-text-fill: black; -fx-font-family: 'Times New Roman'; -fx-background-color: white; -fx-opacity: 1;" );

                contentBox.getChildren().addAll(label);
                Screen screen = Screen.getPrimary();
                Rectangle2D bounds = screen.getVisualBounds();
                root.setPrefHeight(bounds.getHeight());
                root.setPrefWidth(bounds.getWidth());



            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        try {
            if (client.isOwner()) {

                Label chooseNumPlayer = new Label("Choose the number of players");
                chooseNumPlayer.setStyle("-fx-font-size: 20px;  -fx-text-fill: black; -fx-font-family: 'Times New Roman'; -fx-background-color: white; -fx-opacity: 1; ");
                contentBox.getChildren().add(chooseNumPlayer);

                Button num2 = new Button("2");
                num2.setPrefSize(150, 40);
                num2.setStyle("-fx-font-size: 20px;  -fx-text-fill: black; -fx-font-family: 'Times New Roman';");
                num2.setOnAction(e -> {

                    client.println("/set 2");
                });

                Button num3 = new Button("3");
                num3.setPrefSize(150, 40);
                num3.setStyle("-fx-font-size: 20px; -fx-text-fill: black; -fx-font-family: 'Times New Roman';");
                num3.setOnAction(e -> {

                    client.println("/set 3");
                });

                Button num4 = new Button("4");
                num4.setPrefSize(150, 40);
                num4.setStyle("-fx-font-size: 20px; ; -fx-text-fill: black; -fx-font-family: 'Times New Roman';");
                num4.setOnAction(e -> {

                    client.println("/set 4");
                });

                HBox hBox = new HBox(5);
                hBox.getChildren().addAll(num2, num3, num4);
                hBox.setAlignment(Pos.CENTER);

                contentBox.getChildren().addAll(hBox);
                Screen screen = Screen.getPrimary();
                Rectangle2D bounds = screen.getVisualBounds();
                root.setPrefHeight(bounds.getHeight());
                root.setPrefWidth(bounds.getWidth());
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }


        Pane namePlayerLobby = new Pane();
            VBox vBox = new VBox(5);
            namePlayerLobby.setStyle("-fx-background-color: black; -fx-opacity: 1;");
            boolean owner = true;
            for(int i=0; i<client.getBoardView().getListOfPlayer().size(); i++){
                Label myName = new Label();
                if(owner) {
                    owner = false;
                    myName.setText(client.getBoardView().getListOfPlayer().get(i).getNickname() + " (owner)");
                }else{
                    myName.setText(client.getBoardView().getListOfPlayer().get(i).getNickname());
                }
                myName.setStyle("-fx-font-size: 20px; ; -fx-text-fill: black; -fx-font-family: 'Times New Roman'; -fx-opacity: 1;");
                vBox.getChildren().add(myName);
            }
            contentBox.getChildren().addAll(vBox);
            vBox.setStyle("-fx-border-color: lightblue; -fx-border-width: 5px; -fx-background-color: white");

            Pane chat = new Pane(chat());
            chat.setStyle("-fx-alignment: center;");

            contentBox.getChildren().addAll(chat);
            Constant constant = new Constant();
            String display5 = constant.getDisplay5();
            root.setStyle("-fx-background-image: url('"+display5+"'); " + "-fx-background-size: cover; " + " -fx-background-repeat: no-repeat;");
            root.setAlignment(Pos.CENTER);

            root.add(contentBox, 2, 0);
        primaryStage.setMaximized(true);
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

    private void playAlone(AbstractClient client, Stage primaryStage) throws RemoteException {
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
            // Manage closing event
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
    public Boolean getFirstSwitch() {
        this.firstSwitch = firstSwitch;
        return firstSwitch;
    }
    public void setFirstSwitch(Boolean firstSwitch) {
        this.firstSwitch = firstSwitch;
        firstSwitch=false;
    }
    private void end(Stage primaryStage, String arg){
        GridPane root = new GridPane();
        VBox contentBox = new VBox();
        Scene sceneEnd = new Scene(root);
        BoxBlur blur = new BoxBlur(3, 4, 3);
        root.setAlignment(Pos.CENTER);
        Constant constant = new Constant();
        String display5 = constant.getDisplay5();
        root.setStyle("-fx-background-image: url('"+display5+"'); " + "-fx-background-size: cover; " + " -fx-background-repeat: no-repeat;");
        Image imageLogo = new Image("images/Publisher material/Title 2000x618px.png");
        ImageView imageViewLogo = new ImageView(imageLogo);
        imageViewLogo.setPreserveRatio(true);
        imageViewLogo.setFitWidth(500);
        imageViewLogo.setFitHeight(154);
        root.add(imageViewLogo, 0, 0);
        Label labelWinner = new Label();
        if(client.getBoardView().getWinner() == null || client.getBoardView().getWinner().getNickname() == null){
            labelWinner.setText("There's no winner");
            labelWinner.setStyle("-fx-font-size: 20px;-fx-text-fill: black; -fx-font-family: 'Times New Roman'; -fx-background-color: white;");
            labelWinner.setAlignment(Pos.CENTER);
            contentBox.getChildren().add(labelWinner);
          //  root.add(labelWinner, 0, 1);
        }else {
            labelWinner.setText(client.getBoardView().getWinner().getNickname() + " won the game!");
            labelWinner.setStyle("-fx-font-size: 20px; -fx-text-fill: black; -fx-font-family: 'Times New Roman'; -fx-background-color: white;");
            labelWinner.setAlignment(Pos.CENTER);
            contentBox.getChildren().add(labelWinner);
         //   root.add(labelWinner, 0, 1);
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

        VBox vBox = new VBox();

        for(Player p: client.getBoardView().getListOfPlayer()){
            Label label = new Label();
            label.setText(p.getNickname() + "  " + p.getScore());
            label.setStyle("-fx-font-size: 15px;  -fx-text-fill: black; -fx-font-family: 'Times New Roman';");
            label.setAlignment(Pos.CENTER);
            vBox.getChildren().add(label);


        }
        contentBox.getChildren().add(vBox);
        vBox.setStyle("-fx-background-color: white; -fx-border-color: lightblue; -fx-border-width: 2px; -fx-alignment: center; ");
        root.add(vBox, 0, 2);
        Button button = new Button("Lobby");
        button.setStyle("-fx-font-size: 20px; -fx-text-fill: black; -fx-font-family: 'Times New Roman';");
        button.setOnMouseClicked(e -> {
            client.println("lobbbb");
        });
       // root.add(button, 0, 3);
        contentBox.getChildren().add(button);
        contentBox.setAlignment(Pos.CENTER);
        root.add(contentBox, 0, 2);
        primaryStage.setScene(sceneEnd);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }
    public String getCommand(){
        return command;
    }
}
