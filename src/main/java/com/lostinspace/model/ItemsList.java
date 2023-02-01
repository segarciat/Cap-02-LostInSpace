package com.lostinspace.model;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

// after loading the text resource for the game map, acts as the root ArrayList for all Rooms
public class ItemsList {
    public List<Item> items;

    public List<Item> getItems() {
        return items;
    }
}
