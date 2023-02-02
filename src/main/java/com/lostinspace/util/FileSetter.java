package com.lostinspace.util;

import com.google.gson.Gson;
import com.lostinspace.model.Inventory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileSetter {
    private Gson gson;
    private FileGetter filegetter;
    private String resource;
    OutputStream stream = null;


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
            try {
                assert stream != null;
                stream.close();
                Reader json = filegetter.getResource(rscDestination);
                Inventory retText = gson.fromJson(json, Inventory.class);                 // Convert JSON File to Java Object
                System.out.println("RESULT: " + retText);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


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
