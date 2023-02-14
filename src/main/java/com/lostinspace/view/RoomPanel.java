package com.lostinspace.view;

import com.lostinspace.app.AppGUI;
import com.lostinspace.controller.GUIController;
import com.lostinspace.model.Room;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class RoomPanel extends ImagePanel {
    private GUIController controller;

    // font size
    private static final Font MONOSPACE_BOLD_MED = new Font("Monospaced", Font.BOLD, 14);

    // font colors
    private static final Color COLOR_GREEN = new Color(76, 175, 82);
    private static final int WINDOW_SIZE = 720;
    public static final int ROOM_TRANSITION_DELAY = 1500;
    public static final int TEXTAREA_HEIGHT = 168;

    private final JTextArea roomTextArea;
    private final AppGUI app;

    public RoomPanel(AppGUI app, Room room) {
        super(room.getImage(), app.getFrame().getWidth(), app.getFrame().getHeight());

        controller = new GUIController();

        this.setLayout(new GridBagLayout());
        this.app = app;

        // Get room description
        roomTextArea = new JTextArea(room.getDescription());

        // Set text area attributes
        roomTextArea.setForeground(COLOR_GREEN);
        roomTextArea.setFont(MONOSPACE_BOLD_MED);
        roomTextArea.setMinimumSize(new Dimension(WINDOW_SIZE, TEXTAREA_HEIGHT));
        roomTextArea.setLineWrap(true);
        roomTextArea.setWrapStyleWord(true);
        roomTextArea.setOpaque(false);
        roomTextArea.setEditable(false);
        roomTextArea.setFocusable(false);
        roomTextArea.setMargin(new Insets(12,24,0,24));

        // Set spacer text area
        JTextArea spacer = new JTextArea();
        spacer.setOpaque(false);
        spacer.setEditable(false);
        spacer.setFocusable(false);

        // Create layout constraints
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipady = 504;
        this.add(spacer, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.ipady = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        this.add(roomTextArea, gbc);

        // Create direction buttons for each exit
        Map<String, String> roomExits = room.getExits();
        Map<String, String> roomExitDescriptions = room.getExit_descriptions();

        JPanel buttonPane = new JPanel();
        buttonPane.setOpaque(false);

        for (String exit: roomExits.keySet()) {
            JButton directionButton = new JButton(String.format("Go %s", exit));
            String exitRoomName = roomExits.get(exit);
            String exitRoomDescription = roomExitDescriptions.get(exitRoomName);

            directionButton.addActionListener(new RoomExitAction(room.getDescription(), exitRoomName,
                    exitRoomDescription));
            buttonPane.add(directionButton);
        }

        if (room.getName().equals("Docking Bay")) {
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.insets = new Insets(0, 0, -24, 288);
            JButton item = SwingComponentCreator.createButtonWithImage("/images_item/scrambler.png", 216, 264, 48, 48);
            item.addActionListener(new getItemAction("scrambler"));
            this.add(item, gbc);
            this.setComponentZOrder(item, 0);
        }

        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.gridy = 2;
        this.add(buttonPane, gbc);

        this.setComponentZOrder(spacer, 2);
        this.setComponentZOrder(roomTextArea, 1);
        this.setComponentZOrder(roomTextArea, 1);
        this.setComponentZOrder(buttonPane, 1);
    }

    private class RoomExitAction implements ActionListener {
        private final String roomDescription;
        private final String exitRoomName;
        private final String exitRoomDescription;

        public RoomExitAction(String roomDescription, String exitRoomName, String exitRoomDescription) {
            this.roomDescription = roomDescription;
            this.exitRoomName = exitRoomName;
            this.exitRoomDescription = exitRoomDescription;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            roomTextArea.setText(exitRoomDescription);

            // Set time for room transition
            Timer timer = new Timer(ROOM_TRANSITION_DELAY, e1 -> {
                app.getFrame().setContentPane(app.getRoomFrames().get(exitRoomName));
                roomTextArea.setText(roomDescription);
            });
            timer.setRepeats(false);

            timer.start();
        }
    }

    private class getItemAction implements ActionListener {
        private final String itemName;

        public getItemAction(String itemName) {
            this.itemName = itemName;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            controller.getItem(itemName);
        }
    }
}
