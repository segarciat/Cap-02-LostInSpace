package com.lostinspace.model;

public class ItemUse {
    private String useDescription;
    private String method;
    private String failUseDescription;

    public String getUseDescription() {
        return useDescription;
    }

    public String getFailUseDescription() {
        return failUseDescription == null? "": failUseDescription;
    }

    public String getMethod() {
        return method;
    }
}
