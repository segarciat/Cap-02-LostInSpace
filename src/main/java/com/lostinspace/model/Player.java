package com.lostinspace.model;

import java.text.DecimalFormat;

public class Player {
    private String currentRoom; // current location of player

    /* current oxygen level for player
     * oxygen acts as both a timer/health bar for the player
     * when oxygen == 0.00 the game is over
     */
    private double oxygen;

    // CTOR
    public Player(String currentRoom, double oxygen){
        this.currentRoom = currentRoom;
        this.oxygen = oxygen;
    }

    // PLAYER OBJECT METHODS
    //allows player to add oxygen to their current amount
    public void refillOxygen(double refill){
        refill = getOxygen() + refill;
        setOxygen(refill);
    }

    // consumes a given amount of oxygen, happens when player conducts an action
    public void consumeOxygen(double o2Consumed){
        setOxygen( (getOxygen() - o2Consumed) );
    }

    // ACCESSOR METHODS
    public String getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(String currentRoom) {
        this.currentRoom = currentRoom;
    }

    public double getOxygen() {
        return oxygen;
    }

    public void setOxygen(double oxygen) {
        this.oxygen = oxygen;
    }
}
