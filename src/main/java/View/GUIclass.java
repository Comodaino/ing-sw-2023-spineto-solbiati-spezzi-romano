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
    private AbstractClient client;
    private State state;
    private boolean chair;

    public static void Play(AbstractClient client) {

        JFrame play = new JFrame("Play");
        play.setLocationRelativeTo(null);
        play.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,createBoard(),createShelf(client));
        splitPane.setDividerLocation(0.5);

        panel.add(splitPane);
        play.add(panel);
        play.pack();
        play.setVisible(true);

    }
    public static JPanel createBoard(){
        JPanel board = new JPanel();
        // board.setLayout(new OverlayLayout(board));
        Border border1 = BorderFactory.createLineBorder(Color.BLACK);
        TitledBorder titledBorder1 = BorderFactory.createTitledBorder(border1, "The Board");
        board.setBorder(titledBorder1);

        ImageIcon imageBoard = new ImageIcon("/Users/alessandraromano/Desktop/ALE/Proj Soft Eng/resources/livingroom.png");
        imageBoard.setImage(imageBoard.getImage().getScaledInstance(750, 750,50));
        JLabel imageOfBoard = new JLabel(imageBoard);
        board.add(imageOfBoard);

       /*    DA CONTROLLARE (NON MOSTRA LA TILE CAT1)
        JPanel tilePanel = new JPanel();

        tilePanel.setOpaque(false);
        tilePanel.setLayout(null);

        ImageIcon  imageCat1 = new ImageIcon("/Users/alessandraromano/Desktop/git_tutorial/ProvaMarket/foto/Tile/Gatti1.1.png");
        imageCat1.setImage(imageCat1.getImage().getScaledInstance(50,50,50));
        JLabel tileCat1 = new JLabel(imageCat1);
        tileCat1.setBounds(10,10,imageCat1.getIconWidth(),imageCat1.getIconHeight());
        tilePanel.add(tileCat1);


        tilePanel.setBounds(0, 0, imageBoard.getIconWidth(), imageBoard.getIconHeight());
        board.add(tilePanel);
*/
        return board;
    }
    public static JPanel createShelf(AbstractClient client){
        JPanel shelf = new JPanel();
        shelf.setLayout(new BorderLayout());

        Border border2 = BorderFactory.createLineBorder(Color.BLACK);
        TitledBorder titledBorder2 = BorderFactory.createTitledBorder(border2, "MyShelf");
        shelf.setBorder(titledBorder2);

        ImageIcon imageShelf = new ImageIcon("/Users/alessandraromano/Desktop/ALE/Proj Soft Eng/resources/bookshelf.png");
        imageShelf.setImage(imageShelf.getImage().getScaledInstance(500,500,30));

        JLabel imageOfShelf = new JLabel(imageShelf);

        shelf.add(imageOfShelf, BorderLayout.NORTH);

        if(client.getPlayer().getChair == true){
            ImageIcon imageChair = new ImageIcon("/Users/alessandraromano/Desktop/ALE/Proj Soft Eng/resources/firstplayertoken.png");
            JLabel chairImage = new JLabel(imageChair);
            shelf.add(chairImage,BorderLayout.WEST);
            imageChair.setImage(imageChair.getImage().getScaledInstance(100,100,100));
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
                Play(client);
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
