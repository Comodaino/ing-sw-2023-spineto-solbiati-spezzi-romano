/*package View;

import Distributed.AbstractClient;
import Distributed.ClientSocket.ClientAppSocket;
import Distributed.RemotePlayer;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.IOException;

public class GUIclass extends JFrame implements ViewInterface{
    private RemotePlayer player;
    private AbstractClient client;
    private State state;

    public static void main (String[] args) throws IOException, InterruptedException {
        ClientAppSocket client= new ClientAppSocket(25565,"GUI");
        client.connect();
        play(client);
    }
    public static void play(AbstractClient client) {

        JFrame play = new JFrame("Play");
        play.setLocationRelativeTo(null);
        play.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,createBoard(client),createShelf(client));
        splitPane.setDividerLocation(0.5);

        panel.add(splitPane);
        play.add(panel);
        play.pack();
        play.setVisible(true);

    }
    public static JPanel createBoard(AbstractClient client){
        JPanel board = new BoardPanel();

        Border border1 = BorderFactory.createLineBorder(Color.BLACK);
        TitledBorder titledBorder1 = BorderFactory.createTitledBorder(border1, "The Board");
        board.setBorder(titledBorder1);
        board.setPreferredSize(new Dimension(750,750));

        JPanel tilePanel = new JPanel(new GridLayout(9,9));
        tilePanel.setOpaque(false);
        tilePanel.setPreferredSize(new Dimension(750,750));
        client.getBoardView();
/*
        for(int i=0; i<9;i++) {
            for (int j = 0; j < 9; j++) {

            }
        }

 */
/*
        ImageIcon  imageCat1 = new ImageIcon("View/resources/Gatti1.1.png");
        imageCat1.setImage(imageCat1.getImage().getScaledInstance(70,70,50));
        JLabel tileCat1 = new JLabel(imageCat1);
        tileCat1.setBounds(10,10,imageCat1.getIconWidth(),imageCat1.getIconHeight());
        tilePanel.add(tileCat1);



        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new OverlayLayout(mainPanel));
        mainPanel.add(board);
        mainPanel.add(tilePanel);

        board.add(tilePanel);

      //  if(boardView.getTile(0,0).getType()== TileType.ONE){

        return board;
    }
    public static JPanel createShelf(AbstractClient client){
        JPanel shelf = new SheldPanel();
        shelf.setLayout(new BorderLayout());

        Border border2 = BorderFactory.createLineBorder(Color.BLACK);
        TitledBorder titledBorder2 = BorderFactory.createTitledBorder(border2, "MyShelf");
        shelf.setBorder(titledBorder2);

        ImageIcon imageShelf = new ImageIcon("View/images/boards/bookshelf.png");
        imageShelf.setImage(imageShelf.getImage().getScaledInstance(500,500,30));

        JLabel imageOfShelf = new JLabel(imageShelf);

        shelf.add(imageOfShelf, BorderLayout.NORTH);

        if(client.getPlayer().getModelPlayer().getChair()){
            ImageIcon imageChair = new ImageIcon("View/images/misc/firstplayertoken.png");
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
        Constant c = new Constant();
        int persGoal = player1.getModelPlayer().getGoal().CreatePersonalGoal();
        switch (persGoal){

            case 0: ImageIcon imagePersGoal1 = new ImageIcon(c.getCostantPersGoal()+"12.png");
                    return imagePersGoal1;
            case 1: ImageIcon imagePersGoal2 = new ImageIcon(c.getCostantPersGoal()+"9.png");
                    return imagePersGoal2;
            case 2: ImageIcon imagePersGoal3 = new ImageIcon(c.getCostantPersGoal()+"2.png");
                    return imagePersGoal3;
            case 3: ImageIcon imagePersGoal4 = new ImageIcon(c.getCostantPersGoal()+"3.png");
                    return imagePersGoal4;
            case 4: ImageIcon imagePersGoal5 = new ImageIcon(c.getCostantPersGoal()+"4.png");
                    return imagePersGoal5;
            case 5: ImageIcon imagePersGoal6 = new ImageIcon(c.getCostantPersGoal()+"11.png");
                    return imagePersGoal6;
            case 6: ImageIcon imagePersGoal7 = new ImageIcon(c.getCostantPersGoal()+"5.png");
                    return imagePersGoal7;
            case 7: ImageIcon imagePersGoal8 = new ImageIcon(c.getCostantPersGoal()+"10.png");
                    return imagePersGoal8;
            case 8: ImageIcon imagePersGoal9 = new ImageIcon(c.getCostantPersGoal());
                    return imagePersGoal9;
            case 9: ImageIcon imagePersGoal10 = new ImageIcon(c.getCostantPersGoal()+"6.png");
                    return imagePersGoal10;
            case 10:ImageIcon imagePersGoal11 = new ImageIcon(c.getCostantPersGoal()+"8.png");
                    return imagePersGoal11;
            case 11:ImageIcon imagePersGoal12 = new ImageIcon(c.getCostantPersGoal()+"7.png");
                    return imagePersGoal12;
        }
        return null;
    }
    public static void Home(){
        JFrame home = new JFrame();
        home.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel homePanel = new JPanel();
        JLabel nickname = new Nickname();
        homePanel.setPreferredSize(new Dimension(1200,800));
        homePanel.add(nickname);
        home.add(homePanel);
        home.pack();
        home.setVisible(true);

    }
    public static void Lobby(){

    }

    public void GUIclass(AbstractClient client,State state, RemotePlayer player, Boolean chair){
        this.player= player;
        this.client = client;
        this.state = state;

    }
    public void update(String arg){

        switch (this.state){
            case HOME:{
                Home();
            }
            break;
            case PLAY: {
                play(client);
            }
            break;
            case LOBBY:{
                Lobby();
            }
            break;
        }

    }

    @Override
    public void setState(State state) {
        this.state=state;
    }
}
        */