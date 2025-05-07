package com.lllllllhp.data;

import com.lllllllhp.model.game.MapModel;

public class MapRecord {
    private MapModel mapModel;
    private double time;

    public MapRecord(MapModel mapModel) {
        this.mapModel = mapModel;
    }

    public MapModel getMapModel() {
        return mapModel;
    }

    public void setMapModel(MapModel mapModel) {
        this.mapModel = mapModel;
    }
}
