package com.lostinspace.classes;

/*
 * Room Class| Author: Mike Greene
 * Room object for text adventure Lost in Space.
 * Holds all data for one of the rooms in the game.
 */

import java.util.ArrayList;

public class Room {
    public String name;             // name of area
    public String description;      // description of area
    public ArrayList<String> items; // array of items that can be used/got in the currentRoom
    public Exit exits;              // object acts as where each direction will lead player

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<String> getItems() {
        return items;
    }

    public Exit getExits() {
        return exits;
    }
}