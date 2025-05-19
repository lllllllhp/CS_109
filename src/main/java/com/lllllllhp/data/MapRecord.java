package com.lllllllhp.data;

import com.lllllllhp.model.game.MapModel;
import com.lllllllhp.model.game.MovementRecord;
import com.lllllllhp.model.game.Time;

import java.util.*;

public class MapRecord {
    private MapModel mapModel;
    private Time time;
    private Deque<MovementRecord> recordDeque = new ArrayDeque<>();
    private Map<String, Integer> keyMap = new HashMap<>();

    public MapRecord(MapModel mapModel) {
        this.mapModel = mapModel;
    }

    public MapModel getMapModel() {
        return mapModel;
    }

    public void setMapModel(MapModel mapModel) {
        this.mapModel = mapModel;
    }

    public Deque<MovementRecord> getRecordDeque() {
        return recordDeque;
    }

    public void setRecordDeque(Deque<MovementRecord> recordDeque) {
        this.recordDeque = recordDeque;
    }

    public Map<String, Integer> getKeyMap() {
        return keyMap;
    }

    public void setKeyMap(Map<String, Integer> keyMap) {
        this.keyMap = keyMap;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }
}
