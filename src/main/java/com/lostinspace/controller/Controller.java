package com.lostinspace.controller;

/*
 * Player Controller Class |
 * The player controller script for text adventure Lost in Space.
 * Handles all player commands and their feedback.
 * Handles loading game map into memory.
 */

import com.lostinspace.app.App;
import com.lostinspace.model.*;
import com.lostinspace.util.*;


import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.util.*;


/*
 * handles user commands and their feedback
 * stores inventory data
 * loads game text/dialogue
 */
public class Controller {
    public static final String INSTRUCTIONS_TXT = "instructions.txt";
    public static final String ITEMUSES_JSON = "itemuses.json";
    private static final String os = System.getProperty("os.name").toLowerCase(); // identify operating system of user
    public static final String TUTORIAL_TEXT_TXT = "tutorialText.txt";
    public static final String GAMEOBJECTIVES_TXT = "gameobjectives.txt";
    public static final String WELCOME_TXT = "welcome.txt";
    public static final String PROLOGUE_TXT = "prologue.txt";
    public static final String ITEMS_JSON = "items.json";
    public static final String HIDDENITEMS_JSON = "hiddenitems.json";
    public static final String INTERACTABLES_JSON = "interactables.json";
    public static final String SHIPROOMS_JSON = "shiprooms.json";
    GameEvents events = new GameEvents();           // ref to Game Event Methods

    private List<Room> roomsList;                      // import instance of game map from shipRooms.json (game features 16 distinct areas)
    private List<Item> items;                          // import instance of list of collectable items
    private List<HiddenItem> hiddenItems;              // import instance of list of items that begin as hidden
    private List<Item> interactables;                  // import instance of list of interactable objects
    private Map<String, ItemUse> itemUses; // map containing descriptions of item use results
    private String titleCard;
    private String instructions;
    private String objectives;
    private String prologue;
    private String tutorialsText;

    // map containing locked doors and interactables
    private final Map<String, Boolean> lockedObjects = new HashMap<>(Map.of("bridge", false, "cabinet", true));

    // methods that define what happens after using items
    private final ItemUseMethods itemUseMethods = new ItemUseMethods();

    // create player
    private final Player player = new Player("Docking Bay", 80.00);
    private List<Item> inventory = new ArrayList<>();  // player inventory, which is initially empty

    //-------------------------------CONTROLLER METHODS

    // Display prologue text
    public void prologue() {
        String[] lines = prologue.split(System.getProperty("line.separator"));

        double linesToDisplay = 13;
        double printLimit = lines.length / linesToDisplay;
        printLimit = Math.ceil(printLimit);

        int idx = 0;
        while (printLimit > 0) {
            for (int i = 0; i < linesToDisplay; i++) {
                if (!lines[idx].equals(null)) {
                    TextPrinter.displayText(lines[idx]);
                    idx++;
                }
                if (idx == lines.length) {
                    break;
                }
            }
            try {
                events.enterToContinue();
            } catch (IOException e) {
                e.printStackTrace();
            }
            printLimit--;
        }
    }

