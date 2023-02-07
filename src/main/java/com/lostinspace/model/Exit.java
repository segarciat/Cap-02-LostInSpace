package com.lostinspace.model;

/*
 * Room Exits Class| Author: Mike Greene
 * Exits object for text adventure Lost in Space.
 * Holds all data for where each exit leads from an area.
 */

public class Exit{
    private final String west; // all are "" by default in .json file
    private final String east; // some directions will contain the name of the room they lead to
    private final String north;
    private final String south;

    // Constructor
    public Exit(String north, String south, String east, String west) {
        this.north = north;
        this.south = south;
        this.east = east;
        this.west = west;
    }

    // ACCESSOR METHODS
    public String getWest() {
        return west;
    }

    public String getEast() {
        return east;
    }

    public String getNorth() {
        return north;
    }

    public String getSouth() {
        return south;
    }
}

