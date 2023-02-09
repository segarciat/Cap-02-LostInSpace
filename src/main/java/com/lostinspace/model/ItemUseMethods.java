package com.lostinspace.model;

import com.lostinspace.app.App;
import com.lostinspace.controller.Controller;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ItemUseMethods {
    public static final double OXYGEN_REFILL = 25.5; // how much oxygen is restored to player
    boolean easyMode = false;   // used to define difficulty settings

    // returns the controller instance from main class
    Controller getController() {
        Controller controller = App.getController();
        return controller;
    }

    // LIST OF METHODS ATTACHED TO THE USE OF GAME ITEMS
    // All are called reflectively via Controller.useItem()

    public void useScrambler() {

    }

    public void usePipes() {
        getController().getPlayer().refillOxygen(OXYGEN_REFILL);

        // interate through interactables list to find the pipes item
        for (Item item : getController().getInteractables()) {
            String currentRoom = getController().getPlayer().getCurrentRoom();
            item.getRoom().remove(currentRoom);
        }
    }

    public void useKey() {
        // get the list of hidden items from the Controller class
        Map<String, ItemUse> itemUses = getController().getItemUses();
        List<HiddenItem> hiddenItems = getController().getHiddenItems();
        List<Item> interactables = getController().getInteractables();

        for (int i = 0; i < getController().getInventory().size(); i++) {
            if (getController().getInventory().get(i).getName().equals("key")) {
                // if the player is in the correct room
                if (getController().getPlayer().getCurrentRoom().equals("Cargo Hold")) {

                    getController().unlockThis("cabinet");    // unlock the cabinet using the key
                    getController().getInventory().get(i).setUsed(true); // flag key as having been used

                    // display the description of succeeding to use the key
                    System.out.println(itemUses.get("cabinet").getUseDescription());

                    for (Iterator<Item> iter = interactables.iterator(); iter.hasNext(); ) {
                        Item item = iter.next();
                        // if the user input matches the item name AND the item has not been used
                        if (item.getName().equals("cabinet")) {
                            item.setUsed(true); // set cabinet as having been used
                        }
                    }

                    // now iterate through the hidden items list,
                    // and remove tool from the list of hidden items
                    for (int j = 0; j < hiddenItems.size(); j++) {
                        if (hiddenItems.get(j).getName().equals("tool")) {
                            getController().itemNotHidden(hiddenItems.get(j));
                        }
                    }
                    // finally, pickup the tool item, or throw an exception if failed.
                    try {
                        getController().pickUpItem("tool");
                    } catch (IOException err) {
                        throw new RuntimeException(err);
                    }
                } else {
                    System.out.println("...but you're unable to use it now!");
                    getController().getInventory().get(i).setUsed(false);
                }
            }
        }
    }

    public void useComponent() {
    }

    public void useTool() {
    }

    public void useKeycard() {
        for (int i = 0; i < getController().getInventory().size(); i++) {
            if (getController().getInventory().get(i).getName().equals("keycard")) {
                // if the player is in the correct room
                if (getController().getPlayer().getCurrentRoom().equals("Command Deck Hallway")) {
                    //unlock the room using the keycard
                    getController().unlockThis("bridge");
                    System.out.println("...the doors to the Bridge UNLOCK with a chime.");
                } else {
                    System.out.println("...but you're unable to use it here!");
                    getController().getInventory().get(i).setUsed(false);
                }
            }
        }
    }

    public void useManual() {
    }

    public void useFlameThrower() {
    }

    public void useCabinet() {
        if (getController().getInventory().contains("key")) {
            useKey();
        }
    }

    public void useConsole() {
    }

    /**
     * Player can win if their inventory contains "component", "tool", and "manual".
     */
    public void useShip() {
        Set<String> winningItems = Set.of("component", "tool", "manual");
        boolean canUseShip = getController().getInventory().stream()
                .filter(item -> winningItems.contains(item.getName()))
                .allMatch(Item::isUsed);

        if (!canUseShip) {
            throw new IllegalArgumentException("Sorry, you need ALL three items to fix the ship. It is inoperable as " +
                    "of now.");
        }
    }

    public void useRack() {
        // get the list of hidden items from the Controller class
        List<HiddenItem> hiddenItems = getController().getHiddenItems();

        for (int i = 0; i < hiddenItems.size(); i++) {
            if (hiddenItems.get(i).getName().equals("component")) {
                getController().itemNotHidden(hiddenItems.get(i));
            }
        }

        try {
            getController().pickUpItem("component");
        } catch (IOException err) {
            throw new RuntimeException(err);
        }
    }

    public void usePoster() {
    }

    public void useLocker() {
        // get the list of hidden items from the Controller class
        List<HiddenItem> hiddenItems = getController().getHiddenItems();

        for (int i = 0; i < hiddenItems.size(); i++) {
            if (hiddenItems.get(i).getName().equals("key")) {
                getController().itemNotHidden(hiddenItems.get(i));
            }
        }

        try {
            getController().pickUpItem("key");
        } catch (IOException err) {
            throw new RuntimeException(err);
        }
    }

    public void useCooler() {
        // get the list of hidden items from the Controller class
        List<HiddenItem> hiddenItems = getController().getHiddenItems();

        for (int i = 0; i < hiddenItems.size(); i++) {
            if (hiddenItems.get(i).getName().equals("corpse")) {
                getController().interactableNotHidden(hiddenItems.get(i));
            }
        }
    }

    public void useCorpse() {
        // get the list of hidden items from the Controller class
        List<HiddenItem> hiddenItems = getController().getHiddenItems();

        for (int i = 0; i < hiddenItems.size(); i++) {
            if (hiddenItems.get(i).getName().equals("keycard")) {
                getController().itemNotHidden(hiddenItems.get(i));
            }
        }

        try {
            getController().pickUpItem("keycard");
        } catch (IOException err) {
            throw new RuntimeException(err);
        }
    }

    public void useLibrary() {
        // get the list of hidden items from the Controller class
        List<HiddenItem> hiddenItems = getController().getHiddenItems();

        for (int i = 0; i < hiddenItems.size(); i++) {
            if (hiddenItems.get(i).getName().equals("manual")) {
                getController().itemNotHidden(hiddenItems.get(i));
            }
        }

        try {
            getController().pickUpItem("manual");
        } catch (IOException err) {
            throw new RuntimeException(err);
        }
    }

    public void useAirlock() {
        System.out.println("This will be part of an action packed sequence of events in the FINAL VERSION!");
    }

    // ACCESSOR METHODS
    public boolean isEasyMode() {
        return easyMode;
    }

    public void setEasyMode(boolean easyMode) {
        this.easyMode = easyMode;
    }
}
