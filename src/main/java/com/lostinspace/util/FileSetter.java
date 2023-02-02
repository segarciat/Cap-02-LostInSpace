package com.lostinspace.util;

import com.google.gson.Gson;
import com.lostinspace.model.Inventory;
import com.lostinspace.model.RoomsRoot;


import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileSetter {
    static Gson gson = new Gson();
    static FileGetter filegetter = new FileGetter();

    public  void saveToFile(String data) {
        OutputStream os = null;
        try {
            Path source = Paths.get(getClass().getResource("/inventory.json").getPath());
            os = new FileOutputStream(String.valueOf(source));
            os.write(data.getBytes(), 0, data.length());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                os.close();
                Reader json = filegetter.getResource("inventory.json");
                Inventory retText = gson.fromJson(json, Inventory.class);                 // Convert JSON File to Java Object
                System.out.println("RESULT: " + retText);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
