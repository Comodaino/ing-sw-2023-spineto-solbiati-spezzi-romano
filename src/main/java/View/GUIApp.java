/*

package View;


import Distributed.AbstractClient;
import Distributed.ClientSocket.ClientAppSocket;
import Distributed.RemotePlayer;
import Model.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.Type;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class GUIApp extends Application implements ViewInterface{
    private AbstractClient client;
    private State state;
    private Stage primaryStage;


    public static void execute(String[] args){
        launch(args);
    }

   /* public GUIApp(AbstractClient client) throws RemoteException {
        this.client = client;
        this.state = State.HOME;
        Thread thread = new Thread() {
            @Override
            public void run() {
                start();
            }
        };
        try {
            update();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        thread.start();
    }

    */
    public void start(Stage primaryStage) throws RemoteException {
        home(primaryStage);
        play(client,primaryStage);
        primaryStage.show();
    }


    public void play(AbstractClient client,Stage primaryStage) throws RemoteException {
        GridPane mainPane = new GridPane();
        Scene scene = new Scene(mainPane);
        primaryStage.setScene(scene);

        ColumnConstraints column1 = new ColumnConstraints();
        column1.setHgrow(Priority.ALWAYS);
        RowConstraints row1 = new RowConstraints();
        row1.setVgrow(Priority.ALWAYS);
        mainPane.getColumnConstraints().add(column1);
        mainPane.getRowConstraints().add(row1);


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

        boardPane.getChildren().add(fillBoardGridPane);
        return boardPane;
    }
