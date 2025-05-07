package com.lllllllhp.utils.socket;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Message implements Serializable {
    private final Type type;
    private final String sender;
    private final String content;
    private final LocalDateTime dateTime;

    public enum Type {
        Message, Move
    }

    public Message(Type type, String sender, String content, LocalDateTime dateTime) {
        this.type = type;
        this.sender = sender;
        this.content = content;
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        switch (type) {
            case Message -> {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                return String.format("[%s] %s: %s", dateTime.format(formatter), sender, content);
            }
            case Move -> {
                return "";
            }
            default -> {
                return "Message error";
            }
        }
    }

    public Type getType() {
        return type;
    }

    public String getSender() {
        return sender;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }
}
