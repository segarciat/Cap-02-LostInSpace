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
    public ArrayList<Item> items;                       // array of items that can be used/got in the currentRoom
    public ArrayList<PointOfInterest> pointsOfInterest; // array of item descriptions for when they are inspected
    public Exit exits;                                  // object acts as where each direction will lead player
//    public Map<String, String> exits;
    private Map<String, Room> exitMap;

    public Map<String, Room> getExitMap() {
        return exitMap;
    }

    public void setExitMap(Map<String, Room> exitMap) {
        this.exitMap = exitMap;
    }



    // ACCESSOR METHODS
    public String getName() { return name; }

    public String getDescription() {
        return description;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public ArrayList<PointOfInterest> getPois() { return pointsOfInterest; }

    public Exit getExits() {
        return exits;
    }

//    public Map<String, String> getExits() {
//        return exits;
//    }
}