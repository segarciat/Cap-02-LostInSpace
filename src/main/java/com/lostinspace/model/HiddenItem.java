package com.lostinspace.model;

import java.util.List;

public class HiddenItem {
    private String name;
    private List<String> synonyms;
    private List<String> inside;
    private boolean hidden;
    private String fullName;
    private String description;
    private boolean used;
    private String usedDescription;

    public HiddenItem(String name, List<String> synonyms, List<String> inside, boolean hidden, String fullName, String description, boolean used, String usedDescription) {
        this.name = name;
        this.synonyms = synonyms;
        this.inside = inside;
        this.hidden = hidden;
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

    public List<String> getInside() {
        return inside;
    }

    public void setInside(List<String> inside) {
        this.inside = inside;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
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