package main.java.com.jaehyeoklim.tcp.chat.domain;

import java.util.Date;
import java.util.UUID;

public class Message {

    private final UUID id;
    private final String sender;
    private final String content;
    private final Date timestamp;

    public Message(UUID id, String sender, String content, Date timestamp) {
        this.id = id;
        this.sender = sender;
        this.content = content;
        this.timestamp = timestamp;
    }

    public UUID getId() {
        return id;
    }

    public String getSender() {
        return sender;
    }

    public String getContent() {
        return content;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return String.format("Message[ID=%s, sender=%s, content=%s, timestamp=%s]",
                id, sender, content, timestamp);
    }
}
