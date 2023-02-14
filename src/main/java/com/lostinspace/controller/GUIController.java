package com.lostinspace.controller;

import com.lostinspace.model.*;
import com.lostinspace.model.ItemMod;

import java.util.Map;

public class GUIController {
    private final Model model;
    private final ItemController itemController;

    // Other constants
    public static final double O_2_CONSUMED = 5.0;
    public static final double INITIAL_OXYGEN = 80.00;
    public static final String INITIAL_ROOM = "Docking Bay";
    public static boolean isEasyMode = false;

    public GUIController() {
        this.model = new Model();
        this.itemController = new ItemController(model.getPlayer());

        setUp();
    }

    // Set up initial parameters for the game
    private void setUp() {
        model.getPlayer().setCurrentRoom(INITIAL_ROOM);
        model.getPlayer().setOxygen(INITIAL_OXYGEN);
    }

    /*
     * ITEM METHODS
     */
    private ItemMod getItemByName(String itemName) {
        boolean isFound = false;
        ItemMod item = new ItemMod();

        for (String itemString : model.getItems().keySet()) {
            if (itemString.equals(itemName)) {
                isFound = true;
                item = model.getItems().get(itemString);
            }
        }

        if (!isFound) {
            throw new IllegalArgumentException("Item not found");
        }

        return item;
    }

    public void getItem(String itemName) {
        ItemMod item = getItemByName(itemName);
        itemController.getItem(item);
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


}
