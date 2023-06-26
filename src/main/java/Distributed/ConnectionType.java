package Distributed;

import java.io.Serializable;

/**
 * ConnectionType is an enumeration for the type of connection: SOCKET or RMI.
 * @author Nicolò
 */
public enum ConnectionType implements Serializable {
    SOCKET,
    RMI
}