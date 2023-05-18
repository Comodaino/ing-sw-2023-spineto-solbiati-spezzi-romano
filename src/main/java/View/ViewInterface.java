package View;

import java.io.IOException;

//TODO CHECK IF IT NEEDS TO BE OBSERVABLE
public abstract interface ViewInterface  {

    void update(String arg) throws IOException;

    public void setState(State state);
}
