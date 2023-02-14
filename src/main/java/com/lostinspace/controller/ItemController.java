package com.lostinspace.controller;

import com.lostinspace.model.ItemMod;
import com.lostinspace.model.Player;

class ItemController {
    private final Player player;

    public ItemController(Player player) {
        this.player = player;
    }

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

    public void getItem(ItemMod item) {
        // add item to player inventory
        player.addToInventory(item);
    }


}
