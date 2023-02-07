package com.lostinspace.model;

public class Player {
    private String currentRoom; // current location of player

    /* current oxygen level for player
     * oxygen acts as both a timer/health bar for the player
     * when oxygen == 0.00 the game is over
     */
    private double oxygen;

    // Constructor
    public Player(String currentRoom, double oxygen){
        this.currentRoom = currentRoom;
        this.oxygen = oxygen;
    }

    // PLAYER OBJECT METHODS
    //allows player to add oxygen to their current amount
    public void refillOxygen(double refill){
        refill = getOxygen() + refill;
        if(refill > 100.00) refill = 100.00; // limit oxygen to "100.00%"
        setOxygen(refill);
    }

    // consumes a given amount of oxygen, happens when player conducts an action
    public void consumeOxygen(double o2Consumed){
        setOxygen( (getOxygen() - o2Consumed) ); // reduce oxygen by o2Consumed
        if(getOxygen() < 0.00) setOxygen(0.00); // limit empty oxygen to "0.00%"
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
