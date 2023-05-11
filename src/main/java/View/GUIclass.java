package View;

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

    public static void Play() {


        JFrame play = new JFrame("Play");
        play.setLocationRelativeTo(null);
        play.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        JPanel board = new JPanel();
        JPanel shelf = new JPanel();

        Border border1 = BorderFactory.createLineBorder(Color.BLACK);
        TitledBorder titledBorder1 = BorderFactory.createTitledBorder(border1, "The Board");
        board.setBorder(titledBorder1);

        Border border2 = BorderFactory.createLineBorder(Color.BLACK);
        TitledBorder titledBorder2 = BorderFactory.createTitledBorder(border2, "MyShelf");
        shelf.setBorder(titledBorder2);

        ImageIcon imageBoard = new ImageIcon("/Users/alessandraromano/Desktop/git_tutorial/ProvaMarket/foto/livingroom.png");
        ImageIcon imageShelf = new ImageIcon("/Users/alessandraromano/Desktop/git_tutorial/ProvaMarket/foto/bookshelf.png");
        imageBoard.setImage(imageBoard.getImage().getScaledInstance(500, 500,50));
        imageShelf.setImage(imageShelf.getImage().getScaledInstance(300,300,30));

        JLabel imageOfBorard = new JLabel(imageBoard);
        JLabel imageOfShelf = new JLabel(imageShelf);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,board,shelf);
        splitPane.setDividerLocation(0.5);

        panel.add(splitPane);
        board.add(imageOfBorard);
        shelf.add(imageOfShelf);

        play.add(panel);
        play.pack();
        play.setVisible(true);

    }
    public static void Home(){

    }
    public static void Lobby(){

    }

    public void GUIclass(AbstractClient client){
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
