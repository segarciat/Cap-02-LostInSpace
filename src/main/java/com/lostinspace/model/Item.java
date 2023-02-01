package com.lostinspace.model;

public class Item {
    private String name;
    private String fullName;
    private boolean isHeld;

    public Item() {
        super();
    }
    public Item(String name, String fullName, boolean isHeld) {
        setName(name);
        setFullName(fullName);
        this.isHeld = isHeld;
    }

    public String getName() {
        return name;
    }
    private void setName(String name) {
        this.name = name;
    }

    public String getFullName() { return fullName; }

    private void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public boolean isHeld() {
        return isHeld;
    }

    public void setHeld(boolean held) {
        isHeld = held;
    }
}
