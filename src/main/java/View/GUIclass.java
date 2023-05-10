package View;

import Distributed.RemotePlayer;
import Model.Tile;

import javax.swing.*;
import java.awt.*;

public class GUIclass {
    private RemotePlayer player;

    private State state;

    public static void Play(String[] args) {


        JFrame frameplay = new JFrame();
        JPanel board = new JPanel();
        JPanel shelf = new JPanel();
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,board,shelf);
        frameplay.add(splitPane);

        ImageIcon imageBoard



        frameplay.pack();
        frameplay.setVisible(true);
    }

    public void GUIclass(AbstractClient client){
        this.player= client.getPlayer();
        switch (this.state){
            case
        }
    }
}
