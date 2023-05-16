package Distributed;

import java.io.Serializable;

public enum States implements Serializable {
    INIT,
    PLAY,
    WAIT_SETTINGS,
    WAIT_TURN,
    END,
    CLOSE,
    ERROR
}
