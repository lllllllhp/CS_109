package com.lllllllhp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lllllllhp.data.MapPre;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class MapGenerator {
    public static void main(String[] args) {
        String name = "www";
        String fileName = name + ".json";

        Path path = Path.of("src/main/resources/maps", fileName);
        Gson gson = new GsonBuilder().create();

        int[][] map = new int[][]{
                {4, 4, 0, 0, 0, 0, 0, 0, 0},
                {4, 4, 0, 2, 2, 0, 1, 0, 0},
                {0, 0, 0, 0, 0, 1, 0, 0, 0},
                {0, 3, 0, 0, 0, 0, 0, 0, 0},
                {0, 3, 0, 0, 0, 0, 0, 0, 0},
        };
        int targetCol = 7;
        int targetRow = 3;

        MapPre mapPre = new MapPre(name, map, targetCol, targetRow);

        String json = gson.toJson(mapPre);
        try {
            Files.writeString(path, json);
            System.out.println("写入成功");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
