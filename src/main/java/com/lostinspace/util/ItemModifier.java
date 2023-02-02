package com.lostinspace.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lostinspace.model.Item;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

import static com.lostinspace.util.Controller.filegetter;

class ItemModifier {
    private String fileName;
    private Item item;
    private List<Item> items;


    // CTOR

    // this CTOR only requires a fileName from which JSON is expected to be pull from and automatically creates a List<Item>
    public ItemModifier(String fileName)  {
        super();
        setJsonFile(fileName);
    }

    public ItemModifier(String fileName, Item item) {
        new ItemModifier(fileName);
        setItem(item);
    }
// BUSINESS METHODS

    // grab the items from memory and sets it to a List<Item> in memory that is accessible from getItems
    public List<Item> setItemListFromJsonToMemory() throws IOException {
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
    public List<Item> addItemToItemList(Item itemToAdd) throws IOException {
        try {
            setItems(setItemListFromJsonToMemory());
            // add an item from the argument provided
            getItems().add(itemToAdd);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // returns the List<Item> to be packaged back up and returned to the JSON
            return getItems();
    }

    // ACCESSORS
    public void setJsonFile(String fileName) {
        this.fileName = fileName;
    }

    public String getJsonFile() {
        return fileName;
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

    public static void main(String[] args) throws IOException {
        ItemModifier itemModifier = new ItemModifier("items_v2.json");
        try {
            List<String> m451Syn = List.of(new String[]{"flamethrower", "thrower", "m451", "fire"});
            List<String> m451Locations = List.of(new String[]{"Cargo Bay", "Bridge"});
            Item testItem = new Item("m451 Flamethrower", m451Syn, m451Locations , "m451 Dual-Stage Reciprocating Flamethrower", "The m451 is a state-of-the-art tool that allows stage adjustment for focused or wide area fields of coverage and oscillating muzzle modulator that delivers an even coverage across the direction of fire.", false, "The tank is depleted! You need to find more butane to use it again");

        itemModifier.addItemToItemList(testItem);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}