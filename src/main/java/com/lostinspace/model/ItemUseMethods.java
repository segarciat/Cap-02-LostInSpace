package com.lostinspace.model;

import com.lostinspace.app.App;
import com.lostinspace.controller.Controller;

import java.util.Iterator;

public class ItemUseMethods {
    double oxygenRefill = 25.5;
    boolean easyMode = false;

    public void useScrambler() {
        System.out.println("You done used the SCRAMBLER!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    }

    public void usePipes() {
        Controller controller = App.getController(); // get the controller instance from main class
        controller.getPlayer().refillOxygen(oxygenRefill);

        // read comments below for explanation
        if (easyMode == false) {
            // interate through interactables list to find the pipes item
            for (Iterator<Item> iter = controller.getInteractables().iterator(); iter.hasNext(); ) {
                Item item = iter.next();

                /*
                 * this removes the currentRoom from the item's room list
                 * this makes the pipes no longer usable in this room only
                 * the reason for this is to make oxygen pipes a 1 use item only
                 * for the sake of difficulty, I've created a boolean that you can set
                 * to turn this off, called easyMode
                 */
                item.getRoom().remove(controller.getPlayer().getCurrentRoom());
            }
        }
    }

    public void useKey() {
    }

    public void useComponent() {
    }

    public void useTool() {
    }

    public void useKeycard() {
    }

    public void useManual() {
    }

    public void useFlameThrower() {
    }

    public void useCabinet() {
    }

    public void useConsole() {
    }

    public void useShip() {
        // does nothing right now,
        // will probably make this the command to use to win the game
        // once all items are in ship
    }

    public void useRack() {
    }

    public void usePoster() {
    }

    public void useLocker() {
    }

    public void useCooler() {
    }

    public void useCorpse() {
    }

    public void useLibrary() {
    }


    // ACCESSOR METHOD
    public boolean isEasyMode() {
        return easyMode;
    }

    public void setEasyMode(boolean easyMode) {
        this.easyMode = easyMode;
    }
}
