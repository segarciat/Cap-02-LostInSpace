package com.lostinspace.controller;

import com.lostinspace.model.ItemMod;
import com.lostinspace.model.Player;

class ItemController {
    private final Player player;

    public ItemController(Player player) {
        this.player = player;
    }

    public void getItem(ItemMod item) {
        // add item to player inventory
        player.addToInventory(item);
    }
}
