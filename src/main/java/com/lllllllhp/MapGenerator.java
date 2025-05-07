package com.lllllllhp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lllllllhp.data.MapPre;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class MapGenerator {
    public static void main(String[] args) {
        String name = "map4";
        String fileName = name + ".json";

        Path path = Path.of("src/main/resources/maps", fileName);
        Gson gson = new GsonBuilder().create();

        int[][] map = new int[][]{
                {1, 1, 3, 4, 4},
                {3, 3, 3, 4, 4},
                {3, 3, 2, 2, 1},
                {0, 3, 1, 1, 3},
                {1, 3, 0, 1, 3},
        };
        int targetCol = 1;
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