//    public GridPane fillBoard(AbstractClient client){
      public GridPane fillBoard(CellType cellType){
        GridPane fillBoardPane = new GridPane();
        Pane emptyComponent = new Pane();

        emptyComponent.setMinSize(50, 50);
       for (int i = 0; i < 9; i++) {
           for (int j = 0; j < 9; j++) {
              // switch (client.getBoardView().getCell(i, j).getType()){
               switch (cellType){
                   case ONE:
                       emptyComponent.setPrefSize(50, 50);
                       fillBoardPane.add(emptyComponent, i, j);
                       break;
                   case TWO:
                       if((i==2 || j==3) || (i==5 && j==8)){
                           Pane emptyComponent2 = new Pane();
                           emptyComponent2.setPrefSize(52, 52);
                           fillBoardPane.add(emptyComponent2, i, j);

                       }else {
                           fillBoardPane.add(getTile(i, j), i, j);
                       }
                       break;
                   case THREE:
                       if (client.getBoardView().getListOfPlayer().size()>2){
                           fillBoardPane.add(getTile(i,j),i,j);
                       }else {
                           emptyComponent.setPrefSize(50, 50);
                           fillBoardPane.add(emptyComponent, i, j);
                       }
                       break;
                   case FOUR:
                       if (client.getBoardView().getListOfPlayer().size()==4) {
                           fillBoardPane.add(getTile(i, j), i, j);
                       }else {
                           emptyComponent.setPrefSize(50, 50);
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

    public Button getTile(int r, int c){
        Button tileButton = new Button();
        tileButton.setMinSize(52, 52);
        if(r==5 || c==3){
            tileButton.setPrefSize(52, 52);
            Image imageTile = new Image("images/item tiles/Gatti1.3.png");
            ImageView imageView = new ImageView(imageTile);
            imageView.setPreserveRatio(true);
            imageView.setFitHeight(52);
            imageView.setFitWidth(52);
            tileButton.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
            tileButton.setGraphic(imageView);
        }else {
            Image imageTile = new Image("images/item tiles/Gatti1.2.png");
            ImageView imageView = new ImageView(imageTile);
            imageView.setPreserveRatio(true);
            imageView.setFitHeight(52);
            imageView.setFitWidth(52);
            tileButton.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
            tileButton.setGraphic(imageView);
        }

        tileButton.setOnAction(e -> {
            tileButton.setStyle("-fx-background-color: gold; -fx-border-color: gold;");
            tileButton.setGraphic(null);
        });

        return tileButton;
    }
  /*  public Button getTile(int row, int column){
        Button tileButton = new Button();
        String imageTilePath = null;
        Constant tile = null;
        switch (client.getBoardView().getTile(row, column).getColor()){
            case BLUE:
            {switch (client.getBoardView().getTile(row, column).getType()) {
                case ONE:
                    imageTilePath = tile.getConstantTile() + "Cornici.1.png";
                    break;
                case TWO:
                    imageTilePath = tile.getConstantTile() + "Cornici.2.png";
                    break;
                case THREE:
                    imageTilePath = tile.getConstantTile() + "Cornici.3.png";
                    break;
            }
            break;}
            case GREEN:
            {switch (client.getBoardView().getTile(row, column).getType()) {
                case ONE:
                    imageTilePath = tile.getConstantTile() + "Gatti1.1.png";
                    break;
                case TWO:
                    imageTilePath = tile.getConstantTile() + "Gatti1.2.png";
                    break;
                case THREE:
                    imageTilePath = tile.getConstantTile() + "Gatti1.3.png";
                    break;
            }
            break;}
            case YELLOW:
            {switch (client.getBoardView().getTile(row, column).getType()) {
                case ONE:
                    imageTilePath = tile.getConstantTile() + "Giochi1.1.png";
                    break;
                case TWO:
                    imageTilePath = tile.getConstantTile() + "Giochi1.2.png";
                    break;
                case THREE:
                    imageTilePath = tile.getConstantTile() + "Giochi1.3.png";
                    break;
            }
            break;}
            case WHITE:
            {switch (client.getBoardView().getTile(row, column).getType()) {
                case ONE:
                    imageTilePath = tile.getConstantTile() + "Libri1.1.png";
                    break;
                case TWO:
                    imageTilePath = tile.getConstantTile() + "Libri1.2.png";
                    break;
                case THREE:
                    imageTilePath = tile.getConstantTile() + "Libri1.3.png";
                    break;
            }
            break;}
            case PINK:
            {switch (client.getBoardView().getTile(row, column).getType()) {
                case ONE:
                    imageTilePath = tile.getConstantTile() + "Piante1.1.png";
                    break;
                case TWO:
                    imageTilePath = tile.getConstantTile() + "Piante1.2.png";
                    break;
                case THREE:
                    imageTilePath = tile.getConstantTile() + "Piante1.3.png";
                    break;
            }
            break;}
            case LIGHTBLUE:
            {switch (client.getBoardView().getTile(row, column).getType()) {
                case ONE:
                    imageTilePath = tile.getConstantTile() + "Trofei1.1.png";
                    break;
                case TWO:
                    imageTilePath = tile.getConstantTile() + "Trofei1.2.png";
                    break;
                case THREE:
                    imageTilePath = tile.getConstantTile() + "Trofei1.3.png";
                    break;
            }
            break;}

        }
        tileButton.setPrefSize(50, 50);
        Image imageTile = new Image(imageTilePath);
        ImageView imageView = new ImageView(imageTile);
        imageView.setPreserveRatio(true);
        imageView.setFitHeight(50);
        imageView.setFitWidth(50);
        tileButton.setGraphic(imageView);
        return tileButton;
    }
*/
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

        shelfImageView.setPreserveRatio(true);

        Image iamgeTile45 = new Image("images/item tiles/Giochi1.2.png");
        ImageView imageView45 = new ImageView(iamgeTile45);
        imageView45.setFitWidth(35);
        imageView45.setFitHeight(35);
        imageView45.setPreserveRatio(true);

        shelfGridPane.add(imageView45, 4, 0);
        shelfGridPane.setAlignment(Pos.CENTER);
       // shelfGridPane.add(imageView45, 0, 1);
       // if (client.isOwner()==true) {
            Image chairImage = new Image("images/misc/firstplayertoken.png");
            ImageView chairImageView = new ImageView(chairImage);
            chairImageView.setFitWidth(100);
            chairImageView.setFitHeight(100);
            chairImageView.setPreserveRatio(true);
        //    shelfPane.add(chairImageView, 0, 1);
       //  }
        HBox hbox = new HBox(3);
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10);

        Image personalGoalImage = createPersonalGoal(client);
        ImageView personalGoalImageView = new ImageView(personalGoalImage);
        personalGoalImageView.setFitWidth(150);
        personalGoalImageView.setFitHeight(150);
        personalGoalImageView.setPreserveRatio(true);
   //    shelfPane.add(personalGoalImageView, 1, 1);
        hbox.getChildren().addAll(chairImageView,personalGoalImageView);
        hbox.setAlignment(Pos.CENTER);
        shelfPane.add(hbox,0,1);


        return shelfPane;
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

    public void home(Stage stage){
        Image imageHome = new Image("images/Publisher material/Display_3.jpg");
        ImageView imageView = new ImageView(imageHome);
        BoxBlur blur = new BoxBlur(3, 4, 3);
        imageView.setEffect(blur);

        imageView.fitWidthProperty().bind(stage.widthProperty());
        imageView.fitHeightProperty().bind(stage.heightProperty());

        Label nickname = new Label("Enter your nickname");
        nickname.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: black; -fx-font-family: 'Comic Sans MS';");
        TextField nicknameField = new TextField();
        nicknameField.setPromptText("Enter your nickname");
        nicknameField.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: black; -fx-font-family: 'Comic Sans MS';");

        Button button = new Button("Play");
        VBox contentBox = new VBox(10);
        contentBox.getChildren().addAll(nickname, nicknameField, button);
        contentBox.setStyle("-fx-alignment: center; -fx-padding: 100px; -fx-background-color: white; -fx-opacity: 0.5;");
        final String[] nicknameString = new String[1];
        String regex = "^[a-zA-Z0-9 ]+$";


        button.setOnAction(e -> {
            String inp = nicknameField.getText();
            inp= inp.replace(" ", "");
            if (inp.length() <= 10 && inp.length() > 0 && inp.matches(regex)){
                nicknameString[0] = inp;
                nickname.setText("Your nickname is: "+nicknameString[0]);
                primaryStage.close();
                setState(State.PLAY);
                try {
                    update(nicknameString[0]);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                nicknameField.clear();
                nickname.setText("Error, retype your nickname");
                nicknameField.setPromptText("Nickname too long or empty");
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
                home(primaryStage);
                break;
            case PLAY:
                play(client,primaryStage );
                break;
           // case END:
             //   end();
               // break;
        }
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

    }

}

*/