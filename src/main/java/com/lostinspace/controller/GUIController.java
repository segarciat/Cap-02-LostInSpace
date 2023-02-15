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
        this.itemController = new ItemController(model, model.getPlayer());

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
    // Get look description
    public String lookItem(ItemMod item) {
        return item.getLookDescription();
    }

    // Get item
    public String getItem(ItemMod item) {
        String returnText = "";

        if (!model.checkInInventory(item)) {
            returnText = itemController.getItem(item);
        }

        return returnText;
    }

    // Get or interaction with item on the map
    public String interactItem(ItemMod item) {
        String returnText = "";

        returnText = itemController.interactItem(item);

        return returnText;
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
}
