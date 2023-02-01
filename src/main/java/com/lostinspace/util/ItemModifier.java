package com.lostinspace.util;

import com.google.gson.Gson;
import com.lostinspace.model.Item;
import com.lostinspace.model.Room;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class ItemModifier {
    private final FileGetter getFile = new FileGetter();
    private String fileName;
    private ArrayList<Item> items;
    private Map<String, ArrayList<Room>> map;

    // CTOR
    public ItemModifier(String fileName) {
        super();
        setFile(fileName);
    }

    public ItemModifier(String fileName, Map<String, ArrayList<Room>> map) {
        new ItemModifier(fileName);
        setMap(map);
    }

    // BUSINESS METHODS

    // TODO: write a method that gets a list of items from a room and returns it as an ArrayList
    private Map<String, ArrayList<Room>> getMapObjectFromJson(String file) {
        Gson gson = new Gson();
        map = getMap();

        try (Reader itemJson = getFile.getResource(file)) {
            map = gson.fromJson(itemJson, Map.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }
    // TODO: create an overloaded getMapObjectFromJson method that accepts a map of string : objects

    // TODO: create a method that accepts an room map, searches for items, and adds in an item obj
//    public Map<String, ArrayList<Room>> addObjectToMap(String name, String fullName, boolean isHeld) {
//        Item item = new Item(name, fullName, isHeld);
//        Map<String, ArrayList<Room>> map = getMapObjectFromJson("sampleText.json");
//        ArrayList<Room> roomArrayList = map.get("rooms");
////        System.out.println(roomArrayList);
//        for (Room room : roomArrayList) {
//            System.out.println(room);
//        }
//        return map;
//    }

    // TODO: write a method that will allow the player to pick up and item
    public void addItemObjectToInventory(HashMap<String, Object> item) throws IOException {

    }

    // TODO: write a method that will allow the player to drop an item
    public void removeItemFromJson() {

    }

    // TODO: inspect item (in a room --> controller); maybe migrate over here?

    // ACCESSORS
    public void setFile(String fileName) {
        this.fileName = fileName;
    }

    public String getJsonFile() {
        return fileName;
    }

    public Map<String, ArrayList<Room>> getMap() {
        return map;
    }

    public void setMap(Map<String, ArrayList<Room>> map) {
        this.map = map;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "@" + Integer.toHexString(hashCode());
    }

    public static void main(String[] args) throws IOException {
        ItemModifier modifier = new ItemModifier("sampleText.json");
//        modifier.addObjectToMap("cigar", "fresh cuban", true);
    }
}