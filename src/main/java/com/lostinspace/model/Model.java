package com.lostinspace.model;

import com.lostinspace.util.ImageLoader;
import com.lostinspace.util.JSONLoader;
import com.lostinspace.util.TMXLoader;
import com.lostinspace.util.TextLoader;
import com.lostinspace.view.RoomPanel;
import com.lostinspace.view.SwingComponentCreator;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Model {
    // text files
    public static final String INSTRUCTIONS_TXT = "text/instructions.txt";
    public static final String GAME_OBJECTIVES_TXT = "text/objectives.txt";
    public static final String PROLOGUE_TXT = "text/prologue.txt";

    // JSON files
    public static final String ITEMS_JSON = "json/items_modified.json";
    public static final String SHIP_ROOMS_JSON = "json/rooms_modified.json";

    // maps of rooms, objects
    private final Map<String, Room> rooms;
    private final Map<String, ItemMod> items;
    private final Map<String, Set<ItemMod>> roomItems;
    private final Map<String, Image> roomImages;
    private final Map<String, Map<String, Rectangle>> roomItemRectangles;

    // strings containing text from files
    private final String instructions;
    private final String objectives;
    private final String prologue;
    private final String tutorial;

    // Player class initial parameters
    private final Player player;

    // CWO2 class
    private final Officer officerZhang;

    /**
     * Data class with all the application state; it loads the data and provides access to it.
     */
    public Model() {
        // Load all text.
        instructions = TextLoader.loadText(INSTRUCTIONS_TXT);
        tutorial = TextLoader.loadText(INSTRUCTIONS_TXT);
        objectives = TextLoader.loadText(GAME_OBJECTIVES_TXT);
        prologue = TextLoader.loadText(PROLOGUE_TXT);

        // Build all objects from JSON
        rooms = JSONLoader.loadFromJsonAsList(SHIP_ROOMS_JSON, Room.class).stream()
                .collect(Collectors.toMap(Room::getName, Function.identity()));

        items = JSONLoader.loadFromJsonAsList(ITEMS_JSON, ItemMod.class).stream()
                .collect(Collectors.toMap(ItemMod::getName, Function.identity()));


        // Key: Room name. Value: A set of items for that room

        roomItems = rooms.values().stream().collect(
                Collectors.toMap(Room::getName, room -> room.getItems().stream()
                        .map(itemName -> new ItemMod(items.get(itemName))).collect(Collectors.toSet()))
        );

        // Load images for each room.
        roomImages = rooms.values().stream()
                .collect(Collectors.toMap(Room::getName, room -> ImageLoader.loadImage(room.getImage())));

        roomItemRectangles = rooms.values().stream()
                .collect(Collectors.toMap(Room::getName, TMXLoader::loadRoomItemRectangles));

        for (String roomName: roomItems.keySet()) {
            roomItems.get(roomName).forEach(item -> item.setRectangle(roomItemRectangles.get(roomName).get(item.getName())));
        }

        this.player = new Player("", 0, new ArrayList<>(0));
        this.officerZhang = new Officer(new ArrayList<>());
    }

    /*
     * CHECK METHODS
     */
    // Check if item is in inventory
    public boolean inventoryContains(String itemName) {
        boolean isFound = false;

        for (ItemMod itemInInventory : player.getInventory()) {
            if (itemInInventory.getName().equals(itemName)) {
                isFound = true;
                break;
            }
        }

        return isFound;
    }

    /**
     * Returns an ItemMod item from the inventory
     * @param itemName String itemName of the item you want to retrieve from the inventory
     * @return ItemMod in the player's inventory, or null if not found.
     */
    public ItemMod returnItemFromInventory(String itemName) {
        return player.getInventory().stream()
                .filter(item -> item.getName().equals(itemName)).findFirst()
                .orElse(null);
    }

    /*
     * GETTER OBJECT BY NAME
     */
    public Room getRoomByName(String roomName) {
        Room room = new Room();

        for (String roomString : rooms.keySet()) {
            if (roomString.equals(roomName)) {
                room = rooms.get(roomString);
            }
        }

        return room;
    }

    public ItemMod getItemByName(String itemName) {
        ItemMod item = new ItemMod();

        for (String itemString : items.keySet()) {
            if (itemString.equals(itemName)) {
                item = items.get(itemString);
            }
        }

        return item;
    }

    public ItemMod getHiddenItemByName(String hiddenItemName, String roomName) {
        Set<ItemMod> itemMods = getRoomItems().get(roomName);

        ItemMod hiddenItem = new ItemMod();

        for (ItemMod itemMod : itemMods) {
            if (itemMod.getName().equals(hiddenItemName)) {
                hiddenItem = itemMod;
            }
        }

        return hiddenItem;
    }

    /*
     * ACCESSOR METHODS
     */

    /**
     *
     * @return The player object of the game.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     *
     * @return The trusty officer that is trying to fix the ship to get us back in orbit.
     */
    public Officer getOfficerZhang() {
        return officerZhang;
    }

    /**
     * Key-value pairs. The key is the room's name, the value is a map of of rectangles, with the keys being the name
     * of the items in the room.
     * @return Map of rectangles for each of the objects in that room.
     */
    public Map<String, Map<String, Rectangle>> getRoomItemRectangles() {
        return Collections.unmodifiableMap(roomItemRectangles);
    }

    /**
     * Key-value pairs, key is the room's name, and value is a set of items for that room.
     *
     * @return View of the items in each of the rooms
     */
    public Map<String, Set<ItemMod>> getRoomItems() {
        return Collections.unmodifiableMap(roomItems);
    }

    /**
     * Key-value pairs, key is the room's name, and value is the room's image.
     * @return View of images for the rooms.
     */
    public Map<String, Image> getRoomImages() {
        return Collections.unmodifiableMap(roomImages);
    }

    /**
     * Key-value pairs, key is the rooms name, and value is the room object.
     * @return View of all rooms in the game.
     */
    public Map<String, Room> getRooms() {
        return Collections.unmodifiableMap(rooms);
    }

    /**
     * Key-value pairs, key is the item's name, and value is the item object.
     * @return View of all items available in the game.
     */
    public Map<String, ItemMod> getItems() {
        return Collections.unmodifiableMap(items);
    }

    /**
     *
     * @return Instructions on how to play the game.
     */
    public String getInstructions() {
        return instructions;
    }

    /**
     *
     * @return Text with the objective of the game.
     */
    public String getObjectives() {
        return objectives;
    }

    /**
     *
     * @return Text for the game's prologue.
     */
    public String getPrologue() {
        return prologue;
    }

    /**
     *
     * @return Text teaching player how to play the game.
     */
    public String getTutorial() {
        return tutorial;
    }
}
