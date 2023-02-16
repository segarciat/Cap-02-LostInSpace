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
        String textDescription = "";

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
                return textDescription;
            }
        }

        // Check if interactable requires an item
        if (item.getRequiredItem() == null) {
            item.setUsed(true);
            textDescription = item.getUseDescription();
        } else {
            // Get required item for interactable
            String requiredItemName = item.getRequiredItem();

            /*
             * Must return item from inventory to get the 'same' object from the inventory panel
             * If model.getItemByName() method is used, the object is not the same
             */
            ItemMod requiredItemInInventory = model.returnItemFromInventory(requiredItemName);

            // If required item is not in inventory, then display failedUsedDescription
            if (model.checkInInventory(requiredItemInInventory) && requiredItemInInventory.isUsed()) {
                item.setUsed(true);
                textDescription = item.getUseDescription();
            } else {
                textDescription = item.getFailedUseDescription();
            }
        }

        return textDescription;
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

    /**
     * Use the item the player clicks on
     * @param inventoryItem ItemMod item player wants to use
     * @return String
     */
    public String useItem(ItemMod inventoryItem) {
        String textDescription = "";

        /*
         * If item has already been used, then return early itemUSEDDescription
         * If not, if item is successfully interacted with, then set to used = true
         */
        if (inventoryItem.isUsed()) {
            return inventoryItem.getUsedDescription();
        }

        // Check current location of the player
        String currentLocation = model.getPlayer().getCurrentRoom();

        // Check if item can be used
        if (!currentLocation.equals(inventoryItem.getUseLocation())) {
            textDescription = "You cannot use " + inventoryItem.getName() + " right now. It may have a use somewhere else.";
        } else {
            inventoryItem.setUsed(true);
            textDescription = inventoryItem.getUseDescription();
        }

        return textDescription;
    }
}
