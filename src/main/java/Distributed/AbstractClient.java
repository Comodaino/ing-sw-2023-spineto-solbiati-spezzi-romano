package Distributed;

import Model.BoardView;

public interface AbstractClient {
    public void println(String arg);

    RemotePlayer getPlayer();
    boolean isOwner();
    BoardView getBoardView();
}
