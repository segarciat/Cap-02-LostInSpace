package com.lostinspace.model;

public class PointOfInterest {
    private String name;
    private String description;
    private boolean used;
    private String usedDescr;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isUsed() {
        return used;
    }

    public String getUsedDescr() {
        return usedDescr;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }
}
