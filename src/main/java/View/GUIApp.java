package View;


import Distributed.AbstractClient;
import Distributed.ClientSocket.ClientAppSocket;
import Distributed.RemotePlayer;
import Distributed.States;
import Model.Player;
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
import java.rmi.RemoteException;

public class GUIApp extends Application implements ViewInterface{
    private RemotePlayer player;
    private AbstractClient client;
    private State state;
    private Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) throws RemoteException {
      /*  try {
            ClientAppSocket clientSocket = new ClientAppSocket(25565, "GUI");
            clientSocket.connect();
          //  setState(State.HOME);
           // update(clientSocket.getNickname());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


       */


      //  home(primaryStage);
        play(client,primaryStage);
        primaryStage.show();
    }


    public void play(AbstractClient client,Stage primaryStage) throws RemoteException {
        BorderPane mainPane = new BorderPane();
        Scene scene = new Scene(mainPane);
        primaryStage.setScene(scene);

        Pane shelfPane = createShelf(client);

        SplitPane splitPane = new SplitPane();
        Pane boardPane = createBoard(client,splitPane);
        splitPane.getItems().addAll(boardPane,shelfPane);
        splitPane.setDividerPosition(0, 0.5);
        splitPane.setOrientation(Orientation.HORIZONTAL);
        mainPane.setCenter(splitPane);

        primaryStage.setTitle("Play");
        primaryStage.setOnCloseRequest(e -> {
            // Gestire l'evento di chiusura
        });
        primaryStage.show();
    }
    public Pane createBoard(AbstractClient client,SplitPane splitPane){
        Pane boardPane = new Pane();

        Image imageBoard = new Image("images/boards/livingroom.png");
        ImageView imageView = new ImageView(imageBoard);

        imageView.setPreserveRatio(true);
        imageView.fitWidthProperty().bind(boardPane.widthProperty());
        imageView.fitHeightProperty().bind(boardPane.heightProperty());

        boardPane.getChildren().addAll(imageView);

        boardPane.widthProperty().addListener((observable, oldValue, newValue) -> {
            double splitPaneWidth = newValue.doubleValue();
            double boardPaneWidth = splitPaneWidth / 2;
            boardPane.setPrefWidth(boardPaneWidth);
        });

        return boardPane;
    }
    public Pane createShelf(AbstractClient client) throws RemoteException {
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

        if (true) {
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
Player p = new Player("Ale",true);
     //   Player p= client.getBoardView().getListOfPlayer().get(0);
        Constant c = new Constant();
        int persGoal = p.getGoal().CreatePersonalGoal();
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

    @Override
    public void addChatMessage(String tmp) {

    }
      /*  public GUIApp(AbstractClient client, RemotePlayer player, State state) {
        this.player = player;
        this.client = client;
        this.state = state;
        }

       */



}

