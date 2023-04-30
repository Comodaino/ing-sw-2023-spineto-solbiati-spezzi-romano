package Distributed.RMI.common;

import Distributed.RMI.client.Client;

import java.io.Serializable;

public interface Message extends Serializable {
    public String getContent();

    public String getAuthor();
}