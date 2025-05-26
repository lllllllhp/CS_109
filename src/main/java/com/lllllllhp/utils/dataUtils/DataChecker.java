package com.lllllllhp.utils.dataUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lllllllhp.data.UserData;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DataChecker {
    public static String toHash(UserData userData) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        byte[] hashBytes;
        StringBuilder hashCode = new StringBuilder();

        String data = gson.toJson(userData);

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            //UTF-8编码
            hashBytes = md.digest(data.getBytes(StandardCharsets.UTF_8));

            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hashCode.append("0");
                hashCode.append(hex);
            }
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e.toString());
        }
        return hashCode.toString();
    }

    public static boolean checkData(UserData userData, Path dataPath) {
        Path hashPath = dataPath.getParent().resolve("hash");
        String originHash = "";
        String newHash;
        try {
            originHash = Files.readString(hashPath);
        } catch (IOException e) {
            System.out.println("can't find .hash");
            System.out.println(e.toString());
        }
        newHash = toHash(userData);

        return (originHash.equals(newHash));
    }

    public static void createHashFile(String hashCode, Path dataPath) {
        Path hashPath = dataPath.getParent().resolve("hash");
        try {
            Files.writeString(hashPath, hashCode);
        } catch (IOException e) {
            System.out.println("create hash fail");
            System.out.println(e.toString());
        }
    }
}
