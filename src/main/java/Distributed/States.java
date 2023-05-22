package Distributed;

import java.io.Serializable;

public enum States implements Serializable {
    INIT,
    PLAY,
    WAIT_SETTING,
    WAIT_TURN,
    END,
    CLOSE,
    ERRORClientAppSocket
}
