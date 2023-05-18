package Distributed;

import java.io.Serializable;

public enum States implements Serializable {
    INIT,
    PLAY,
    WAIT,
    WAIT_TURN,
    END,
    CLOSE,
    ERROR
}
