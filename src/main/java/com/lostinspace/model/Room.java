package com.lostinspace.model;

/*
 * Room Class| Author: Mike Greene
 * Room object for text adventure Lost in Space.
 * Holds all data for one instance of a room in the game.
 */

import java.util.List;
import java.util.Map;

public class Room {
    private String name;                                 // name of area
    private String description;                          // description of area
    private Map<String, String> exits;                   // object acts as where each direction will lead player
    private Map<String, String> exit_descriptions;       // descriptions of the exits
    private List<String> interactables;
    private String entryItem;

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
        return interactables;
    }

    public String getEntryItem() {
        return entryItem;
    }
}