package com.lostinspace.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.List;

public class JSONLoader {
    /**
     * Parse a JSON file in 'resources' containing an array of objects as a List of objects of that type.
     *
     * @param jsonFile A file containing an array of objects.
     * @param classObject The class object corresponding to the type T requested.
     * @param <T> The type of the elements in the list.
     * @return A list of objects of the type requested.
     */
    public static <T> List<T> loadFromJsonAsList(String jsonFile, Class<T> classObject) {
        InputStream is = classObject.getClassLoader().getResourceAsStream(jsonFile);
        try (Reader reader = new InputStreamReader(is)) {
            Type listType = TypeToken.getParameterized(List.class, classObject).getType();
            Gson gson = new Gson();
            return gson.fromJson(reader, listType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
