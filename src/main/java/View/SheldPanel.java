package View;

import javax.swing.*;
import java.awt.*;

public class SheldPanel extends JPanel {
    private ImageIcon backgroundImage;
    public SheldPanel(){
        super();
        setOpaque(false);
        backgroundImage = new ImageIcon("/Users/alessandraromano/Desktop/ALE/Proj Soft Eng/resources/sfondo parquet.jpg");
    }
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(backgroundImage.getImage(),0,0,getWidth(),getHeight(),this);
    }

}
