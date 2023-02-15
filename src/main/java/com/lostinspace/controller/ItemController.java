package com.lostinspace.controller;

import com.lostinspace.model.*;

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
        /*
         * If item has already been used, then send itemUSEDDescription
         * If not, if item is successfully interacted with, then set to used = true
         */
        if (item.isUsed()) {
            return item.getUsedDescription();
        }

        // If item is 'ship', then check inventory of Officer officerZhang object
        if (item.getName().equals("ship")) {
            if (model.getOfficerZhang().getInventory().size() != 3) {
                return item.getFailedUseDescription();
            } else {
                // TODO: GO TO WIN CONDITION IN CONTROLLER OR APP
                GUIController.winGame();
            }
        }

        // Check if interactable requires an item
        if (item.getRequiredItem() == null) {
            item.setUsed(true);
            return item.getUseDescription();
        } else {
            // Get required item for interactable
            String requiredItem = item.getRequiredItem();
            ItemMod required = model.getItemByName(requiredItem);

            // If required item is not in inventory, then display failedUsedDescription
            if (model.checkInInventory(required)) {
                item.setUsed(true);
                return item.getUseDescription();
            } else {
                return item.getFailedUseDescription();
            }
        }
    }

    // Check if an item in the inventory can be used (depends on location)
    public Boolean canUseItemInInventory(ItemMod item) {
        boolean canUse = false;
        String currentLocation = model.getPlayer().getCurrentRoom();

        if (item.getUseLocation().equals(currentLocation)) {
            canUse = true;
        }

        return canUse;
    }
}
