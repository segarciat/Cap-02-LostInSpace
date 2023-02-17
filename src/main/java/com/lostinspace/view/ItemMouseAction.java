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
    private final AppView view;
    private final GUIController controller;

    private final ItemMod item;
    private final RoomPanel panel;
    private final JButton button;

    // constants
    private final int INVENTORY_SIZE = 48;
    private final int TIMER_DELAY = 3000;

    ItemMouseAction(AppView view, GUIController controller, ItemMod item, RoomPanel panel, JButton button) {
        this.view = view;
        this.controller = controller;
        this.item = item;
        this.panel = panel;
        this.button = button;
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        Timer timer = new Timer(TIMER_DELAY, e1 -> {
            view.setRoute(Route.WIN);
            view.update();
        });
        timer.setRepeats(false);

        if (e.getButton() == MouseEvent.BUTTON1) {
            // Change text area text to the look description of the item
            String lookDescription = controller.lookItem(item);
            setRoomAreaText(lookDescription);

        } else if (e.getButton() == MouseEvent.BUTTON3) {
            if (item.getItemMethod() == null)
                return;

            String textDescription = "";

            /*
             * If a player 'gets' an item, the text area is updated to notify the player
             * In addition, the item button is removed from the panel
             */
            if (item.getItemMethod().equals("get")) {
                textDescription = controller.getItem(item);
                addButtonToInventory(item);
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

                ItemMod requiredItemInInventory = controller.getModel().returnItemFromInventory(item.getRequiredItem());

                if (item.getRequiredItem() == null) {
                    if (item.isUsed() && item.getHiddenItem() != null)
                        revealHiddenItem(item);
                } else if (requiredItemInInventory != null) {
                    if (requiredItemInInventory.isUsed() && item.getHiddenItem() != null)
                        revealHiddenItem(item);
                }
            }

            panel.updateView();
            setRoomAreaText(textDescription);

            // Check winning condition
            if (winGameConditionMet(textDescription)) {
                timer.start();
            }
        }
    }

    /**
     * Checks if the winning conditions have been met
     * @param textDescription String textDescription returned from the ItemController
     * @return boolean whether the condition has been met
     */
    public boolean winGameConditionMet(String textDescription) {
        ItemMod ship = controller.getModel().getItemByName("ship");
        String useDescription = ship.getUseDescription();

        if (textDescription.equals(useDescription)) {
            return true;
        }

        return false;
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

    /**
     * Add item button to inventory
     * This is only for immediate addition to the inventory panel
     * This button will be removed when the player switches room and will be recreated in the RoomPanel updateView
     * method
     */
    private void addButtonToInventory(ItemMod item) {
        // Get inventory size
        int inventorySize = controller.getPlayer().getInventory().size();

        // Place the image of the item depending on number of items in the inventory
        Rectangle item1Area = new Rectangle(515, 40, INVENTORY_SIZE, INVENTORY_SIZE);
        Rectangle item2Area = new Rectangle(607, 40, INVENTORY_SIZE, INVENTORY_SIZE);
        Rectangle item3Area = new Rectangle(515, 100, INVENTORY_SIZE, INVENTORY_SIZE);
        Rectangle item4Area = new Rectangle(607, 100, INVENTORY_SIZE, INVENTORY_SIZE);
        Rectangle item5Area = new Rectangle(515, 160, INVENTORY_SIZE, INVENTORY_SIZE);
        Rectangle item6Area = new Rectangle(607, 160, INVENTORY_SIZE, INVENTORY_SIZE);

        Rectangle itemArea = new Rectangle();

        /*
         * Depending on how many items are in the inventory, place the new item the same spot
         * The item is added to the player's inventory BEFORE this method is called
         * Example: If the player has 0 items and picks up 1 item, then place the new item in spot 1
         */
        switch(inventorySize) {
            case 1:
                itemArea = item1Area;
                break;
            case 2:
                itemArea = item2Area;
                break;
            case 3:
                itemArea = item3Area;
                break;
            case 4:
                itemArea = item4Area;
                break;
            case 5:
                itemArea = item5Area;
                break;
            case 6:
                itemArea = item6Area;
                break;
            default:
                break;
        }

        JButton inventoryItemButton = SwingComponentCreator.createButtonWithImage(item.getImage(), itemArea);
        inventoryItemButton.addMouseListener(new InventoryItemAction(controller, item, panel));
        panel.add(inventoryItemButton);

        panel.repaintPanel();
    }

    /**
     * Remove item button from panel
     */
    private void removeButtonFromPanel() {
        panel.remove(button);

        panel.repaintPanel();
    }

    /*
     * If item that was being interacted with has a hidden item, then add it to the panel after being interacted with
     */
    private void revealHiddenItem(ItemMod item) {
        if (item.getHiddenItem() != null) {
            ItemMod hiddenItem = controller.getHiddenItem(item);
            item.setHiddenItem(null);

            addHiddenItemToPanel(hiddenItem);
        }
    }

    /**
     * Add hidden item button to panel (acts as a normal item button)
     * @param hiddenItem Item that was previously hidden and will now be shown.
     */
    private void addHiddenItemToPanel(ItemMod hiddenItem) {
        JButton hiddenItemButton = SwingComponentCreator.createButtonWithImage(hiddenItem.getImage(),
                hiddenItem.getRectangle());

        hiddenItemButton.addMouseListener(new ItemMouseAction(view, controller, hiddenItem, panel, hiddenItemButton));
        panel.add(hiddenItemButton);

        panel.repaintPanel();
    }
}
