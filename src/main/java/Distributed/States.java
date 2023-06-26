package Distributed;

import java.io.Serializable;

/**
 * ConnectionType is an enumeration for the states a Client could be in: INIT, PLAY, WAIT, END, CLOSE, ERRORClientAppSocket.
 * @author Nicolò
 */
public enum States implements Serializable {
    INIT,
    PLAY,
    WAIT,
    END,
    CLOSE,
    ERRORClientAppSocket
}
