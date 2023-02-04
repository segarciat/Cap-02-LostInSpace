package com.lostinspace.model;

import java.util.List;

// after loading the text resource for the game map, acts as the root ArrayList for all Rooms
public class InteractablesList {
    public List<Item> interactables;

    public List<Item> getInteractables() {
        return interactables;
    }
}