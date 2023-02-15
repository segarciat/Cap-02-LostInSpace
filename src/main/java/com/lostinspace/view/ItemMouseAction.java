package com.lostinspace.view;

import com.lostinspace.controller.GUIController;
import com.lostinspace.model.ItemMod;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Action that "item buttons" responds when hovering, left-, or right-clicking (highlighting, GET/USE/LOOK actions).
 */
class ItemMouseAction implements MouseListener {
    private final GUIController controller;
    private final ItemMod item;
    private final RoomPanel panel;
    private final JButton button;

    ItemMouseAction(GUIController controller, ItemMod item, RoomPanel panel, JButton button) {
        this.controller = controller;
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
            setRoomAreaText(lookDescription);

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

                // Check if interactable item requires an item
                if (item.getRequiredItem() != null) {
                    String requiredItemName = item.getRequiredItem();
                    ItemMod requiredItem = controller.getModel().getItemByName(requiredItemName);

                    // If required item is in the player's inventory, then reveal hidden item
                    if (controller.getModel().checkInInventory(requiredItem)) {
                        // Reveal hidden item
                        if (item.getHiddenItem() != null) {
                            revealHiddenItem(item);
                        }
                    }
                } else {
                    // Reveal hidden item
                    if (item.getHiddenItem() != null) {
                        revealHiddenItem(item);
                    }
                }
            }

            setRoomAreaText(textDescription);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    /*
     * When user's mouse enters the item area rectangle, add border
     */
    @Override
    public void mouseEntered(MouseEvent e) {
        JButton button = (JButton) e.getSource();
        button.setBorder(new LineBorder(Color.PINK)); // add border color on hover
    }

    /*
     * When user's mouse exits the item area rectangle, remove border
     */
    @Override
    public void mouseExited(MouseEvent e) {
        JButton button = (JButton) e.getSource();
        button.setBorder(BorderFactory.createEmptyBorder()); // empty border when not hovering
    }

    /**
     * Set text of the roomTextArea on the RoomPanel
     * @param text text to be displayed
     */
    private void setRoomAreaText(String text) {
        JTextArea textArea = panel.getRoomTextArea();
        textArea.setText(text);
    }

    // TODO: Add item buttons to the inventory when the player 'gets' an item
    /**
     * Add item button to inventory
     */
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

    /*
     * If item that was being interacted with has a hidden item, then add it to the panel after being interacted with
     */
    private void revealHiddenItem(ItemMod item) {
        if (item.getHiddenItem() != null) {
            ItemMod hiddenItem = controller.getHiddenItem(item);
            addHiddenItemToPanel(hiddenItem);
            item.setHiddenItem(null);
        }
    }

    /**
     * Add hidden item button to panel (acts as a normal item button)
     * @param hiddenItem Item that was previously hidden and will now be shown.
     */
    private void addHiddenItemToPanel(ItemMod hiddenItem) {
        JButton hiddenItemButton = SwingComponentCreator.createButtonWithImage(hiddenItem.getImage(),
                hiddenItem.getRectangle());
        hiddenItemButton.setName(hiddenItem.getName());

        hiddenItemButton.addMouseListener(new ItemMouseAction(controller, hiddenItem, panel, hiddenItemButton));
        panel.add(hiddenItemButton);

        // If any item buttons are added or removed, revalidate panel
        panel.revalidate();
        panel.repaint();
    }
}
