package Distributed;

import Model.BoardView;

public interface AbstractClient {
    public void println(String arg);
    boolean isOwner();
    BoardView getBoardView();
    String getNickname();
}
