package com.lostinspace.model;

/*
 * Room Class| Author: Mike Greene
 * Room object for text adventure Lost in Space.
 * Holds all data for one instance of a room in the game.
 */

import java.util.ArrayList;
import java.util.Map;

public class Room {
    public String name;                                 // name of area
    public String description;                          // description of area
    public Exit exits;                                  // object acts as where each direction will lead player

    private Map<String, Room> exitMap;

    public Map<String, Room> getExitMap() {
        return exitMap;
    }


    // ACCESSOR METHODS
    public String getName() { return name; }

    public String getDescription() {
        return description;
    }

    public Exit getExits() {
        return exits;
    }
}