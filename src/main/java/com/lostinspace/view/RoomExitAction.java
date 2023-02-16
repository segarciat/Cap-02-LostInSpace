package com.lostinspace.view;

import com.lostinspace.controller.GUIController;
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
