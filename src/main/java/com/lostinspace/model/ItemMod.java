package com.lostinspace.model;

import java.awt.*;
import java.util.ArrayList;

public class ItemMod {
    private String name;
    private String[] synonyms;
    private String description;
    private String lookDescription;
    private boolean used;
    private String useDescription;
    private String failedUseDescription;
    private String usedDescription;
    private String requiredItem;
    private String image;
    private boolean isHidden;
    private Rectangle rectangle;
    private String itemMethod;
    private String useLocation;
    private String hiddenItem;

    public ItemMod() {

    }

    public ItemMod(ItemMod item) {
        this.name = item.name;
        this.synonyms = item.synonyms;
        this.description = item.description;
        this.lookDescription = item.lookDescription;
        this.used = item.used;
        this.useDescription = item.useDescription;
        this.failedUseDescription = item.failedUseDescription;
        this.usedDescription = item.usedDescription;
        this.requiredItem = item.requiredItem;
        this.isHidden = item.isHidden;
        this.image = item.image;
        this.rectangle = item.rectangle;
        this.itemMethod = item.itemMethod;
        this.useLocation = item.useLocation;
        this.hiddenItem = item.hiddenItem;
    }

    public String getName() {
        return name;
    }

    public String[] getSynonyms() {
        return synonyms;
    }

    public String getDescription() {
        return description;
    }

    public String getLookDescription() {
        return lookDescription;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public String getUseDescription() {
        return useDescription;
    }

    public String getFailedUseDescription() {
        return failedUseDescription;
    }

    public String getUsedDescription() {
        return usedDescription;
    }

    public String getRequiredItem() {
        return requiredItem;
    }

    public boolean isHidden() {
        return isHidden;
    }

    public String getImage() {
        return image;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public void setRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

    public String getItemMethod() {
        return itemMethod;
    }

    public String getUseLocation() {
        return useLocation;
    }

    public void setHiddenItem(String hiddenItem) {
        this.hiddenItem = hiddenItem;
    }

    public String getHiddenItem() {
        return hiddenItem;
    }
}
