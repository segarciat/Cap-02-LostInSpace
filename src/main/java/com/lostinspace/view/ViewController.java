package com.lostinspace.view;

import com.lostinspace.model.*;
import com.lostinspace.util.GameEvents;
import com.lostinspace.util.JSONLoader;
import com.lostinspace.util.TextLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

class ViewController {
    // text files
    public static final String INSTRUCTIONS_TXT = "text/instructions.txt";
    public static final String TUTORIAL_TXT = "text/tutorial.txt";
    public static final String GAME_OBJECTIVES_TXT = "text/objectives.txt";
    public static final String PROLOGUE_TXT = "text/prologue.txt";

    // JSON files
    public static final String ITEM_USES_JSON = "itemUses.json";
    public static final String ITEMS_JSON = "items.json";
    public static final String HIDDEN_ITEMS_JSON = "hiddenItems.json";
    public static final String INTERACTABLES_JSON = "interactables.json";
    public static final String SHIP_ROOMS_JSON = "json/rooms_modified.json";

    // Other constants
    public static final double O_2_CONSUMED = 5.0;
    public static final double INITIAL_OXYGEN = 80.00;
    public static final String INITIAL_ROOM = "Docking Bay";
    public static boolean isEasyMode = false;

    // maps of rooms, objects
    private Map<String, Room> roomMap;                  // import instance of game map from rooms.json (game features 16 distinct areas)
    private List<Item> items;                           // import instance of list of collectable items
    private List<HiddenItem> hiddenItems;               // import instance of list of items that begin as hidden
    private List<Item> interactables;                   // import instance of list of interactable objects
    private Map<String, ItemUse> itemUses;              // map containing descriptions of item use results

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

        items = JSONLoader.loadFromJsonAsList(ITEMS_JSON, Item.class);
        hiddenItems = JSONLoader.loadFromJsonAsList(HIDDEN_ITEMS_JSON, HiddenItem.class);
        itemUses = JSONLoader.loadFromJsonAsMap(ITEM_USES_JSON, ItemUse.class);

        // Load all items that can be interacted with.
        List<Item> loadedInteractables = JSONLoader.loadFromJsonAsList(INTERACTABLES_JSON, Item.class);

        // Using the list of all items that can be interacted with, create one for each room
        interactables = new ArrayList<>();

        // Each room should have its own separate items
        for (String roomName: roomMap.keySet()) {
            List<String> roomInteractables = roomMap.get(roomName).getInteractables();
            if (roomInteractables == null)
                continue;

            for (String interactableName: roomInteractables) {
                // Find the item of matching name.
                Item item = loadedInteractables.stream().filter(i -> i.getName().equalsIgnoreCase(interactableName)).findFirst().get();

                // Make a copy of it.
                item = new Item(item);

                // Make its current room list have only the current room.
                item.setRoom(List.of(roomName));

                // Add it to the list of all interactables
                interactables.add(item);
            }
        }
    }

    public void move() {

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

    public List<Item> getItems() {
        return items;
    }

    public List<HiddenItem> getHiddenItems() {
        return hiddenItems;
    }

    public List<Item> getInteractables() {
        return interactables;
    }

    public Map<String, ItemUse> getItemUses() {
        return itemUses;
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
