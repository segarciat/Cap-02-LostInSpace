package com.lostinspace.view;

import com.lostinspace.app.AppGUI;
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

    public RoomPanel(AppGUI app, Room room, GUIController controller) {
        super(room.getImage(), app.getFrame().getWidth(), app.getFrame().getHeight());

        this.controller = controller;
        this.app = app;

        // Set up frame attributes
        this.setLayout(null);
        this.setSize(this.getPreferredSize());

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
        roomTextArea.setMargin(new Insets(0,24,0,24));
        roomTextArea.setBounds(0, WINDOW_SIZE - TEXTAREA_HEIGHT, WINDOW_SIZE, TEXTAREA_HEIGHT);
        this.add(roomTextArea);

        // Create direction buttons for each exit
        Map<String, String> roomExits = room.getExits();
        Map<String, String> roomExitDescriptions = room.getExit_descriptions();

        JPanel buttonPane = new JPanel();
        buttonPane.setOpaque(false);
        buttonPane.setBounds(0, WINDOW_SIZE -TEXTAREA_HEIGHT - 64, WINDOW_SIZE, 48);

        for (String exit: roomExits.keySet()) {
            JButton directionButton = new JButton(String.format("Go %s", exit));
            String exitRoomName = roomExits.get(exit);
            String exitRoomDescription = roomExitDescriptions.get(exitRoomName);

            directionButton.addActionListener(new RoomExitAction(room.getDescription(), exitRoomName,
                    exitRoomDescription));
            directionButton.setFocusable(false);
            buttonPane.add(directionButton);
        }

        Model model = app.getController().getModel();

        // Add items for this room.
        Set<ItemMod> itemMods = model.getRoomItems().get(room.getName());
        for (ItemMod item: itemMods) {
            if (item.getImage() != null) {
                JButton button = SwingComponentCreator.createButtonWithImage(item.getImage(), item.getRectangle());
                button.addActionListener(new ItemButtonClickAction(item));
                button.addMouseListener(new ItemButtonHoverAction(item));

                this.add(button);
            }
        }

        // this.add(buttonPane, gbc);
        this.add(buttonPane);
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
                Player player = app.getController().getPlayer();
                player.setCurrentRoom(exitRoomName);
                app.getFrame().setContentPane(app.getRoomFrames().get(exitRoomName));
                roomTextArea.setText(roomDescription);
                app.getFrame().revalidate();
//                app.getFrame().requestFocus();
            });
            timer.setRepeats(false);

            timer.start();
        }
    }

    private class ItemButtonClickAction implements ActionListener {
        private final ItemMod item;
        private ItemButtonClickAction(ItemMod item) {
            this.item = item;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.printf("clicked on %s\n", item.getName());
        }
    }

    private class ItemButtonHoverAction implements MouseListener {
        private final ItemMod item;
        private ItemButtonHoverAction(ItemMod item) {
            this.item = item;
        }

        @Override
        public void mouseClicked(MouseEvent e) {}

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {

                // Change text area text to the look description of the item
                String lookDescription = controller.lookItem(item);
                roomTextArea.setText(lookDescription);
            } else if (e.getButton() == MouseEvent.BUTTON3) {
                String textDescription = controller.getOrUseItem(item);
                roomTextArea.setText(textDescription);


            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {}

        @Override
        public void mouseEntered(MouseEvent e) {
            JButton button = (JButton) e.getSource();
            button.setBorder(new LineBorder(Color.PINK)); // add border color on hover
        }

        @Override
        public void mouseExited(MouseEvent e) {
            JButton button = (JButton) e.getSource();
            button.setBorder(BorderFactory.createEmptyBorder()); // empty border when not hovering
        }

        private void removeItemFromMap() {

        }
    }
}