    // Display game Title Card
    public void titleCard(){

        TextPrinter.displayText(titleCard);
        try {
            events.enterForNewGame();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Display user commands
    public void gameInstructions() {
        TextPrinter.displayText(tutorialsText);
        try {
            events.enterForNewGame();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    //--------------------------------PLAYER METHODS

    // commands the player may enter into the console
    public void userCommands(String[] inputArr) throws IOException {
        if (inputArr.length == 0) {
            throw new IllegalArgumentException("No input.");
        }

        // SINGLE WORD COMMANDS
        // display objectives
        if (inputArr[0].equals("objectives")) {
            objectives();
        }

        // exit the game
        else if (inputArr[0].equals("exit") || inputArr[0].equals("quit") || inputArr[0].equals("escape")) {
            quit();
        }

        // restart the game
        else if (inputArr[0].equals("new") || inputArr[0].equals("restart") || inputArr[0].equals("escape")) {
            restart();
        }

        // restart the game
        else if (inputArr[0].equals("help") || inputArr[0].equals("instructions")) {
            help();
        }

        // make oxygen pipes never run out, this is a difficulty setting
        // call this command again to turn it back off
        else if (inputArr[0].equals("easymode")) {
            clearConsole();

            // either turns on or turns off easy mode
            itemUseMethods.setEasyMode(!itemUseMethods.isEasyMode());
        }

        // check for commands that are too short or too long
        else if (inputArr.length != 2) {
            clearConsole();
            if (inputArr[0].equals("")) {
                TextPrinter.displayText("\n\nEMPTY COMMAND!\n\n", Color.RED);
            } else {
                TextPrinter.displayText("I don't know how to simply, \"" + inputArr[0] + "\". I need a target to " + inputArr[0] + "!");
            }
        }

        // MULTI-WORD COMMANDS
        // movement commands
        else if (inputArr[0].equals("go") || inputArr[0].equals("walk") || inputArr[0].equals("move") || inputArr[0].equals("run")) {
            // check that player is allowed to go in that direction
            player.setCurrentRoom(move(getRoomsList(), player.getCurrentRoom(), inputArr[1]));
        }

        // inspect rooms, items, or anything listed as a Point of interest
        else if (inputArr[0].equals("look") || inputArr[0].equals("inspect") || inputArr[0].equals("examine") || inputArr[0].equals("study") || inputArr[0].equals("investigate")) {
            // rooms are inspected differently than items
            if (inputArr[1].equals("room")) {
                TextPrinter.displayText(inspectRoom(getItems(), getInteractables(), getRoomsList(), player.getCurrentRoom()));
            } else {
                clearConsole();
                TextPrinter.displayText(inspectItem(getItems(), getInteractables(), player.getCurrentRoom(), inputArr[1]));
            }
        }

        // getting items from rooms
        else if (inputArr[0].equals("get") || inputArr[0].equals("grab")) {
            clearConsole();
            pickUpItem(inputArr[1]);
        } else if (inputArr[0].equals("drop") || inputArr[0].equals("release") || inputArr[0].equals("leave")) {
            // iterate through the inventory
            for (int i = 0; i < getInventory().size(); i++) {
                if (inputArr[1].equals(getInventory().get(i).getName())) {     // find name of item to drop
                    String itemToRemoveName = getInventory().get(i).getName(); // remove item from inventory
                    Item removedItem = getInventory().remove(i);               //
                    TextPrinter.displayText(String.format("Dropped %s!\n", itemToRemoveName));
                    // and remove the item from the room's item list
                    getItems().add(removedItem);
                } else {
                    TextPrinter.displayText(String.format("I can't drop %s because %s isn't there!", inputArr[1], inputArr[1]));
                }
            }
        }

        // using items and interactables
        else if (inputArr[0].equals("use")) {
            // check that player is allowed to use the item, then display the results
            clearConsole();
            useItem(getInventory(), getInteractables(), inputArr[1]);
        }

        // invalid command
        else {
            clearConsole();
            TextPrinter.displayText("I don't know how to " + inputArr[0] + " something!\n\n!***** Ensure you PRESS ENTER to continue to the Command Prompt before entering Commands! *****!", Color.RED);
        }
    }

    // Display commands reminder
    public void help() {
        TextPrinter.displayText(instructions, Color.CYAN);
        try {
            events.enterForNewGame();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // restarts game when called
    public void restart() {
        String[] string = {};
        try {
            App.main(string);
        } catch (IOException err) {
            err.printStackTrace();
        }
    }

    // quits the game when called
    public void quit() {
        System.exit(0);
    }

    // display game winning objectives
    public void objectives(){
        TextPrinter.displayText(objectives);
        try {
            events.enterForNewGame();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /*
     * displays the current status of the player, including
     * current location, inventory, and oxygen levels
     */
    public void showStatus(String location, String description) {
        TextPrinter.displayText("--------------------------------", Color.YELLOW);

        TextPrinter.displayText("You are in the " + location + '\n');            //print the player 's current location

        TextPrinter.displayText(description, Color.GREEN);          // print description of current room


        //iterate through the inventory and add each item to the string
        StringBuilder itemInventorySB = new StringBuilder();
        for (int i = 0; i < getInventory().size(); i++) {
            itemInventorySB.append(" - ").append( getInventory().get(i).getName());
        }

        // print what the player is carrying
        TextPrinter.displayText(String.format("\nInventory: %s", itemInventorySB), Color.BLUE);

        // round oxygen percentage down to 2 decimal places
        double roundOff = Math.round(player.getOxygen() * 100) / 100.0;

        // print remaining oxygen
        TextPrinter.displayText(String.format("\nOxygen Level: %.2f" + " percent", roundOff), Color.RED);

        TextPrinter.displayText("--------------------------------", Color.YELLOW);
    }

    /*
     * moves player between rooms in map by finding valid exits
     * prompts player to INSPECT ROOM when invalid choice is given.
     * returns string which resets currentRoom in App
     */
    public String move(List<Room> map, String room, String dir) {
        String retRoom = ""; // create empty string to hold return room

        // iterate through map
        for (Room value : map) {
            // if the direction desired exists as an exit in that room...
            if (value.getName().equals(room)) {
                // ...then reassign return room as the room in that direction
                switch (dir) {
                    case "north":
                        retRoom = value.getExits().getNorth();
                        break;

                    case "south":
                        retRoom = value.getExits().getSouth();
                        break;

                    case "east":
                        retRoom = value.getExits().getEast();
                        break;

                    case "west":
                        retRoom = value.getExits().getWest();
                        break;
                    // if an invalid direction is chosen, tell the player
                    default:
                        String message = "\nINVALID DIRECTION: " + dir + "\nChoose a valid direction. (Hint: INSPECT ROOM if you're lost)";
                        TextPrinter.displayText(message, Color.RED);
                        retRoom = room;
                        break;
                }

                // if retRoom is an empty string then there is no exit in that direction
                if (retRoom.equals("")) {
                    String sb = "\nINVALID DIRECTION: " + dir + "\nThere is no EXIT in that DIRECTION. (Hint: INSPECT ROOM if you're lost)";
                    retRoom = room;
                    TextPrinter.displayText(sb, Color.RED);
                    return retRoom; // return back to starting room
                }
                // else, check if this room is locked
                else if (lockedObjects.containsKey(retRoom.toLowerCase())) {
                    if (lockedObjects.get(retRoom.toLowerCase())) {
                        String message = String.format("\nThe %s is LOCKED!\n\nYou must find a means to open it first.", retRoom);
                        TextPrinter.displayText(message, Color.YELLOW);
                        retRoom = room;
                    }
                }
            }
        }
        return retRoom; // return new room
    }

    /*
     * allows player to inspect rooms to find items and exits
     * returns string detailing
     */
    public String inspectRoom(List<Item> items, List<Item> interactables, List<Room> rooms, String room) {
        StringBuilder roomDescriptionSB = new StringBuilder("You survey the area. \n\nYou're able to find: \n"); // string holds return description
        TextPrinter.displayText("Current Room: " + room);
        // iterate through room list
        for (Item item : items) {
            for (int j = 0; j < item.getRoom().size(); j++) {
                if (item.getRoom().get(j).equals(room)) {              // ensure item is in same room as player
                    // first add all items to return
                    roomDescriptionSB.append( "- ").append(item.getFullName()).append("\n");
                }
            }
        }

        for (Item interactable : interactables) {
            for (int j = 0; j < interactable.getRoom().size(); j++) {
                if (interactable.getRoom().get(j).equals(room)) {              // ensure item is in same room as player
                    // first add all items to return
                    roomDescriptionSB.append("- ").append(interactable.getFullName()).append("\n");
                }
            }
        }

        roomDescriptionSB.append("\nExits: \n");            // then add a header for exits from the room

        for (Room value : rooms) {
            if (value.getName().equals(room)) {
                // add each existing exit to the return string
                if (!value.getExits().getNorth().equals("")) {          // ignore non-exits
                    roomDescriptionSB.append("- North: ").append(value.getExits().getNorth()).append("\n");
                }
                if (!value.getExits().getSouth().equals("")) {
                    roomDescriptionSB.append("- South: ").append(value.getExits().getSouth()).append("\n");
                }
                if (!value.getExits().getEast().equals("")) {
                    roomDescriptionSB.append("- East: ").append(value.getExits().getEast()).append("\n");
                }
                if (!value.getExits().getWest().equals("")) {
                    roomDescriptionSB.append("- West: ").append(value.getExits().getWest()).append("\n");
                }

                roomDescriptionSB.append("\n"); // add a new line for formatting
            }
        }

        return roomDescriptionSB.toString();                       // return description
    }

    /*
     * allows player to inspect items and pointsOfInterest
     * returns string detailing what was inspected
     */
    public String inspectItem(List<Item> items, List<Item> interactables, String room, String toBeInspected) {
        String itemDescription = "I cannot INSPECT " + toBeInspected + "!"; // create empty string to hold return description

        // iterate through inventory list
        for (int i = 0; i < getInventory().size(); i++) {
            // find the inventory item matching the inspected item
            if (getInventory().get(i).getName().equals(toBeInspected) || getInventory().get(i).getSynonyms().contains(toBeInspected)) {
                if (!getInventory().get(i).isUsed()) {
                    return getInventory().get(i).getDescription();     // then return the unused description
                } else {
                    return getInventory().get(i).getUsedDescription(); // if it has return the used description
                }
            } else {
                itemDescription = "There is no \"" + toBeInspected + "\".\n\n(Use INSPECT ROOM if you are looking for an item!)";
            }
        }

        // iterate through items list
        for (Item item : items) {
            if (item.getName().equals(toBeInspected) || item.getSynonyms().contains(toBeInspected)) {  // find the items matching the inspected item
                if (item.getRoom().contains(room)) {            // if instance of item in the same room
                    if (!item.isUsed()) {
                        return item.getDescription();     // then return the unused description
                    } else {
                        return item.getUsedDescription();   // if it has return the used description
                    }
                }
            } else {
                itemDescription = "There is no \"" + toBeInspected + "\".\n\n(Use INSPECT ROOM if you are looking for an item!)";
            }
        }

        // iterate through interactables list
        for (Item interactable : interactables) {
            if (interactable.getName().equals(toBeInspected) || interactable.getSynonyms().contains(toBeInspected)) {  // find the interactables matching the inspected item
                if (interactable.getRoom().contains(room)) {            // if instance of interactable in the same room
                    if (!interactable.isUsed()) {
                        return interactable.getDescription();     // then return the unused description
                    } else {
                        return interactable.getUsedDescription();   // if it has return the used description
                    }
                }
            } else {
                itemDescription = "There is no \"" + toBeInspected + "\".\n\n(Use INSPECT ROOM if you are looking for an item!)";
            }
        }
        return itemDescription; // return description
    }

    /*
     * allows player to get items and add them to the inventory
     * using certain interactables will call this automatically
     */
    public void pickUpItem(String toBePickedUp) throws IOException {
        // look into the arraylist of items
        for (Iterator<Item> iter = getItems().iterator(); iter.hasNext(); ) {
            Item item = iter.next();
            // if the user input matches the item name AND the item has not been used
            if (toBePickedUp.equals(item.getName())) {
                // then it will add that item to the user's inventory list in memory
                getInventory().add(item);
                TextPrinter.displayText(String.format("\nYou picked up the %s!\n", item.getName().toUpperCase()));
                TextPrinter.displayText(String.format("You stow the %s away in your field bag", item.getName().toUpperCase()));

                // and remove the item from the room's item list
                iter.remove();
                return;
            }
        }
        // Default message if nothing is able to be picked up
        TextPrinter.displayText(String.format("There is no %s that you can see to GET in this ROOM!\n\n(Some items are hidden, INSPECT objects to find hidden items!)", toBePickedUp.toUpperCase()));
    }

    /*
     * allows player to use items and pointsOfInterest
     * returns string detailing what was the result
     */
    public void useItem(List<Item> inventory, List<Item> interactables, String toBeUsed) {
        // instantiate a null Method class object
        Method method;

        // iterate through inventory list
        for (Item item : inventory) {
            // if the item toBeUsed is in the inventory
            if (item.getName().equals(toBeUsed) || item.getSynonyms().contains(toBeUsed)) {
                if (!item.isUsed()) {
                    item.setUsed(true);
                    // this allows one to retrieve any method using reflection
                    try {
                        // get meta data from ItemUseMethods class using an instance
                        @SuppressWarnings("unchecked") Class<ItemUseMethods> clazz = (Class<ItemUseMethods>) itemUseMethods.getClass();
                        // reassign method using the .getMethod() method from .getClass() via Java reflection
                        method = clazz.getMethod(itemUses.get(toBeUsed).getMethod());
                    } catch (NoSuchMethodException err) {
                        throw new RuntimeException(err);
                    }

                    try {
                        clearConsole();
                        // display description of use effects to player
                        TextPrinter.displayText(itemUses.get(toBeUsed).getUseDescription());
                        // invoke the method retrieved above, this allows any item object to be used the same way
                        method.invoke(itemUseMethods);
                        return;
                    } catch (IllegalAccessException | InvocationTargetException err) {
                        throw new RuntimeException(err);
                    }
                } else {
                    // if the item has been used already, use different description text
                    TextPrinter.displayText(item.getUsedDescription());
                    return;
                }
            }
        }

        // iterate through interactables list
        for (Item interactable : interactables) {
            // if the item toBeUsed is an interactable
            if (interactable.getName().equals(toBeUsed) || interactable.getSynonyms().contains(toBeUsed)) {
                if (lockedObjects.containsKey(interactable.getName())) {
                    if (!lockedObjects.get(interactable.getName())) { // check if this interactable is considered locked
                        // check if the item is in the same room
                        if (interactable.getRoom().contains(player.getCurrentRoom())) {
                            if (!interactable.isUsed()) {
                                interactable.setUsed(true);
                                // this allows one to retrieve any method using reflection in the same way as above
                                try {
                                    @SuppressWarnings("unchecked") Class<ItemUseMethods> clazz = (Class<ItemUseMethods>) itemUseMethods.getClass();
                                    method = clazz.getMethod(itemUses.get(interactable.getName()).getMethod());
                                } catch (NoSuchMethodException err) {
                                    throw new RuntimeException(err);
                                }

                                try {
                                    TextPrinter.displayText(itemUses.get(interactable.getName()).getUseDescription());
                                    method.invoke(itemUseMethods);
                                    return;
                                } catch (IllegalAccessException | InvocationTargetException err) {
                                    throw new RuntimeException(err);
                                }
                            } else {
                                TextPrinter.displayText(interactable.getUsedDescription());
                                return;
                            }
                        }
                    } else {
                        TextPrinter.displayText(String.format("\nThe %s is LOCKED!\n\nYou must find a means to open it first.", toBeUsed));
                        return;
                    }
                } else {
                    // check if the item is in the same room
                    if (interactable.getRoom().contains(player.getCurrentRoom())) {
                        if (!interactable.isUsed()) {
                            interactable.setUsed(true);
                            // this allows one to retrieve any method using reflection in the same way as above
                            try {
                                @SuppressWarnings("unchecked") Class<ItemUseMethods> clazz = (Class<ItemUseMethods>) itemUseMethods.getClass();
                                method = clazz.getMethod(itemUses.get(interactable.getName()).getMethod());
                            } catch (NoSuchMethodException err) {
                                throw new RuntimeException(err);
                            }

                            try {
                                TextPrinter.displayText(itemUses.get(interactable.getName()).getUseDescription());
                                method.invoke(itemUseMethods);
                                return;
                            } catch (IllegalAccessException | InvocationTargetException err) {
                                throw new RuntimeException(err);
                            }
                        } else {
                            TextPrinter.displayText(interactable.getUsedDescription());
                            return;
                        }
                    }

                }
            }
        }
        // default error message
        TextPrinter.displayText("You're either not carrying a \"" + toBeUsed + "\" right now, or you can't see one in this ROOM.\n\nItems must be in your INVENTORY to use unless you cannot GET the item. [Your SHIP, for example]\nINSPECT objects to find hidden items!");

    }

    // removes item from the lockedObjects map to free up their use by the player
    public void unlockThis(String toBeUnlocked) {
        if (lockedObjects.containsKey(toBeUnlocked)) {
            lockedObjects.put(toBeUnlocked, false);
        }
    }

    //-------------------------------UTILITY METHODS

    // when a hidden item is made visible, make it a part of the normal itemsList
    public void itemNotHidden(HiddenItem hiddenItem) {
        getHiddenItems().remove(hiddenItem); // remove the item to be revealed from the hiddenItem list
        Item newItem = new Item(hiddenItem); // create new item using hiddenItem info
        getItems().add(newItem);             // add the new item to the items list
    }

    // when a hidden interactable item is made visible, make it a part of the normal interactables list
    public void interactableNotHidden(HiddenItem hiddenItem) {
        getHiddenItems().remove(hiddenItem);         // remove the item to be revealed from the hiddenItem list
        Item newInteractable = new Item(hiddenItem); // create new item using hiddenItem info
        getInteractables().add(newInteractable);     // add the new item to the interactables list
    }

    /**
     * Clear console by calling the appropriate system command depending on the OS.
     */
    public static void clearConsole() {
        ProcessBuilder var0 = os.contains("windows") ? new ProcessBuilder(new String[]{"cmd", "/c", "cls"}) : new ProcessBuilder(new String[]{"clear"});

        try {
            var0.inheritIO().start().waitFor();
        } catch (IOException var3) {
            var3.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    // returns the items list object
    public void loadGameObjects() {
        instructions = TextLoader.loadText(INSTRUCTIONS_TXT);
        tutorialsText = TextLoader.loadText(TUTORIAL_TEXT_TXT);
        objectives = TextLoader.loadText(GAMEOBJECTIVES_TXT);
        titleCard = TextLoader.loadText(WELCOME_TXT);
        prologue = TextLoader.loadText(PROLOGUE_TXT);

        roomsList = JSONLoader.loadFromJsonAsList(SHIPROOMS_JSON, Room.class);
        items = JSONLoader.loadFromJsonAsList(ITEMS_JSON, Item.class);
        hiddenItems = JSONLoader.loadFromJsonAsList(HIDDENITEMS_JSON, HiddenItem.class);
        interactables = JSONLoader.loadFromJsonAsList(INTERACTABLES_JSON, Item.class);
        itemUses = JSONLoader.loadFromJsonAsMap(ITEMUSES_JSON, ItemUse.class);
    }

    //-------------------------------ACCESSOR METHODS

    public List<Room> getRoomsList() {
        return roomsList;
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

    public List<Item> getInventory() {
        return inventory;
    }

    private void setInventory(List<Item> inventory) {
        this.inventory = inventory;
    }

    public Map<String, ItemUse> getItemUses() {
        return itemUses;
    }

    public ItemUseMethods getItemUseMethods() {
        return itemUseMethods;
    }

    public Player getPlayer() {
        return player;
    }
}
