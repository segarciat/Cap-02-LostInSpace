package com.lostinspace.controller;

import com.lostinspace.view.AppView;
import com.lostinspace.model.*;
import com.lostinspace.model.ItemMod;
import com.lostinspace.view.Route;

import java.util.Map;

/*
 * Primary controller class for the GUI
 */
public class GUIController {
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
        this.itemController = new ItemController(model);

        // Call set up method
        setUp();
    }

    // Set up initial parameters for the game
    private void setUp() {
        model.getPlayer().setCurrentRoom(INITIAL_ROOM);
        model.getPlayer().setOxygen(INITIAL_OXYGEN);
        view.setRoute(Route.TITLE);
        view.update();
    }

    public void execute() {
        // Call set up method
        setUp();
    }


    public void movePlayer(String destination) {
        Player player = model.getPlayer();
        if (!player.getCurrentRoom().equalsIgnoreCase(destination))
            player.consumeOxygen(O_2_CONSUMED, isEasyMode);
        player.setCurrentRoom(destination);
        view.update();
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
        String returnText = "";

        if (!model.checkInInventory(item)) {
            returnText = itemController.getItem(item);
        }

        return returnText;
    }

    /**
     * Interact with item on the map
     * @param item ItemMod item player is interacting with
     * @return String
     */
    public String interactItem(ItemMod item) {
        String returnText = "";

        returnText = itemController.interactItem(item);

        return returnText;
    }

    // Get hidden items when an interactable is interacted with

    /**
     * Get hidden item object when an interactable item is being interacted with
     * @param item ItemMod item player is interacting with
     * @return ItemMod
     */
    public ItemMod getHiddenItem(ItemMod item) {
        return itemController.getHiddenItem(item, getPlayer().getCurrentRoom());
    }

    /*
     * WIN CONDITION MET
     */
    public static void winGame() {
        // TODO: Win game condition met, get images, etc for win game panel
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
