package com.lostinspace.model;

public class ItemMod {
    private String name;
    private String[] synonyms;
    private String description;
    private String lookDescription;
    private boolean used;
    private String useDescription;
    private String failedUseDescription;
    private String usedDescription;
    private String[] requiredItems;
    private boolean isHidden;

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

    public String[] getRequiredItems() {
        return requiredItems;
    }

    public boolean isHidden() {
        return isHidden;
    }
}
