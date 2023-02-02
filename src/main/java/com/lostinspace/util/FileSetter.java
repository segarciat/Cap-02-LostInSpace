package com.lostinspace.util;

import com.google.gson.Gson;
import com.lostinspace.model.Inventory;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileSetter {
    private Gson gson;
    private FileGetter filegetter;
    private String resource;
    OutputStream stream;


    public FileSetter() {
        setGson();
        setFilegetter();
    }

    public void saveToFile(String dataToAdd, String rscDestination) throws IOException{

        try {
            Path source = Paths.get(getClass().getResource(rscDestination).getPath());
            stream = new FileOutputStream(String.valueOf(source));
            stream.write(dataToAdd.getBytes(), 0, dataToAdd.length());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
                assert stream != null;
                stream.close();
                Reader json = filegetter.getResource(rscDestination);
                Inventory retText = gson.fromJson(json, Inventory.class);                 // Convert JSON File to Java Object
                System.out.println("RESULT: " + retText);

        }
    }


//    public void saveJsonToFile(String dataToAdd, String rscDestination) throws IOException {
//        Path source = Paths.get(getClass().getResource(rscDestination).getPath());
//        FileWriter file = new FileWriter("inventory.json");
//        file.write(dataToAdd);
//        file.close();
//        try (FileWriter file = new FileWriter(rscDestination)) {
//            Writer bufferedWriter = new BufferedWriter(new OutputStreamWriter(System.out));
//        }
//    }

    public Gson getGson() {
        return gson;
    }

    private void setGson() {
        this.gson = new Gson();
    }

    public FileGetter getFilegetter() {
        return filegetter;
    }

    private void setFilegetter() {
        this.filegetter = new FileGetter();
    }

    public String resource() {
        return resource;
    }

    private void setResource(String resource) {
        this.resource = resource;
    }

}
