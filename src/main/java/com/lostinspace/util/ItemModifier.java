package com.lostinspace.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lostinspace.model.Item;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

import static com.lostinspace.util.Controller.filegetter;

class ItemModifier {
    Gson gson = new Gson();
    private String jsonFileName;
    private Item item;
    private List<Item> items;


    // CTOR

    // this CTOR only requires a fileName from which JSON is expected to be pull from and automatically creates a List<Item>

    public ItemModifier(String jsonFile) {
        setJsonFile(jsonFile);
    }
// BUSINESS METHODS

    // grab the items from memory and sets it to a List<Item> in memory that is accessible from getItems
    public void setItemListFromJsonToMemory() throws IOException {
        String resource = getJsonFile();
        try (Reader reader = filegetter.getResource(resource)) {
            var itemListType = new TypeToken<List<Item>>() {
            }.getType();
            setItems(gson.fromJson(reader, itemListType));
//            return gson.fromJson(reader, itemListType);
        } catch (IOException err) {
            throw new RuntimeException(err);
        }
    }

    // TODO: write a method that allows a user to add an item to an list of items and returns that list
    public boolean addItemToItemList(Item itemToAdd) {
        return getItems().add(itemToAdd);
    }

    // TODO: write a method that packages a the list of items to a string and returns it
    public void saveToJsonFile(String jsonDestination) throws NullPointerException, IOException {
        FileSetter overwrite = new FileSetter();
        overwrite.saveToFile(gson.toJson(getItems()), jsonDestination);


    }

    // TODO: write a method that uses file setter to ship the string list of items to a JSON file

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

    public static void main(String[] args) throws IOException {
        List<String> m451Syn = List.of(new String[]{"flamethrower", "thrower", "m451", "fire"});
        List<String> m451Locations = List.of(new String[]{"Cargo Bay", "Bridge"});
        Item testItem = new Item("m451 Flamethrower", m451Syn, m451Locations, "m451 Dual-Stage Reciprocating Flamethrower", "The m451 is a state-of-the-art tool that allows stage adjustment for focused or wide area fields of coverage and oscillating muzzle modulator that delivers an even coverage across the direction of fire.", false, "The tank is depleted! You need to find more butane to use it again");
        ItemModifier itemModifier = new ItemModifier("items_v2.json");
        itemModifier.setItemListFromJsonToMemory();
//        System.out.println(itemModifier.getItems());
//        System.out.println(itemModifier.addItemToItemList(testItem));
        List<Item> testy = itemModifier.getItems();
//        System.out.println(testy.get(0).getName());
//        System.out.println(testy.get(1).getName());
        itemModifier.saveToJsonFile("inventory.json");
    }
}