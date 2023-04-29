package Distributed.common;

import java.io.Serializable;

public interface Message extends Serializable {
    public String getContent();
}