package com.lostinspace.controller;

import com.lostinspace.view.AppView;
import com.lostinspace.model.*;
import com.lostinspace.model.ItemMod;
import com.lostinspace.view.Route;

import java.util.List;
import java.util.Map;

/*
 * Primary controller class for the GUI
 */
public class GUIController {
    public static final String ENVIRO_FIELD = "Enviro-Field";
    // Create instance of model and other controllers
    private final Model model;
    private final AppView view;
    private final ItemController itemController;

    // Other constants
    public static final double O_2_CONSUMED = 5.0;
    public static final double INITIAL_OXYGEN = 80.00;
    public static final String INITIAL_ROOM = "Docking Bay";
    public static boolean isEasyMode = false;

    // Constructor
    public GUIController(Model model) {
        // Create new instance of model and controllers
        this.model = model;
        this.view = new AppView(this, model);
        this.itemController = new ItemController(model, view);

        // Call set up method

        // Cheat mode for satisfying winning condition
//        cheatMode();
    }

    // Set up initial parameters for the game
    private void setUp() {
        model.getPlayer().setCurrentRoom(INITIAL_ROOM);
        model.getPlayer().setOxygen(INITIAL_OXYGEN);
        view.setRoute(Route.TITLE);
        view.update();
    }

    private void cheatMode() {
        String[] keyItems = new String[] {"component", "tool", "manual"};

        for (String item : keyItems) {
            ItemMod keyItem = model.getItemByName(item);
            model.getOfficerZhang().addItemToInventory(keyItem);
        }
    }

    public void execute() {
        // Call set up method
        setUp();
    }

    /**
     * Moves the player to a new room and alters their oxygen, according to isEasyMode.
     * @param destinationName The new room that the player will be in.
     * @return whether or not the player moved.
     */
    public boolean movePlayer(String destinationName) {
        Player player = model.getPlayer();

        String currentRoomName = player.getCurrentRoom();

        // See can move.
        Room destinationRoom = model.getRooms().get(destinationName);
        String requiredItemName = destinationRoom.getEntryItem();
        ItemMod requiredItemOwnedByPlayer = model.returnItemFromInventory(requiredItemName);

        /*
         * Check if the entry item has been used
         * If the item has not been used, then notify the player they cannot move to that location
         * If the item has been used, the player can proceed
         */
        if (requiredItemName != null && (requiredItemOwnedByPlayer == null || !requiredItemOwnedByPlayer.isUsed())) {
            destinationName = currentRoomName; // stay in this room.
        } else if (player.getOxygen() == 0 && !isEasyMode) {
            // If the player's oxygen is 0, then move them to the Enviro-Field (end-game)
            player.setCurrentRoom(ENVIRO_FIELD);
        } else {
            // If player's oxygen is > 0, then move them to the destination
            player.consumeOxygen(O_2_CONSUMED, isEasyMode);
            player.setCurrentRoom(destinationName);
        }

        return !currentRoomName.equalsIgnoreCase(destinationName);
    }

    /**
     * Toggles easy mode for the game and returns the new value.
     * @return Whether the game is now in easy mode or not.
     */
    public boolean toggleEasyMode() {
        isEasyMode = !isEasyMode;
        return isEasyMode;
    }

    /*
     * ITEM METHODS
     */
    /**
     * Get look description
     * @param item ItemMod item player is looking at
     * @return String
     */
    public String lookItem(ItemMod item) {
        return item.getLookDescription();
    }

    /**
     * Get item
     * @param item ItemMod item player is getting
     * @return String
     */
    public String getItem(ItemMod item) {
        String textDescription = "";

        if (!model.inventoryContains(item.getName())) {
            textDescription = itemController.getItem(item);
        }

        return textDescription;
    }

    /**
     * Interact with item on the map
     * @param item ItemMod item player is interacting with
     * @return String
     */
    public String interactItem(ItemMod item) {
        return itemController.interactItem(item);
    }

    /**
     * Get hidden item object when an interactable item is being interacted with
     * @param item ItemMod item player is interacting with
     * @return ItemMod
     */
    public ItemMod getHiddenItem(ItemMod item) {
        return itemController.getHiddenItem(item, getPlayer().getCurrentRoom());
    }

    /**
     * Use the item the player clicks on
     * @param item ItemMod item player wants to use
     * @return String
     */
    public String useItem(ItemMod item) {
        return itemController.useInventoryItem(item);
    }

    /*
     * ACCESSOR METHODS
     */
    public Player getPlayer() {
        return model.getPlayer();
    }

    public Map<String, Room> getRoomMap() {
        return model.getRooms();
    }

    public Map<String, ItemMod> getItems() {
        return model.getItems();
    }

    public String getInstructions() {
        return model.getInstructions();
    }

    public String getObjectives() {
        return model.getObjectives();
    }

    public String getPrologue() {
        return model.getPrologue();
    }

    public String getTutorial() {
        return model.getTutorial();
    }

    public Model getModel() {
        return model;
    }

    public ItemController getItemController() {
        return itemController;
    }
}
