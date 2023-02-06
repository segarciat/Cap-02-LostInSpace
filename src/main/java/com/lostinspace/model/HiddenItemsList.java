package com.lostinspace.model;

import java.util.List;

// after loading the text resource for the game map, acts as the root ArrayList for all Rooms
public class HiddenItemsList {
    public List<HiddenItem> hiddenItems;

    public List<HiddenItem> getHiddenItems() {
        return hiddenItems;
    }
}