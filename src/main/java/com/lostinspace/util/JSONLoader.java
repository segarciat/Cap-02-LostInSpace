package com.lostinspace.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lostinspace.app.App;

import java.io.*;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Utility class for loading and parsing json files.
 */
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
        Type type = TypeToken.getParameterized(List.class, classObject).getType();
        return loadFromJson(jsonFile, type);
    }

    /**
     * Parse a JSON file in 'resources' containing String keys.
     * @param jsonFile A file containing an array of objects.
     * @return A Map with String keys and values being maps
     */
    public static <T> Map<String, T> loadFromJsonAsMap(String jsonFile, Class<T> classObject) {
        Type type = TypeToken.getParameterized(Map.class, String.class, classObject).getType();
        return loadFromJson(jsonFile, type);
    }

    /**
     * Helper method for loading
     * @param jsonFile Path string of json file in the resources.
     * @param type The type of the object returned by this method.
     * @param <T> The type parameter corresponding to the type of the object.
     * @return An object loaded from JSON of type 'type' (T).
     */
    private static <T> T loadFromJson(String jsonFile, Type type) {
        try (Reader reader = new InputStreamReader(Objects.requireNonNull(App.class.getClassLoader().getResourceAsStream(jsonFile)))) {
            Gson gson = new Gson();
            return gson.fromJson(reader, type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
