package com.lllllllhp.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class UserData {
    private boolean isMember;
    private String id;
    private String passWord;
    private int level;
    private double rating;
    private MapRecord mapRecord;

    public void save() {
        Path savingPath = Path.of("src/main/resources/User", id, "userData.json");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String userData = gson.toJson(this);
        try {
            Files.createDirectories(savingPath.getParent());
            Files.writeString(savingPath, userData);
        } catch (IOException e) {
            System.out.println(e.toString());
        }
        System.out.println("Saved.");
    }

    public UserData(String id, String passWord, int level, MapRecord mapRecord, double rating) {
        isMember = true;
        this.id = id;
        this.passWord = passWord;
        this.level = level;
        this.mapRecord = mapRecord;
        this.rating = rating;
    }

    public UserData(String id, String passWord) {
        isMember = true;
        this.id = id;
        this.passWord = passWord;
        level = 1;
    }//初始化，注册新用户

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
}
