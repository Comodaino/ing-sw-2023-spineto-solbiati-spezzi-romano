package Distributed;

import java.io.Serializable;

public enum States implements Serializable {
    INIT,
    PLAY,
    WAIT,
    END,
    CLOSE,
    ERRORClientAppSocket
}
