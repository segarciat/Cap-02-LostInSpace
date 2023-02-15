package com.lostinspace.model;

import java.util.ArrayList;

/*
 * Object class for CWO2 Zhang
 */
public class Officer {
    private ArrayList<ItemMod> inventory;

    public Officer(ArrayList<ItemMod> inventory) {
        this.inventory = inventory;
    }

    public ArrayList<ItemMod> getInventory() {
        return inventory;
    }

    public void addItemToInventory(ItemMod item) {
        getInventory().add(item);
    }
}
