package com.lllllllhp.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lllllllhp.model.game.MovementRecord;
import com.lllllllhp.utils.dataUtils.DataChecker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public class UserData {
    private boolean isMember;
    private String id;
    private String passWord;
    private int level;
    private double rating;
    private MapRecord mapRecord;
    private Map<String, MapRecord> playRecords = new HashMap<>();

    public void save() {
        Path dataPath = Path.of("src/main/resources/User", id, "userData.json");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String userData = gson.toJson(this);
        try {
            Files.createDirectories(dataPath.getParent());
            Files.writeString(dataPath, userData);
        } catch (IOException e) {
            System.out.println(e.toString());
        }
        DataChecker.createHashFile(DataChecker.toHash(this), dataPath);
        System.out.println("Saved.");
    }

    public UserData(String id, String passWord) {
        isMember = true;
        this.id = id;
        this.passWord = passWord;
        level = 1;
    }//初始化，注册新用户

    public void saveRecord(MapRecord mapRecord) {
        //保存随机地图，共用一个，加rating
        if ("random_".equals(mapRecord.getName())) {
            rating += 0.5;
            playRecords.put("random_", mapRecord);
            return;
        }
        //原来为倒序，调整为正序
        Deque<MovementRecord> deque = mapRecord.getRecordDeque();

        Deque<MovementRecord> stack = new ArrayDeque<>();
        while (!deque.isEmpty()) {
            stack.push(deque.pollFirst()); // 从头取出放到栈顶
        }
        // 再从栈中取出放回 deque
        while (!stack.isEmpty()) {
            deque.addLast(stack.pop());
        }

        //保存step最少的
        if (!playRecords.containsKey(mapRecord.getName())) {
            playRecords.put(mapRecord.getName(), mapRecord);
        } else if (playRecords.get(mapRecord.getName()).getRecordDeque().size() > mapRecord.getRecordDeque().size()) {
            playRecords.put(mapRecord.getName(), mapRecord);
        } else if (playRecords.get(mapRecord.getName()).getRecordDeque().size() == mapRecord.getRecordDeque().size()
                && playRecords.get(mapRecord.getName()).getTime().getTotal() >= mapRecord.getTime().getTotal()) {
            playRecords.put(mapRecord.getName(), mapRecord);
        }
    }

    //------------------------------------------------------------
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public MapRecord getMapRecord() {
        return mapRecord;
    }

    public void setMapRecord(MapRecord mapRecord) {
        this.mapRecord = mapRecord;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public boolean isMember() {
        return isMember;
    }

    public void setMember(boolean member) {
        isMember = member;
    }

    public Map<String, MapRecord> getPlayRecords() {
        return playRecords;
    }

    public void setPlayRecords(Map<String, MapRecord> playRecords) {
        this.playRecords = playRecords;
    }
}
