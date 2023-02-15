package com.lostinspace.model;

/*
 * Room Class| Author: Mike Greene
 * Room object for text adventure Lost in Space.
 * Holds all data for one instance of a room in the game.
 */

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Room {
    private String name;                                 // name of area
    private String description;                          // description of area
    private Map<String, String> exits;                   // object acts as where each direction will lead player
    private Map<String, String> exit_descriptions;       // descriptions of the exits
    private List<String> interactables;
    private String entryItem;
    private String image;
    private String tmx;
    private Set<String> items;

    // ACCESSOR METHODS
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Map<String, String> getExits() {
        return exits;
    }

    public Map<String, String> getExit_descriptions() {
        return exit_descriptions;
    }

    public List<String> getInteractables() {
        if (interactables == null)
            interactables = Collections.emptyList();
        return interactables;
    }

    public String getEntryItem() {
        return entryItem;
    }

    public String getTMX() {
        return tmx;
    }

    public String getImage() {
        return image;
    }

    public Set<String> getItems() {
        if (items == null)
            items = Collections.emptySet();
        return items;
    }

    public void removeItemFromRoom(String itemName) {
        getItems().remove(itemName);
    }
}