package com.lllllllhp.utils.socket.messageModel;

import com.lllllllhp.utils.socket.NetUtils;
import javafx.application.Platform;

import java.time.LocalDateTime;
import static com.lllllllhp.utils.socket.NetUtils.ClientData.*;

public class Command extends Message {
    CommandType commandType;

    public enum CommandType {
        RESTART, UNDO, AI_SOLVE
    }

    public Command(CommandType commandType, String sender, LocalDateTime dateTime) {
        super(sender, dateTime);
        this.commandType = commandType;
    }

    @Override
    public void show() {
        Platform.runLater(() -> {
            switch (commandType) {
                case RESTART -> clientGameCon.restart();
                case UNDO -> clientGameCon.undo();
                case AI_SOLVE -> {
                    System.out.println("he use ai...");
                }
            }
        });
    }
}
