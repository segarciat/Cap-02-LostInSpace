package com.lostinspace.view;

import com.lostinspace.model.Room;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RoomExitAction implements ActionListener {
    public static final int ROOM_TRANSITION_DELAY = 1500;

    private final AppView view;
    private final Room room;
    private final String direction;

    RoomExitAction(AppView view, Room room, String direction) {
        this.view = view;
        this.room = room;
        this.direction = direction;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Attempt to move player in the direction in question.
        String destinationName = room.getExits().get(direction);
        boolean moved = view.getController().movePlayer(destinationName);

        // Update UI depending on the outcome.
        if (moved)
            movePlayer(destinationName);
        else
            cannotMovePlayer("You cannot move to " + destinationName + ". You may need to use an item before entering" +
                    " the area.");
    }

    /**
     * Set the textArea to notify the player they cannot move to that area
     * @param barrierText Text displayed to the user that the user cannot enter the area
     */
    private void cannotMovePlayer(String barrierText) {
        RoomPanel roomPanel = view.getRoomFrames().get(room.getName());
        roomPanel.getRoomTextArea().setText(barrierText);
    }

    /**
     * Move the player to the next location based on destination
     */
    private void movePlayer(String destination) {
        // Show the exit text for the current room.
        RoomPanel currentRoomPanel = view.getRoomFrames().get(room.getName());
        String textToDisplayOnExit = room.getExit_descriptions().get(destination);
        currentRoomPanel.getRoomTextArea().setText(textToDisplayOnExit);

        // Set time for room transition
        Timer timer = new Timer(ROOM_TRANSITION_DELAY, e1 -> {
            // Reset back to original room text.
            currentRoomPanel.getRoomTextArea().setText(room.getDescription());
            // Updating the view will update the player's location.
            view.update();
        });
        timer.setRepeats(false);

        timer.start();
    }
}
