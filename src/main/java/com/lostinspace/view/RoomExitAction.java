package com.lostinspace.view;

import com.lostinspace.controller.GUIController;
import com.lostinspace.model.ItemMod;
import com.lostinspace.model.Room;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RoomExitAction implements ActionListener {
    private final GUIController controller;
    private final RoomPanel panel;
    private final Room room;
    private final String direction;

    public static final int ROOM_TRANSITION_DELAY = 1500;

    RoomExitAction(GUIController controller, String direction, RoomPanel panel, Room room) {
        this.controller = controller;
        this.direction = direction;
        this.panel = panel;
        this.room = room;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String destination = room.getExits().get(direction);
        Room destinationRoom = controller.getModel().getRoomByName(destination);
        ItemMod entryItem = new ItemMod();

        // Check if the destination requires an entry item
        if (destinationRoom.getEntryItem() != null) {

            /*
             * Try getting the entryItem from the model method
             * If the item does not exist the inventory, the method throws an IllegalArgrumentException, and it must
             * be caught to notify the player they cannot move to that location
             */
            try {
                // Entry item exists in the player's inventory
                entryItem = controller.getModel().returnItemFromInventory(destinationRoom.getEntryItem());
            } catch (IllegalArgumentException exception) {
                // Entry item does not exist in the player's inventory
                System.out.println(exception.getMessage());
                cannotMovePlayer("You cannot move to " + destination + ". You may need to use an item before entering" +
                        " the area.");
            }

            /*
             * Check if the entry item has been used
             * If the item has not been used, then notify the player they cannot move to that location
             * If the item has been used, the player can proceed
             */
            if (entryItem.isUsed()) {
                movePlayer(destination);
            } else {
                cannotMovePlayer("You cannot move to " + destination + ". You may need to use an item before entering" +
                        " the area.");
            }
        } else {
            movePlayer(destination);
        }
    }

    /**
     * Set the textArea to notify the player they cannot move to that area
     * @param barrierText Text displayed to the user that the user cannot enter the area
     */
    private void cannotMovePlayer(String barrierText) {
        panel.getRoomTextArea().setText(barrierText);
    }

    /**
     * Move the player to the next location based on destination
     */
    private void movePlayer(String destination) {
        String textToDisplayOnExit = room.getExit_descriptions().get(destination);
        panel.getRoomTextArea().setText(textToDisplayOnExit);

        // Set time for room transition
        Timer timer = new Timer(ROOM_TRANSITION_DELAY, e1 -> {
            // Reset back to original room text.
            panel.getRoomTextArea().setText(room.getDescription());
            controller.movePlayer(destination);
        });
        timer.setRepeats(false);

        timer.start();
    }
}
