package com.lostinspace.controller;

import com.lostinspace.model.*;

/*
 * Controller for ItemMod objects
 */
class ItemController {
    // Fields
    private final Model model;

    // Constructor
    public ItemController(Model model) {
        // Get model from primary Controller ControllerGUI
        this.model = model;
    }

    /**
     * Add item to player inventory, then remove from location
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
     * @param item ItemMod item player is interacting with
     * @return String
     */
    public String interactItem(ItemMod item) {
        String itemDescription = "";

        /*
         * If item has already been used, then return early itemUSEDDescription
         * If not, if item is successfully interacted with, then set to used = true
         */
        if (item.isUsed()) {
            return item.getUsedDescription();
        }

        // If item is 'ship', then check inventory of Officer officerZhang object to check for win game condition
        if (item.getName().equals("ship")) {
            if (model.getOfficerZhang().getInventory().size() != 3) {
                // Return early
                return item.getFailedUseDescription();
            } else {
                // The player has met win game condition, send back to ControllerGUI
                GUIController.winGame();

                // Return early
                return itemDescription;
            }
        }

        // Check if interactable requires an item
        if (item.getRequiredItem() == null) {
            item.setUsed(true);
            itemDescription = item.getUseDescription();
        } else {
            // Get required item for interactable
            String requiredItem = item.getRequiredItem();
            ItemMod required = model.getItemByName(requiredItem);

            // If required item is not in inventory, then display failedUsedDescription
            if (model.checkInInventory(required)) {
                item.setUsed(true);
                itemDescription = item.getUseDescription();
            } else {
                itemDescription = item.getFailedUseDescription();
            }
        }

        // TODO: Check if an item can be used in a certain location

        return itemDescription;
    }

    /**
     * Get hiddenItem as a ItemMod object with Rectangle properties from model class to pass back to RoomPanel to
     * create the JButton
     * @param item ItemMod item player is interacting with
     * @param roomName Room name of the current location of the player
     * @return ItemMod hiddenItem
     */
    public ItemMod getHiddenItem(ItemMod item, String roomName) {
        String hiddenItemName = item.getHiddenItem();

        return model.getHiddenItemByName(hiddenItemName, roomName);
    }
}
