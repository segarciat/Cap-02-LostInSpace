package com.lostinspace.controller;

import com.lostinspace.model.*;
import com.lostinspace.util.JSONLoader;
import com.lostinspace.util.TextLoader;
import com.lostinspace.model.ItemMod;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ViewController {
    // text files
    public static final String INSTRUCTIONS_TXT = "text/instructions.txt";
    public static final String TUTORIAL_TXT = "text/tutorial.txt";
    public static final String GAME_OBJECTIVES_TXT = "text/objectives.txt";
    public static final String PROLOGUE_TXT = "text/prologue.txt";

    // JSON files
    public static final String ITEMS_JSON = "json/items_modified.json";
    public static final String SHIP_ROOMS_JSON = "json/rooms_modified.json";

    // Other constants
    public static final double O_2_CONSUMED = 5.0;
    public static final double INITIAL_OXYGEN = 80.00;
    public static final String INITIAL_ROOM = "Docking Bay";
    public static boolean isEasyMode = false;

    // maps of rooms, objects
    private Map<String, Room> roomMap;                     // import instance of game map from rooms.json (game features 16 distinct areas)
    private List<ItemMod> items;                           // import instance of list of collectable items

    // strings containing text from files
    private String instructions;
    private String objectives;
    private String prologue;
    private String tutorial;

    // create player
    private final Player player = new Player(INITIAL_ROOM, INITIAL_OXYGEN);
    private List<Item> inventory = new ArrayList<>();  // player inventory, which is initially empty

    public ViewController() {
        loadGameObjects();
    }

    // returns the items list object
    public void loadGameObjects() {
        instructions = TextLoader.loadText(INSTRUCTIONS_TXT);
        tutorial = TextLoader.loadText(TUTORIAL_TXT);
        objectives = TextLoader.loadText(GAME_OBJECTIVES_TXT);
        prologue = TextLoader.loadText(PROLOGUE_TXT);

        roomMap = JSONLoader.loadFromJsonAsList(SHIP_ROOMS_JSON, Room.class).stream()
                .collect(Collectors.toMap(Room::getName, Function.identity()));

        items = JSONLoader.loadFromJsonAsList(ITEMS_JSON, ItemMod.class);
    }

    public void test() {
        for (ItemMod item : items) {
            System.out.println(item.getName());
        }
    }

    /*
     * ACCESSOR METHODS
     */

    public Player getPlayer() {
        return player;
    }

    public Map<String, Room> getRoomMap() {
        return roomMap;
    }

    public List<ItemMod> getItems() {
        return items;
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
