package com.lllllllhp.utils.socket.messageModel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Chat extends Message {
    String content;

    public Chat(String sender, String content, LocalDateTime dateTime) {
        super(sender, dateTime);
        this.content = content;
        type = Type.Chat;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return String.format("[%s] %s: %s", dateTime.format(formatter), sender, content);
    }

    @Override
    public void show() {
        System.out.println(this);
    }
}
