package View;

import Model.BoardView;

import java.io.IOException;
import java.util.Observable;

public abstract class ViewInterface extends Observable {
    private State state;
    private BoardView boardView;
    public void setState(State state){
        this.state = state;
    }
    public void setBoardView(BoardView boardView) {
        this.boardView = boardView;
    }

    public abstract void update(String arg) throws IOException;
}
