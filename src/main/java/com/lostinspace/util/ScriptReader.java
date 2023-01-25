package com.lostinspace.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ScriptReader {

    private String script;

    public void readScript(Path path) {
        try {
            script = Files.readString(Path.of(String.valueOf(path)));
            System.out.println(script);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}