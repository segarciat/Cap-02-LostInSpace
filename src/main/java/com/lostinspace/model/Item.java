package com.lostinspace.model;

public class Item {
    private String name;
    private String fullName;
    private boolean isHeld;

    public String getName() {
        return name;
    }

    public String getFullName() { return fullName; }

    public boolean isHeld() {
        return isHeld;
    }

    public void setHeld(boolean held) {
        isHeld = held;
    }
}
