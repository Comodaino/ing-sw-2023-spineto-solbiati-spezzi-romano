package Model;

import java.io.Serializable;

/**
 * represents a private message between two users with attribute sender, recipient, and content
 * @author Alessio
 */
public class Whisper implements Serializable {
    String sender;
    String recipient;
    String content;

    public Whisper(String sender, String recipient, String content){
        this.sender = sender;
        this.recipient =recipient;
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getSender() {
        return sender;
    }
}
