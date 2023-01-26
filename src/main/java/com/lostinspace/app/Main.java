package com.lostinspace.app;


import com.lostinspace.util.ScriptReader;

import java.nio.file.Path;

public class Main {

    public static void main(String[] args) {
        Path title = Path.of("C:\\Practical Applications\\projects\\lost-in-space\\data\\scripts\\","title.txt");
        ScriptReader scriptReader = new ScriptReader();
        scriptReader.readScript(title);
    }
}