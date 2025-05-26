package com.lllllllhp.utils.socket.messageModel;

import com.lllllllhp.model.game.Time;
import com.lllllllhp.utils.socket.NetUtils;
import javafx.application.Platform;
import javafx.scene.paint.Color;

import java.time.LocalDateTime;
import static com.lllllllhp.utils.socket.NetUtils.ClientData.*;

public class Command extends Message {
    CommandType commandType;

    public enum CommandType {
        RESTART, UNDO, AI_SOLVE, RETURN
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
                    clientGame.getRootPaneController().getTips().setTextFill(Color.BLACK);
                    clientGame.getRootPaneController().getTips().setText("he use ai...");
                }
                case RETURN -> {
                    clientGame.getTimeline().stop();
                    clientGame.setTime(new Time(0));
                    clientGame.getRootPaneController().updateTimer();
                    clientGame.getGamePane().getChildren().clear();
                    clientGame.getRootPaneController().getTips().setTextFill(Color.BLACK);
                    clientGame.getRootPaneController().getTips().setText("Waiting for Player...");
                }
            }
        });
    }
}
