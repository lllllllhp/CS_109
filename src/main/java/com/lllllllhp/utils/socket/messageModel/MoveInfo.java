package com.lllllllhp.utils.socket.messageModel;

import com.lllllllhp.model.game.MovementRecord;
import com.lllllllhp.utils.socket.NetUtils;
import javafx.application.Platform;

import java.time.LocalDateTime;

import static com.lllllllhp.model.game.GameControllerModel.doMove;
import static com.lllllllhp.utils.socket.NetUtils.ClientData.*;

public class MoveInfo extends Message {
    MovementRecord movementRecord;

    public MoveInfo(MovementRecord movementRecord, String sender, LocalDateTime dateTime) {
        super(sender, dateTime);
        this.movementRecord = movementRecord;
    }

    @Override
    public void show() {
        System.out.println(movementRecord);

        Platform.runLater(() -> {
            if (doMove(clientGameCon.getMapModel(), clientGame.getBoxes().get(movementRecord.getBoxKey()), movementRecord.getRow(), movementRecord.getCol(), movementRecord.getDirection())) {
                //设置选中的box，防止异常
                clientGame.setSelectedBox(clientGame.getBoxes().get(movementRecord.getBoxKey()));
                clientGame.afterMove(movementRecord.getRow() + movementRecord.getDirection().getRow(), movementRecord.getCol() + movementRecord.getDirection().getCol(), movementRecord.getDirection());
                clientGame.getMovementStack().push(movementRecord);
            }
        });
    }
}
