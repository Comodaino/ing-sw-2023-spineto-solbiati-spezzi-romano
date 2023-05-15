package Distributed;

import Model.BoardView;

public interface AbstractClient {
    public void println(String arg);

    RemotePlayer getPlayer();

    BoardView getBoardView();
}
