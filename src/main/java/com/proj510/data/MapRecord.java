package com.proj510.data;

import com.proj510.model.game.MapModel;
import com.proj510.model.game.MovementRecord;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

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
