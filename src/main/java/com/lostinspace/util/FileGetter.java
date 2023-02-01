package com.lostinspace.util;


import java.lang.*;
import java.io.*;

public class FileGetter {
    public Reader getResource(String rsc) {
        //noinspection ConstantConditions
        // gets a resource and returns it as an InputStreamReader
        return new InputStreamReader(getClass().getClassLoader().getResourceAsStream(rsc));
    }
}