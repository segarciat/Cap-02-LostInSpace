package com.lostinspace.model;

import com.lostinspace.app.App;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Item {
    private String name;
    private List<String> synonyms;
    private List<String> room;
    private String fullName;
    private String description;
    private boolean used;
    private String usedDescription;

    // Constructor
    public Item() {
        super();
    }

    public Item(Item item) {
        this(item.getName(), item.getSynonyms(), item.getRoom(), item.getFullName(), item.getDescription(), item.isUsed(), item.getUsedDescription());
    }

    // Constructor used when a hiddenItem is made visible.
    // Creates a new item in the current room
    public Item(HiddenItem hiddenItem){
        this.name = hiddenItem.getName();
        this.synonyms = hiddenItem.getSynonyms();
        this.room = Arrays.asList(App.getController().getPlayer().getCurrentRoom());
        this.fullName = hiddenItem.getFullName();
        this.description = hiddenItem.getDescription();
        this.used = hiddenItem.isUsed();
        this.usedDescription = hiddenItem.getUsedDescription();
    }

    public Item(String name, List<String> synonyms, List<String> room, String fullName, String description, boolean used, String usedDescription) {
        this.name = name;
        this.synonyms = new ArrayList<>(synonyms);
        this.room = new ArrayList<>(room);
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
