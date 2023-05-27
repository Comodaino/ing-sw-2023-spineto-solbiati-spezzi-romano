package View;


import Distributed.AbstractClient;
import Distributed.ClientSocket.ClientAppSocket;
import Distributed.RemotePlayer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class GUIApp extends Application implements ViewInterface{
    private RemotePlayer player;
    private AbstractClient client;
    private State state;
    private Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
       /* try {
            ClientAppSocket client = new ClientAppSocket(25565, "GUI");
            client.connect();
           // play(client, primaryStage);
            home(primaryStage);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }



        */
        primaryStage.show();
    }


    public void play(AbstractClient client,Stage primaryStage) {
        BorderPane mainPane = new BorderPane();
        Scene scene = new Scene(mainPane, 1200, 800);
        primaryStage.setScene(scene);

        SplitPane splitPane = new SplitPane();
        splitPane.setDividerPosition(0, 0.5);
        splitPane.setOrientation(Orientation.HORIZONTAL);

        Pane boardPane = createBoard(client);
        Pane shelfPane = createShelf(client);

        splitPane.getItems().addAll(boardPane, shelfPane);
        mainPane.setCenter(splitPane);

        primaryStage.setTitle("Play");
        primaryStage.setOnCloseRequest(e -> {
            // Gestire l'evento di chiusura
        });
        primaryStage.show();
    }
    public Pane createBoard(AbstractClient client){
        Pane boardPane = new Pane();
        BorderPane boardBorderPane = new BorderPane();
        Image imageBoard = new Image("images/boards/livingroom.png");
        BackgroundImage backgroundImage = new BackgroundImage(imageBoard, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
        boardBorderPane.setBackground(new Background(backgroundImage));
        boardBorderPane.setPrefSize(750,750);
        boardBorderPane.getStyleClass().add("board-pane");

        client.getBoardView();
        boardPane.getChildren().addAll(boardBorderPane);

        return boardPane;
    }
    public Pane createShelf(AbstractClient client){
        Pane shelfPane = new Pane();

        shelfPane.setPadding(new Insets(10, 10, 10, 10));
        BorderPane shelfBorderPane = new BorderPane();
        shelfBorderPane.setPrefSize(300,750);
        shelfBorderPane.getStyleClass().add("shelf-pane");
        Image imageShelf = new Image("images/boards/bookshelf.png");
        ImageView shelfImageView = new ImageView(imageShelf);
        shelfImageView.setFitWidth(300);


        shelfImageView.setPreserveRatio(true);
        shelfBorderPane.setTop(shelfImageView);
        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.setStyle("-fx-padding: 10px;");

        if (client.getPlayer().getModelPlayer().getChair()) {
            Image chairImage = new Image("images/misc/firstplayertoken.png");
            ImageView chairImageView = new ImageView(chairImage);
            chairImageView.setFitWidth(100);
            chairImageView.setPreserveRatio(true);
            vbox.getChildren().add(chairImageView);
        }

        Image personalGoalImage = createPersonalGoal(client);
        ImageView personalGoalImageView = new ImageView(personalGoalImage);
        personalGoalImageView.setFitWidth(150);
        personalGoalImageView.setPreserveRatio(true);
        vbox.getChildren().add(personalGoalImageView);

        shelfBorderPane.setRight(vbox);
        shelfPane.getChildren().add(shelfBorderPane);
        return shelfPane;
    }

    public Image createPersonalGoal(AbstractClient client) {
        RemotePlayer player1 = client.getPlayer();
        Constant c = new Constant();
        int persGoal = player1.getModelPlayer().getGoal().CreatePersonalGoal();
        String imagePath;

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
                imagePath = c.getCostantPersGoal();
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
            default:
                imagePath = "";
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

        Label nickname = new Label("Inserisci il tuo nickname");
        nickname.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: black; -fx-font-family: 'Comic Sans MS';");
        TextField nicknameField = new TextField();
        nicknameField.setPromptText("Inserisci il tuo nickname");
        nicknameField.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: black; -fx-font-family: 'Comic Sans MS';");

        Button button = new Button("Play");
        VBox contentBox = new VBox(10);
        contentBox.getChildren().addAll(nickname, nicknameField, button);
        contentBox.setStyle("-fx-alignment: center; -fx-padding: 100px; -fx-background-color: white; -fx-opacity: 0.5;");
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
        public void setState(State state) {
            this.state = state;
        }
      /*  public GUIApp(AbstractClient client, RemotePlayer player, State state) {
        this.player = player;
        this.client = client;
        this.state = state;
        }

       */



}

