package com.lostinspace.view;

import com.lostinspace.controller.GUIController;
import com.lostinspace.model.*;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Map;
import java.util.Set;

/**
 * View that shows the room that the player is currently in and associated items while playing.
 */
public class RoomPanel extends ImagePanel {
    // font size
    private static final Font MONOSPACE_BOLD_MED = new Font("Monospaced", Font.BOLD, 14);

    // font colors
    private static final int WINDOW_SIZE = 720;
    public static final int ROOM_TRANSITION_DELAY = 1500;
    public static final int TEXTAREA_HEIGHT = 168;

    // The text being shown in the room.
    private final JTextArea roomTextArea;
    private final GUIController controller;
    private final JProgressBar oxygenBar;

    private final Room room;

    public RoomPanel(AppView app, Room room, GUIController controller) {
        super(room.getImage(), app.getFrame().getWidth(), app.getFrame().getHeight());

        this.controller = controller;
        this.room = room;
        this.oxygenBar = new JProgressBar();
        oxygenBar.setStringPainted(true);
        oxygenBar.setBounds(20, 75, 200, 25);
        updateOxygenBar();

        // Set up frame attributes
        this.setLayout(null);
        this.setSize(this.getPreferredSize());
        this.add(oxygenBar);

        // Get room description
        roomTextArea = SwingComponentCreator.createStyledTextArea(room.getDescription());
        // Set text area attributes
        roomTextArea.setFont(MONOSPACE_BOLD_MED);
        roomTextArea.setMinimumSize(new Dimension(WINDOW_SIZE, TEXTAREA_HEIGHT));
        roomTextArea.setMargin(new Insets(0,24,0,24));
        roomTextArea.setBounds(0, WINDOW_SIZE - TEXTAREA_HEIGHT, WINDOW_SIZE, TEXTAREA_HEIGHT);
        this.add(roomTextArea);

        // Create direction buttons for each exit
        Map<String, String> roomExits = room.getExits();
        JPanel directionButtonsPane = new JPanel();
        directionButtonsPane.setOpaque(false);
        directionButtonsPane.setBounds(0, WINDOW_SIZE -TEXTAREA_HEIGHT - 64, WINDOW_SIZE, 48);

        // Create direction buttons and add to panel
        for (String exit: roomExits.keySet()) {
            JButton directionButton = SwingComponentCreator.createButtonWithText(String.format("Go %s", exit));
            directionButton.addActionListener(new RoomExitAction(exit));
            directionButtonsPane.add(directionButton);
        }

        Model model = app.getController().getModel();

        // Add items for this room
        Set<ItemMod> itemMods = model.getRoomItems().get(room.getName());
        for (ItemMod item: itemMods) {
            if (item.getImage() != null && !item.isHidden()) {
                JButton button = SwingComponentCreator.createButtonWithImage(item.getImage(), item.getRectangle());
                button.addMouseListener(new ItemMouseAction(controller, item, this, button));
                this.add(button);
            }
        }

        this.add(directionButtonsPane);
    }

    /**
     * Displays the player's current oxygen in a certain color according to the amount.
     */
    private void updateOxygenBar() {
        int oxygen = (int) controller.getPlayer().getOxygen();
        oxygenBar.setString(String.format("Oxygen: %s", oxygen));
        oxygenBar.setValue(oxygen);
        Color color;
        if (oxygen > 70)
            color = Color.GREEN;
        else if (oxygen > 30)
            color = Color.YELLOW;
        else
            color = Color.RED;
        oxygenBar.setForeground(color);
    }

    /**
     * Action that updates the view (room) upon clicking a cardinal direction button like "Go North" or "Go West".
     */
    private class RoomExitAction implements ActionListener {

        private final String direction;

        private RoomExitAction(String direction) {
            this.direction = direction;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String destination = room.getExits().get(direction);
            String textToDisplayOnExit = room.getExit_descriptions().get(destination);
            roomTextArea.setText(textToDisplayOnExit);

            // Set time for room transition
            Timer timer = new Timer(ROOM_TRANSITION_DELAY, e1 -> {
                // Reset back to original room text.
                roomTextArea.setText(room.getDescription());
                controller.movePlayer(destination);
                updateOxygenBar();
            });
            timer.setRepeats(false);

            timer.start();
        }
    }

    public JTextArea getRoomTextArea() {
        return roomTextArea;
    }
}
