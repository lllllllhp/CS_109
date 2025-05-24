package com.lllllllhp.data;

import com.lllllllhp.model.game.MapModel;
import com.lllllllhp.model.game.MovementRecord;
import com.lllllllhp.model.game.Time;

import java.io.Serializable;
import java.util.*;

public class MapRecord implements Serializable {
    private String name;
    private boolean hadSuccess = false;
    private MapModel mapModel;
    private Time time;
    private Deque<MovementRecord> recordDeque = new ArrayDeque<>();
    private Map<String, Integer> keyMap = new HashMap<>();

    public MapRecord(MapModel mapModel) {
        this.mapModel = mapModel;
        this.name = mapModel.getName();
    }

    public MapModel getMapModel() {
        return mapModel;
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

    public void setHadSuccess(boolean hadSuccess) {
        this.hadSuccess = hadSuccess;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
