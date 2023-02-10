package com.lostinspace.view;

import com.lostinspace.model.HiddenItem;
import com.lostinspace.model.Item;
import com.lostinspace.model.ItemUse;
import com.lostinspace.model.Room;
import com.lostinspace.util.TextLoader;

import java.util.List;
import java.util.Map;

class ViewController {
    // Text files.
    public static final String INSTRUCTIONS_TXT = "text/instructions.txt";
    public static final String TUTORIAL_TEXT_TXT = "text/tutorialText.txt";
    public static final String GAME_OBJECTIVES_TXT = "text/gameObjectives.txt";
    public static final String PROLOGUE_TXT = "text/prologue.txt";

    // JSON files
    public static final String ITEM_USES_JSON = "itemUses.json";
    public static final String ITEMS_JSON = "items.json";
    public static final String HIDDEN_ITEMS_JSON = "hiddenItems.json";
    public static final String INTERACTABLES_JSON = "interactables.json";
    public static final String SHIP_ROOMS_JSON = "shipRooms.json";

    private Map<String, Room> roomMap;                  // import instance of game map from shipRooms.json (game features 16 distinct areas)
    private List<Item> items;                           // import instance of list of collectable items
    private List<HiddenItem> hiddenItems;               // import instance of list of items that begin as hidden
    private List<Item> interactables;                   // import instance of list of interactable objects
    private Map<String, ItemUse> itemUses;              // map containing descriptions of item use results

    // strings containing text from files.
    private String instructions;
    private String objectives;
    private String prologue;
    private String tutorialsText;

    // returns the items list object
    public void loadGameObjects() {
//        instructions = TextLoader.loadText(INSTRUCTIONS_TXT);
//        tutorialsText = TextLoader.loadText(TUTORIAL_TEXT_TXT);
//        objectives = TextLoader.loadText(GAME_OBJECTIVES_TXT);
        prologue = TextLoader.loadText(PROLOGUE_TXT);

//        roomMap = JSONLoader.loadFromJsonAsList(SHIP_ROOMS_JSON, Room.class).stream()
//                .collect(Collectors.toMap(Room::getName, Function.identity()));
//
//        items = JSONLoader.loadFromJsonAsList(ITEMS_JSON, Item.class);
//        hiddenItems = JSONLoader.loadFromJsonAsList(HIDDEN_ITEMS_JSON, HiddenItem.class);
//        itemUses = JSONLoader.loadFromJsonAsMap(ITEM_USES_JSON, ItemUse.class);
//
//        // Load all items that can be interacted with.
//        List<Item> loadedInteractables = JSONLoader.loadFromJsonAsList(INTERACTABLES_JSON, Item.class);
//
//        // Using the list of all items that can be interacted with, create one for each room
//        interactables = new ArrayList<>();
//
//        // Each room should have its own separate items
//        for (String roomName: roomMap.keySet()) {
//            List<String> roomInteractables = roomMap.get(roomName).getInteractables();
//            if (roomInteractables == null)
//                continue;
//
//            for (String interactableName: roomInteractables) {
//                // Find the item of matching name.
//                Item item = loadedInteractables.stream().filter(i -> i.getName().equalsIgnoreCase(interactableName)).findFirst().get();
//
//                // Make a copy of it.
//                item = new Item(item);
//
//                // Make its current room list have only the current room.
//                item.setRoom(List.of(roomName));
//
//                // Add it to the list of all interactables
//                interactables.add(item);
//            }
//        }
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

    public String getTutorialsText() {
        return tutorialsText;
    }
}
