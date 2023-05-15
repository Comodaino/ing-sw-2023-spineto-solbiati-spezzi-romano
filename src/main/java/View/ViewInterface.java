package View;

import Model.BoardView;
//TODO CHECK IF IT NEEDS TO BE OBSERVABLE
public abstract interface ViewInterface  {

    public void setState(State state);
    public void setBoardView(BoardView boardView);
}
