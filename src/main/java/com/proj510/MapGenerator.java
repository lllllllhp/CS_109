package com.proj510;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.proj510.data.MapPre;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MapGenerator {
    public static void main(String[] args) {
        String name = "哎哟我滴妈你别笑";
        String fileName = name + ".json";

        Path path = Paths.get("src/main/resources/maps", fileName);
        Gson gson = new GsonBuilder().create();

        int[][] map = new int[][]{
                {0, 0, 0, 1, 1},
                {1, 4, 4, 0, 1},
                {3, 4, 4, 1, 3},
                {3, 1, 0, 0, 3},
                {0, 1, 2, 2, 0},
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
