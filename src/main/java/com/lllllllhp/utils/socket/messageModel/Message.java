package com.lllllllhp.utils.socket.messageModel;

import java.io.Serializable;
import java.time.LocalDateTime;

public abstract class Message implements Serializable {
    protected Type type;
    protected String sender;

    protected LocalDateTime dateTime;

    public enum Type {
        Chat, Move
    }

    public Message(String sender, LocalDateTime dateTime) {
        this.sender = sender;
        this.dateTime = dateTime;
    }

    public abstract void show();
}
