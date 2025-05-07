package com.lllllllhp.utils.socket.messageModel;

import com.lllllllhp.model.game.MapModel;

import java.time.LocalDateTime;

public class MapInfo extends Message {
    MapModel mapModel;

    public MapInfo(String sender, MapModel mapModel, LocalDateTime dateTime) {
        super(sender, dateTime);
        this.mapModel = mapModel;
    }
}
