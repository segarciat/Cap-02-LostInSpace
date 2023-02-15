package com.lostinspace.controller;

import com.lostinspace.model.ItemMod;
import com.lostinspace.model.Model;
import com.lostinspace.model.Player;
import com.lostinspace.model.Room;

class ItemController {
    private final Model model;
    private final Player player;

    public ItemController(Model model, Player player) {
        this.model = model;
        this.player = player;
    }

    // Check if item is in inventory
    public Boolean checkInInventory(ItemMod item) {
        boolean isFound = false;

        for (ItemMod itemInInventory : player.getInventory()) {
            if (itemInInventory.getName().equals(item.getName())) {
                isFound = true;
                break;
            }
        }

        return isFound;
    }

    // Add item to player inventory, then remove from location
    public String getItem(ItemMod item) {
        // Add item to inventory
        player.addToInventory(item);

        // Get current Room object
        String roomName = player.getCurrentRoom();
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

        // Check if interactable requires an item
        if (item.getRequiredItem() == null) {
            item.setUsed(true);
            return item.getUseDescription();
        } else {
            // Get required item for interactable
            String requiredItem = item.getRequiredItem();
            ItemMod required = model.getItemByName(requiredItem);

            // If required item is not in inventory, then display failedUsedDescription
            if (checkInInventory(required)) {
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
        String currentLocation = player.getCurrentRoom();

        if (item.getUseLocation().equals(currentLocation)) {
            canUse = true;
        }

        return canUse;
    }
}
