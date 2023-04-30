package Distributed.RMI.common;

import Distributed.RMI.client.Client;

public class ChatMessage implements Message{
    private String msg;
    private String author;

    public ChatMessage(String msg, String author){
        this.msg = msg;
        this.author = author;
    }

    @Override
    public String getContent() {
        return this.msg;
    }
    @Override
    public String getAuthor() {
        return this.author;
    }
}
