package com.lostinspace.model;

/*
 * Room Class| Author: Mike Greene
 * Room object for text adventure Lost in Space.
 * Holds all data for one instance of a room in the game.
 */

import java.util.List;

public class Room {
    private String name;                                 // name of area
    private String description;                          // description of area
    private Exit exits;                                  // object acts as where each direction will lead player
    private List<String> interactables;
    private String entryItem;

    // ACCESSOR METHODS
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Exit getExits() {
        return exits;
    }

    public List<String> getInteractables() {
        return interactables;
    }

    public String getEntryItem() {
        return entryItem;
    }
}