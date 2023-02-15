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

/**
 * View that shows the room that the player is currently in and associated items while playing.
 */
public class RoomPanel extends ImagePanel {
    private GUIController controller;

    // font size
    private static final Font MONOSPACE_BOLD_MED = new Font("Monospaced", Font.BOLD, 14);

    // font colors
    private static final Color COLOR_GREEN = new Color(76, 175, 82);
    private static final int WINDOW_SIZE = 720;
    public static final int ROOM_TRANSITION_DELAY = 1500;
    public static final int TEXTAREA_HEIGHT = 168;

    // The text being shown in the room.
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

        // Create direction buttons and add to panel
        for (String exit: roomExits.keySet()) {
            JButton directionButton = SwingComponentCreator.createButtonWithText(String.format("Go %s", exit));
            String exitRoomName = roomExits.get(exit);
            String exitRoomDescription = roomExitDescriptions.get(exitRoomName);

            directionButton.addActionListener(new RoomExitAction(room.getDescription(), exitRoomName,
                    exitRoomDescription));
            buttonPane.add(directionButton);
        }

        Model model = app.getController().getModel();

        // Add items for this room
        Set<ItemMod> itemMods = model.getRoomItems().get(room.getName());
        for (ItemMod item: itemMods) {
            if (item.getImage() != null && !item.isHidden()) {
                JButton button = SwingComponentCreator.createButtonWithImage(item.getImage(), item.getRectangle());
                button.setName(item.getName());
                button.addMouseListener(new ItemMouseAction(item, this, button));
                this.add(button);
            }
        }

        this.add(buttonPane);
    }

    /**
     * Action that updates the view (room) upon clicking a cardinal direction button like "Go North" or "Go West".
     */
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
            });
            timer.setRepeats(false);

            timer.start();
        }
    }

    /**
     * Action that "item buttons" responds when hovering, left-, or right-clicking (highlighting, GET/USE/LOOK actions).
     */
    private class ItemMouseAction implements MouseListener {
        private final ItemMod item;
        private final ImagePanel panel;
        private final JButton button;

        private ItemMouseAction(ItemMod item, ImagePanel panel, JButton button) {
            this.item = item;
            this.panel = panel;
            this.button = button;
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
                String textDescription = "";

                /*
                 * If a player 'gets' an item, the text area is updated to notify the player
                 * In addition, the item button is removed from the panel
                 */
                if (item.getItemMethod().equals("get")) {
                    textDescription = controller.getItem(item);
                    removeButtonFromPanel();
                }
                /*
                 * If a player wants to interact with an item that cannot be placed into the inventory AKA an
                 * interactable, throw to ItemController and get either useDescription, usedDescription, or
                 * failedUseDescription
                 * This is dependent on either the item has been used before AND if there is a requirement to the use
                 *  of the item
                 * If the item has a hidden item that is revealed, then add that hidden item as an item button on the
                 *  panel
                 * Then, set the original item's hidden item property to null
                 */
                else if (item.getItemMethod().equals("interact")) {
                    textDescription = controller.interactItem(item);

                    // TODO: Check if required item in inventory


                    // If item that was being interacted with has a hidden item, then add it to the panel
                    // after being interacted with
                    if (item.getHiddenItem() != null) {
                        ItemMod hiddenItem = controller.getHiddenItem(item);
                        addHiddenItemToPanel(hiddenItem);
                        item.setHiddenItem(null);
                    }
                }

                roomTextArea.setText(textDescription);
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

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

        // TODO: Add item buttons to the inventory when the player 'gets' an item
        private void addButtonToInventory() {

        }

        /**
         * Remove item button from panel
         */
        private void removeButtonFromPanel() {
            panel.remove(button);

            // If any item buttons are added or removed, revalidate panel
            panel.revalidate();
            panel.repaint();
        }

        /**
         * Add hidden item button to panel (acts as a normal item button)
         * @param hiddenItem
         */
        private void addHiddenItemToPanel(ItemMod hiddenItem) {
            JButton hiddenItemButton = SwingComponentCreator.createButtonWithImage(hiddenItem.getImage(),
                    hiddenItem.getRectangle());
            hiddenItemButton.setName(hiddenItem.getName());

            hiddenItemButton.addMouseListener(new ItemMouseAction(hiddenItem, panel, hiddenItemButton));
            panel.add(hiddenItemButton);

            // If any item buttons are added or removed, revalidate panel
            panel.revalidate();
            panel.repaint();
        }
    }
}
