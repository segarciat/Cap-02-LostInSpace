package com.lostinspace.controller;

import com.lostinspace.model.*;
import com.lostinspace.view.AppView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/*
 * Controller for ItemMod objects
 */
class ItemController {
    public static final String SHIP = "ship";
    public static final String CONSOLE = "console";
    public static final String COMPONENT = "component";
    public static final String TOOL = "tool";
    public static final String MANUAL = "manual";
    public static final String PIPES = "pipes";
    public static final double O_2_CONSUMED_PIPES = 25.0;
    public static final String SCRAMBLER = "scrambler";
    public static final String MONSTER = "monster";
    private final List<String> POSTER_COLORS = Stream.of("Orange", "Pink", "Green", "Blue", "Purple", "Yellow").sorted().collect(Collectors.toList());
    private final List<String> WRONG_COLORS = List.of("Red", "Black", "White", "Silver");

    // Fields
    private final Model model;
    private final AppView view;

    // Constructor
    public ItemController(Model model, AppView view) {
        // Get model from primary Controller ControllerGUI
        this.model = model;
        this.view = view;
    }

    /**
     * Add item to player inventory, then remove from location
     *
     * @param item ItemMod item player is getting
     * @return String
     */
    public String getItem(ItemMod item) {
        // Add item to inventory
        model.getPlayer().addToInventory(item);

        // Get current Room object
        String roomName = model.getPlayer().getCurrentRoom();
        Room room = model.getRoomByName(roomName);

        // Remove item from room
        room.removeItemFromRoom(item.getName());

        return "You added the " + item.getName() + " to your inventory.";
    }

    /**
     * Interact with item on the map
     *
     * @param item ItemMod item player is interacting with
     * @return String indicating whether was already used, or if the item was just used successfully.
     */
    public String interactItem(ItemMod item) {
        /*
         * If item has already been used, then return early itemUSEDDescription
         * If not, if item is successfully interacted with, then set to used = true
         */
        if (item.isUsed())
            return item.getUsedDescription();

        // It is possible that an item is needed, and it has not been used
        // Check if interactable requires an item and if player has it
        ItemMod requiredItemInInventory = model.returnItemFromInventory(item.getRequiredItem());

        if (item.getRequiredItem() != null && (requiredItemInInventory == null || !requiredItemInInventory.isUsed()))
            return item.getFailedUseDescription();

        switch (item.getName()) {
            case SHIP:
                interactShip(item);
                break;
            case CONSOLE:
                interactConsole(item);
                break;
            case PIPES:
                interactPipes(item);
                break;
            default:
                item.setUsed(true);
                break;
        }

        return item.isUsed()? item.getUseDescription() : item.getFailedUseDescription();
    }

    /**
     * Player uses the ship
     * @param item ItemMod ship item object
     */
    private void interactShip(ItemMod item) {
        if (model.getOfficerZhang().getInventory().size() == 3) {
            item.setUsed(true);
            // The player has met win game condition, send back to ControllerGUI
        }
    }

    /**
     * Player uses the pipes
     * @param item ItemMod pipes item object
     */
    private void interactPipes(ItemMod item) {
        model.getPlayer().refillOxygen(O_2_CONSUMED_PIPES);
        item.setUsed(true);
    }

    /**
     * Player uses the console
     * @param item ItemMod console item object
     */
    public void interactConsole(ItemMod item) {
        // Create random colors to display as choices.
        List<String> colors = new ArrayList<>(POSTER_COLORS);
        colors.addAll(WRONG_COLORS);
        Collections.shuffle(colors);

        List<String> userResponse = view.showConsoleGame(colors).stream().sorted().collect(Collectors.toList());

        if (userResponse.equals(POSTER_COLORS)) {
            item.setUsed(userResponse.equals(POSTER_COLORS));
        }
    }

    /**
     * Get hiddenItem as a ItemMod object with Rectangle properties from model class to pass back to RoomPanel to
     * create the JButton
     *
     * @param item     ItemMod item player is interacting with
     * @param roomName Room name of the current location of the player
     * @return ItemMod hiddenItem
     */
    public ItemMod getHiddenItem(ItemMod item, String roomName) {
        String hiddenItemName = item.getHiddenItem();

        return model.getHiddenItemByName(hiddenItemName, roomName);
    }

    /**
     * Use the item the player clicks on
     *
     * @param inventoryItem ItemMod item player wants to use
     * @return String
     */
    public String useInventoryItem(ItemMod inventoryItem) {
        String textDescription;

        /*
         * If item has already been used, then return early itemUSEDDescription
         * If not, if item is successfully interacted with, then set to used = true
         */
        if (inventoryItem.isUsed()) {
            return inventoryItem.getUsedDescription();
        }

        // See if there is a required item.
        ItemMod requiredItem = model.getRoomItems().values().stream()
                .flatMap(Collection::stream)
                .filter(i -> i.getName().equals(inventoryItem.getRequiredItem()))
                .findFirst()
                .orElse(null);

        // Check current location of the player
        String currentLocation = model.getPlayer().getCurrentRoom();

        // Check if item can be used
        if (!currentLocation.equals(inventoryItem.getUseLocation())) {
            textDescription = "You cannot use " + inventoryItem.getName() + " right now. It may have a use somewhere else.";
        } else if (requiredItem != null && !requiredItem.isUsed()) {
            textDescription = inventoryItem.getFailedUseDescription();
        } else {
            inventoryItem.setUsed(true);

            // Add item to Officer inventory
            if (inventoryItem.getName().equals("tool") || inventoryItem.getName().equals("component") || inventoryItem.getName().equals(
                    "manual")) {
                model.getOfficerZhang().addItemToInventory(inventoryItem);
            }

            if (inventoryItem.getName().equals(SCRAMBLER)) {
                // remove monster from the room.
                view.getCurrentRoomPanel().removeItemFromRoom(MONSTER);
            }

            textDescription = inventoryItem.getUseDescription();
        }

        return textDescription;
    }
}
