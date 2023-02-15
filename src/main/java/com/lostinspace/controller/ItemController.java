package com.lostinspace.controller;

import com.lostinspace.model.*;

import java.util.Set;

class ItemController {
    private final Model model;
    public ItemController(Model model) {
        this.model = model;
    }

    // Add item to player inventory, then remove from location
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

    // Interact with item on map
    public String interactItem(ItemMod item) {
        String itemDescription = "";

        /*
         * If item has already been used, then send itemUSEDDescription
         * If not, if item is successfully interacted with, then set to used = true
         */
        if (item.isUsed()) {
            return item.getUsedDescription();               // return early
        }

        // If item is 'ship', then check inventory of Officer officerZhang object
        if (item.getName().equals("ship")) {
            if (model.getOfficerZhang().getInventory().size() != 3) {
                return item.getFailedUseDescription();      // return early
            } else {
                // TODO: GO TO WIN CONDITION IN CONTROLLER OR APP
                GUIController.winGame();
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

        /*
         * TODO: Do we need a useLocation for the items since they are automatically used in the console game?
         * May be useful for only scrambler and the key items for 'using' it to give back to CWO2 Zhang in Docking Bay
         */

        return itemDescription;
    }

    // Get hiddenItem as a ItemMod object with Rectangle properties from model class
    public ItemMod getHiddenItem(ItemMod item, String roomName) {
        String hiddenItemName = item.getHiddenItem();

        return model.getHiddenItemByName(hiddenItemName, roomName);
    }
}
