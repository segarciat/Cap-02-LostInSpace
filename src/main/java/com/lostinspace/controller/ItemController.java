package com.lostinspace.controller;

import com.lostinspace.model.*;
import com.lostinspace.view.AppView;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/*
 * Controller for ItemMod objects
 */
class ItemController {
    private final List<String> POSTER_COLORS = List.of("Orange", "Yellow", "Pink", "Green", "Purple", "Blue");
    private final List<String> WRONG_COLORS = List.of("Red", "Black", "White", "Silver");

    // Fields
    private final Model model;
    private final AppView view;

    // Constructor
    public ItemController(Model model, AppView view) {
        // Get model from primary Controller ControllerGUI
        this.model = model;
        this.view = view;
    }

    /**
     * Add item to player inventory, then remove from location
     * @param item ItemMod item player is getting
     * @return String
     */
    public String getItem(ItemMod item) {
        // Add item to inventory
        model.getPlayer().addToInventory(item);

        // Get current Room object
        String roomName = model.getPlayer().getCurrentRoom();
        Room room = model.getRoomByName(roomName);

        // Remove item from room
        room.removeItemFromRoom(item.getName());

        return "You added the " + item.getName() + " to your inventory.";
    }

    /**
     * Interact with item on the map
     * @param item ItemMod item player is interacting with
     * @return String
     */
    public String interactItem(ItemMod item) {
        String itemDescription = "";

        /*
         * If item has already been used, then return early itemUSEDDescription
         * If not, if item is successfully interacted with, then set to used = true
         */
        if (item.isUsed()) {
            return item.getUsedDescription();
        }

        // If item is 'ship', then check inventory of Officer officerZhang object to check for win game condition
        if (item.getName().equals("ship")) {
            if (model.getOfficerZhang().getInventory().size() != 3) {
                // Return early
                return item.getFailedUseDescription();
            } else {
                // The player has met win game condition, send back to ControllerGUI
                GUIController.winGame();

                // Return early
                return itemDescription;
            }
        } else if (item.getName().equals("console")) {
            boolean isCorrect = useConsole();
            item.setUsed(isCorrect);
            return isCorrect? item.getUseDescription(): item.getFailedUseDescription();
        }

        // Check if interactable requires an item
        if (item.getRequiredItem() == null) {
            item.setUsed(true);
            itemDescription = item.getUseDescription();
        } else {
            // Get required item for interactable
            String requiredItem = item.getRequiredItem();
            ItemMod required = model.getItemByName(requiredItem);

            // If required item is not in inventory, then display failedUsedDescription
            if (model.checkInInventory(required)) {
                item.setUsed(true);
                itemDescription = item.getUseDescription();
            } else {
                itemDescription = item.getFailedUseDescription();
            }
        }

        // TODO: Check if an item can be used in a certain location

        return itemDescription;
    }

    public boolean useConsole() {
        // if the player has already used the console, do nothing.

        // Create panel to hold prompt questions and checkboxes.
        JPanel questionPanel = new JPanel( new FlowLayout( FlowLayout.LEFT) );
        questionPanel.add( new JLabel( UIManager.getIcon("OptionPane.questionIcon" ) ) );

        // Add the prompt question.
        JLabel promptLabel = new JLabel("What are the colors on the poster in the ship?");
        questionPanel.add(promptLabel);

        // Create panel for the boxes
        JPanel panel = new JPanel( new GridLayout(0, 1) );
        panel.add(questionPanel);

        // Create random colors to display as choices.
        List<String> colors = new ArrayList<>(POSTER_COLORS);
        colors.addAll(WRONG_COLORS);
        Collections.shuffle(colors);

        // Create checkboxes for each of those colors and add them to the panel..
        List<JCheckBox> checkBoxes = colors.stream().map(JCheckBox::new).collect(Collectors.toList());

        // Add all checkboxes to the panel.
        checkBoxes.forEach(panel::add);

        int result = JOptionPane.showConfirmDialog(view.getFrame(),
                panel,
                "Console Puzzle",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        List<String> userResponse = checkBoxes.stream()
                .filter(JCheckBox::isSelected)
                .map(JCheckBox::getText)
                .collect(Collectors.toList());

        return userResponse.equals(POSTER_COLORS);
    }

    /**
     * Get hiddenItem as a ItemMod object with Rectangle properties from model class to pass back to RoomPanel to
     * create the JButton
     * @param item ItemMod item player is interacting with
     * @param roomName Room name of the current location of the player
     * @return ItemMod hiddenItem
     */
    public ItemMod getHiddenItem(ItemMod item, String roomName) {
        String hiddenItemName = item.getHiddenItem();

        return model.getHiddenItemByName(hiddenItemName, roomName);
    }
}
