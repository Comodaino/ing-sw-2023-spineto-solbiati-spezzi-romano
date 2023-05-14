package View;

import javax.swing.*;
import java.awt.*;

public class BoardPanel extends JPanel  {
    private ImageIcon imageBoard;

    public BoardPanel(){
        super();
        setOpaque(false);
        imageBoard = new ImageIcon("/Users/alessandraromano/Desktop/ALE/Proj Soft Eng/resources/livingroom.png");

    }
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(imageBoard.getImage(),0,0,getWidth(),getHeight(),this);
    }
}
