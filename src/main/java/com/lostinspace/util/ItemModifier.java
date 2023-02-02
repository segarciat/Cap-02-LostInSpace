package com.lostinspace.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lostinspace.model.Item;
import com.lostinspace.model.Room;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.lostinspace.util.Controller.filegetter;

class ItemModifier {
    private String fileName;
    private List<Item> items;


    // CTOR
    public ItemModifier(String fileName) {
        super();
        setJsonFile(fileName);
    }


    // BUSINESS METHODS

    // TODO: write a method that gets a list of items from a room and returns it as an ArrayList
    public List<Item> getItemsList() throws IOException {
        Gson gson = new Gson();
        String resource = getJsonFile();
        try (Reader reader = filegetter.getResource(resource)) {
            var itemListType = new TypeToken<List<Item>>(){}.getType();
            return gson.fromJson(reader, itemListType);
        } catch (IOException err) {
            throw new RuntimeException(err);
        }
    }

    // TODO: write a method that allows a user to add an item to an list of items and returns that list
    public List<Item> addItemToItemList(Item itemToAdd) {

    }



    // TODO: write a method that will allow the player to pick up and item
    public void addItemObjectToInventory(HashMap<String, Object> item) throws IOException {

    }

    // TODO: write a method that will allow the player to drop an item
    public void removeItemFromJson() {

    }

    // TODO: inspect item (in a room --> controller); maybe migrate over here?

    // ACCESSORS
    public void setJsonFile(String fileName) {
        this.fileName = fileName;
    }

    public String getJsonFile() {
        return fileName;
    }


    @Override
    public String toString() {
        return getClass().getSimpleName() + "@" + Integer.toHexString(hashCode());
    }

    public static void main(String[] args) throws IOException {
        ItemModifier itemModifier = new ItemModifier("items_v2.json");
        try {


            List<Item> itemData = itemModifier.getItemsList();
            for (Item item : itemData) {
                System.out.println(item.getFullName());
//                Item newItem = new Item(item.get("name").toString(), item.get("fullName").toString(), (Boolean) item.get("isHeld"));
//                System.out.println(newItem.getName());
//                System.out.println(newItem.getFullName());
//                System.out.println(newItem.isHeld());

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}