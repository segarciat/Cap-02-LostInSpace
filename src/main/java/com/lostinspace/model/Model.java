package com.lostinspace.model;

import com.lostinspace.util.ImageLoader;
import com.lostinspace.util.JSONLoader;
import com.lostinspace.util.TMXLoader;
import com.lostinspace.util.TextLoader;

import java.awt.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Model {
    // text files
    public static final String INSTRUCTIONS_TXT = "text/instructions.txt";
    public static final String TUTORIAL_TXT = "text/tutorial.txt";
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

    private final Player player;

    public Model() {
        // Load all text.
        instructions = TextLoader.loadText(INSTRUCTIONS_TXT);
        tutorial = TextLoader.loadText(TUTORIAL_TXT);
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
        System.out.println(roomItems.values().stream().mapToInt(Set::size).sum());


        // Load images for each room.
        roomImages = rooms.values().stream()
                .collect(Collectors.toMap(Room::getName, room -> ImageLoader.loadImage(room.getImage())));

        roomItemRectangles = rooms.values().stream()
                .collect(Collectors.toMap(Room::getName, TMXLoader::loadRoomItemRectangles));

        player = new Player("", 0.0);
    }

    /*
     * ACCESSOR METHODS
     */

    public Player getPlayer() {
        return player;
    }

    public Map<String, Map<String, Rectangle>> getRoomItemRectangles() {
        return Collections.unmodifiableMap(roomItemRectangles);
    }

    public Map<String, Set<ItemMod>> getRoomItems() {
        return Collections.unmodifiableMap(roomItems);
    }

    public Map<String, Image> getRoomImages() {
        return Collections.unmodifiableMap(roomImages);
    }

    public Map<String, Room> getRooms() {
        return Collections.unmodifiableMap(rooms);
    }

    public Map<String, ItemMod> getItems() {
        return Collections.unmodifiableMap(items);
    }

    public String getInstructions() {
        return instructions;
    }

    public String getObjectives() {
        return objectives;
    }

    public String getPrologue() {
        return prologue;
    }

    public String getTutorial() {
        return tutorial;
    }
}
