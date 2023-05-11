package View;

import Distributed.AbstractClient;
import Distributed.RemotePlayer;
import Model.Board;
import Model.Tile;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class GUIclass extends JFrame{
    private RemotePlayer player;

    private State state;
    private boolean chair;

    public static void Play() {

        JFrame play = new JFrame("Play");
        play.setLocationRelativeTo(null);
        play.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,createBoard(),createShelf());
        splitPane.setDividerLocation(0.5);

        panel.add(splitPane);
        play.add(panel);
        play.pack();
        play.setVisible(true);

    }
    public static JPanel createBoard(){
        JPanel board = new JPanel();

        Border border1 = BorderFactory.createLineBorder(Color.BLACK);
        TitledBorder titledBorder1 = BorderFactory.createTitledBorder(border1, "The Board");
        board.setBorder(titledBorder1);

        ImageIcon imageBoard = new ImageIcon("/Users/alessandraromano/Desktop/ALE/Proj Soft Eng/resources/livingroom.png");
        imageBoard.setImage(imageBoard.getImage().getScaledInstance(750, 750,50));

        JLabel imageOfBoard = new JLabel(imageBoard);
        board.add(imageOfBoard);


        return board;
    }
    public static JPanel createShelf(AbstractClient client){
        JPanel shelf = new JPanel();

        Border border2 = BorderFactory.createLineBorder(Color.BLACK);
        TitledBorder titledBorder2 = BorderFactory.createTitledBorder(border2, "MyShelf");
        shelf.setBorder(titledBorder2);

        ImageIcon imageShelf = new ImageIcon("/Users/alessandraromano/Desktop/ALE/Proj Soft Eng/resources/bookshelf.png");
        imageShelf.setImage(imageShelf.getImage().getScaledInstance(500,500,30));

        JLabel imageOfShelf = new JLabel(imageShelf);

        shelf.add(imageOfShelf);

        if(client.getPlayer().getChair == true){
            ImageIcon imageChair = new ImageIcon("/Users/alessandraromano/Desktop/ALE/Proj Soft Eng/resources/firstplayertoken.png");
            JLabel chairImage = new JLabel(imageChair);
            shelf.add(chairImage);
        }

        return shelf;
    }
    public static void Home(){
        JFrame home = new JFrame();
        home.setLayout(new BorderLayout());
        // TODO aggiungere immagine logo a NORTH
        // TODO aggiungere pulsante start a SOUTH
        // TODO sistemare update nel caso in cui il  nickname è sbagliato


    }
    public static void Lobby(){

    }

    public void GUIclass(AbstractClient client){;
        this.player= client.getPlayer();
        switch (this.state){
            case HOME:{
                Home();
            }
                break;
            case PLAY: {
                Play();
            }
                break;
            case LOBBY:{
                Lobby();
            }
                break;
        }
    }
    public void update(String arg, Board board){



    }
}
