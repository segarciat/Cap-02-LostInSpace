package com.lostinspace.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;
import com.lostinspace.model.Item;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.List;

import static com.lostinspace.util.Controller.filegetter;

class ItemModifier {
    Gson gson = new Gson();
    private String jsonFileName;
    private Item item;
    private List<Item> items;

    // CTOR
    public ItemModifier(String jsonFile) {
        setJsonFile(jsonFile);
    }
// BUSINESS METHODS

    public void setItemListFromJsonToMemory() throws IOException {
        String resource = getJsonFile();
        try (Reader reader = filegetter.getResource(resource)) {
            var itemListType = new TypeToken<List<Item>>() {
            }.getType();
            setItems(gson.fromJson(reader, itemListType));
        } catch (IOException err) {
            throw new RuntimeException(err);
        }
    }


    public void addItemToListOfItem(Item itemToAdd) {
        getItems().add(itemToAdd);
    }


    public void saveToJsonFile(String jsonDestination) throws IOException {
        FileWriter fileWriter = new FileWriter(jsonDestination, true);
        JsonWriter jsonWriter = new JsonWriter(fileWriter);
        Type listType = new TypeToken<List<Item>>() {}.getType();
        gson.toJson(getItems(), listType, jsonWriter);
    }

    // ACCESSORS
    public void setJsonFile(String fileName) {
        this.jsonFileName = fileName;
    }

    public String getJsonFile() {
        return jsonFileName;
    }

    public Item getItem() {
        return item;
    }

    private void setItem(Item item) {
        this.item = item;
    }

    public List<Item> getItems() {
        return items;
    }

    private void setItems(List<Item> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}