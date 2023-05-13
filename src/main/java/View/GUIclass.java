package View;

import Distributed.AbstractClient;
import Distributed.RemotePlayer;
import Model.Board;
import Model.Player;
import Model.Tile;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.Random;

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

       //    DA CONTROLLARE (NON MOSTRA LA TILE CAT1)
        JPanel tilePanel = new JPanel();

        tilePanel.setOpaque(false);
  //      tilePanel.setLayout(null);

        ImageIcon  imageCat1 = new ImageIcon("/Users/alessandraromano/Desktop/git_tutorial/ProvaMarket/foto/Tile/Gatti1.1.png");
      //  imageCat1.setImage(imageCat1.getImage().getScaledInstance(50,50,50));
        JLabel tileCat1 = new JLabel(imageCat1);
        tileCat1.setBounds(10,10,imageCat1.getIconWidth(),imageCat1.getIconHeight());
        tilePanel.add(tileCat1);


    //    tilePanel.setBounds(0, 0, imageBoard.getIconWidth(), imageBoard.getIconHeight());
        board.add(tilePanel);

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

        if(client.getPlayer().getModelPlayer().getChair() == true){
            ImageIcon imageChair = new ImageIcon("/Users/alessandraromano/Desktop/ALE/Proj Soft Eng/resources/firstplayertoken.png");
            JLabel chairImage = new JLabel(imageChair);
            shelf.add(chairImage,BorderLayout.WEST);
            imageChair.setImage(imageChair.getImage().getScaledInstance(100,100,100));
        }
        ImageIcon imageIcon = new ImageIcon(createPersonalGoal(client).getImage());
        JLabel personalGoal = new JLabel(imageIcon);
        imageIcon.setImage(imageIcon.getImage().getScaledInstance(150,200,100));


        shelf.add(personalGoal,BorderLayout.EAST);

        return shelf;
    }
    public static ImageIcon createPersonalGoal(AbstractClient client){
        RemotePlayer player1 = client.getPlayer();
        int persGoal = player1.getModelPlayer().getGoal().CreatePersonalGoal();
    //    ImageIcon imagePersGoal = new ImageIcon();
        switch (persGoal){

            case 0: ImageIcon imagePersGoal1 = new ImageIcon("/Users/alessandraromano/Desktop/ALE/Proj Soft Eng/resources/Personal Goal /Personal_Goals12.png");
                    return imagePersGoal1;
            case 1: ImageIcon imagePersGoal2 = new ImageIcon("/Users/alessandraromano/Desktop/ALE/Proj Soft Eng/resources/Personal Goal /Personal_Goals9.png");
                    return imagePersGoal2;
            case 2: ImageIcon imagePersGoal3 = new ImageIcon("/Users/alessandraromano/Desktop/ALE/Proj Soft Eng/resources/Personal Goal /Personal_Goals2.png");
                    return imagePersGoal3;
            case 3: ImageIcon imagePersGoal4 = new ImageIcon("/Users/alessandraromano/Desktop/ALE/Proj Soft Eng/resources/Personal Goal /Personal_Goals3.png");
                    return imagePersGoal4;
            case 4: ImageIcon imagePersGoal5 = new ImageIcon("/Users/alessandraromano/Desktop/ALE/Proj Soft Eng/resources/Personal Goal /Personal_Goals4.png");
                    return imagePersGoal5;
            case 5: ImageIcon imagePersGoal6 = new ImageIcon("/Users/alessandraromano/Desktop/ALE/Proj Soft Eng/resources/Personal Goal /Personal_Goals11.png");
                    return imagePersGoal6;
            case 6: ImageIcon imagePersGoal7 = new ImageIcon("/Users/alessandraromano/Desktop/ALE/Proj Soft Eng/resources/Personal Goal /Personal_Goals5.png");
                    return imagePersGoal7;
            case 7: ImageIcon imagePersGoal8 = new ImageIcon("/Users/alessandraromano/Desktop/ALE/Proj Soft Eng/resources/Personal Goal /Personal_Goals10.png");
                    return imagePersGoal8;
            case 8: ImageIcon imagePersGoal9 = new ImageIcon("/Users/alessandraromano/Desktop/ALE/Proj Soft Eng/resources/Personal Goal /Personal_Goals.png");
                    return imagePersGoal9;
            case 9: ImageIcon imagePersGoal10 = new ImageIcon("/Users/alessandraromano/Desktop/ALE/Proj Soft Eng/resources/Personal Goal /Personal_Goals6.png");
                    return imagePersGoal10;
            case 10:ImageIcon imagePersGoal11 = new ImageIcon("/Users/alessandraromano/Desktop/ALE/Proj Soft Eng/resources/Personal Goal /Personal_Goals8.png");
                    return imagePersGoal11;
            case 11:ImageIcon imagePersGoal12 = new ImageIcon("/Users/alessandraromano/Desktop/ALE/Proj Soft Eng/resources/Personal Goal /Personal_Goals7.png");
                    return imagePersGoal12;
        }
        return null;
        //imagePersGoal.setImage(imagePersoGoal.getImage().getScaledInstance(100,100,100));
       // JLabel imageOfPersGoal = new JLabel();
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
