package com.lostinspace.model;

import java.util.List;

public class Item {
    private String name;
    private List<String> synonyms;
    private List<String> room;
    private String fullName;
    private String description;
    private boolean used;
    private String usedDescription;


    public Item() {
        super();
    }

    public Item(String name, List<String> synonyms, List<String> room, String fullName, String description, boolean used, String usedDescription) {
        this.name = name;
        this.synonyms = synonyms;
        this.room = room;
        this.fullName = fullName;
        this.description = description;
        this.used = used;
        this.usedDescription = usedDescription;
    }

    // ACCESSOR METHODS
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public List<String> getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(List<String> synonyms) {
        this.synonyms = synonyms;
    }

    public List<String> getRoom() {
        return room;
    }

    public void setRoom(List<String> room) {
        this.room = room;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public String getUsedDescription() {
        return usedDescription;
    }

    public void setUsedDescription(String usedDescription) {
        this.usedDescription = usedDescription;
    }
}
