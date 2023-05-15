package Distributed;

import Model.BoardView;

import java.io.IOException;

public interface AbstractClient {
    public void println(String arg) throws IOException;

    RemotePlayer getPlayer();
    BoardView getBoardView();
}
