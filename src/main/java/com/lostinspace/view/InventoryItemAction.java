package com.lostinspace.view;

import com.lostinspace.controller.GUIController;
import com.lostinspace.model.ItemMod;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Action that "inventory item buttons" responds with
 */
class InventoryItemAction implements MouseListener {
    private final GUIController controller;
    private final ItemMod item;
    private final RoomPanel panel;

    public InventoryItemAction(GUIController controller, ItemMod item, RoomPanel panel) {
        this.controller = controller;
        this.item = item;
        this.panel = panel;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        String textDescription = "";

        /*
         * Look at item when left-click
         * Use item when right-click
         */
        if (e.getButton() == MouseEvent.BUTTON1) {
            textDescription = controller.lookItem(item);
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            textDescription = controller.useItem(item);
        }

        setRoomAreaText(textDescription);
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
}
