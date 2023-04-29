package Distributed.common;

public class ChatMessage implements Message{
    private String msg;

    public ChatMessage(String msg){
        this.msg = msg;
    }

    @Override
    public String getContent() {
        return this.msg;
    }
}
