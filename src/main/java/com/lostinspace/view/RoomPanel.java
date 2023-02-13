package com.lostinspace.view;

import com.lostinspace.app.AppGUI;
import com.lostinspace.model.Room;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class RoomPanel extends ImagePanel{
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
        gbc.gridy = 2;
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

            directionButton.addActionListener(new RoomExitAction(exitRoomName, exitRoomDescription));
            buttonPane.add(directionButton);
        }

        gbc.gridy = 1;
        this.add(buttonPane, gbc);
    }

    private class RoomExitAction implements ActionListener {
        private final String exitRoomName;
        private final String exitRoomDescription;

        public RoomExitAction(String exitRoomName, String exitRoomDescription) {
            this.exitRoomName = exitRoomName;
            this.exitRoomDescription = exitRoomDescription;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            roomTextArea.setText(exitRoomDescription);

            // Set time for room transition
            Timer timer = new Timer(ROOM_TRANSITION_DELAY,
                    e1 -> app.getFrame().setContentPane(app.getRoomFrames().get(exitRoomName)));
            timer.setRepeats(false);

            timer.start();
        }
    }
}
