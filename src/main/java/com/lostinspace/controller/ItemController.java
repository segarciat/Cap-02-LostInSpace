package com.lostinspace.controller;

import com.lostinspace.model.*;
import com.lostinspace.view.AppView;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/*
 * Controller for ItemMod objects
 */
class ItemController {
    public static final String SHIP = "ship";
    public static final String CONSOLE = "console";
    public static final String SCRAMBLER = "scrambler";
    public static final String AIRLOCK = "airlock";
    public static final String COMPONENT = "component";
    public static final String
            TOOL = "tool";
    public static final String
            MANUAL = "manual";
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
     *
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
     *
     * @param item ItemMod item player is interacting with
     * @return String indicating whether was already used, or if the item was just used successfully.
     */
    public String interactItem(ItemMod item) {
        /*
         * If item has already been used, then return early itemUSEDDescription
         * If not, if item is successfully interacted with, then set to used = true
         */
        if (item.isUsed())
            return item.getUsedDescription();

        ItemMod requiredItemInInventory = model.returnItemFromInventory(item.getRequiredItem());
        // An item is need and it has not been used.
        if (item.getRequiredItem() != null && (requiredItemInInventory == null || !requiredItemInInventory.isUsed()))
            return item.getFailedUseDescription();

        // Check if interactable requires an item and if player has it
        switch (item.getName()) {
            case SHIP:
                useShip();
                break;
            case SCRAMBLER:
                useScrambler(item);
                break;
            case CONSOLE:
                useConsole(item);
                break;
            case AIRLOCK:
                useAirlock(item);
                break;
//            case COMPONENT:
//            case TOOL:
//            case MANUAL:
//                model.getOfficerZhang().addItemToInventory(item);
//                item.setUsed(true);
//                break;
            default:
                item.setUsed(true);
                break;
        }

        return item.isUsed()? item.getUseDescription(): item.getFailedUseDescription();
    }


    private void useShip() {
        if (model.getOfficerZhang().getInventory().size() == 3) {
            // The player has met win game condition, send back to ControllerGUI
            GUIController.winGame();
        }
    }

    public void useConsole(ItemMod item) {
        // if the player has already used the console, do nothing.

        // Create panel to hold prompt questions and checkboxes.
        JPanel questionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        questionPanel.add(new JLabel(UIManager.getIcon("OptionPane.questionIcon")));

        // Add the prompt question.
        JLabel promptLabel = new JLabel("What are the colors on the poster in the ship?");
        questionPanel.add(promptLabel);

        // Create panel for the boxes
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(questionPanel);

        // Create random colors to display as choices.
        List<String> colors = new ArrayList<>(POSTER_COLORS);
        colors.addAll(WRONG_COLORS);
        Collections.shuffle(colors);

        // Create checkboxes for each of those colors and add them to the panel.
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

        item.setUsed(userResponse.equals(POSTER_COLORS));
    }

    public void useScrambler(ItemMod item) {
        // Ensure console was turned on (used)
        boolean powerIsOn = model.getRoomItems().values().stream()
                .flatMap(Collection::stream)
                .filter(i -> i.getName().equals(CONSOLE))
                .findFirst()
                .get()
                .isUsed();

        item.setUsed(powerIsOn);
    }

    private void useAirlock(ItemMod item) {
        // Ensure console was turned on (used)
        boolean scramblerWasUsed = model.getRoomItems().values().stream()
                .flatMap(Collection::stream)
                .filter(i -> i.getName().equals(SCRAMBLER))
                .findFirst()
                .get()
                .isUsed();

        item.setUsed(scramblerWasUsed);
    }

    /**
     * Get hiddenItem as a ItemMod object with Rectangle properties from model class to pass back to RoomPanel to
     * create the JButton
     *
     * @param item     ItemMod item player is interacting with
     * @param roomName Room name of the current location of the player
     * @return ItemMod hiddenItem
     */
    public ItemMod getHiddenItem(ItemMod item, String roomName) {
        String hiddenItemName = item.getHiddenItem();

        return model.getHiddenItemByName(hiddenItemName, roomName);
    }

    /**
     * Use the item the player clicks on
     *
     * @param inventoryItem ItemMod item player wants to use
     * @return String
     */
    public String useInventoryItem(ItemMod inventoryItem) {
        String textDescription = "";

        /*
         * If item has already been used, then return early itemUSEDDescription
         * If not, if item is successfully interacted with, then set to used = true
         */
        if (inventoryItem.isUsed()) {
            return inventoryItem.getUsedDescription();
        }

        // Check current location of the player
        String currentLocation = model.getPlayer().getCurrentRoom();

        // Check if item can be used
        if (!currentLocation.equals(inventoryItem.getUseLocation())) {
            textDescription = "You cannot use " + inventoryItem.getName() + " right now. It may have a use somewhere else.";
        } else {
            inventoryItem.setUsed(true);
            textDescription = inventoryItem.getUseDescription();
        }

        return textDescription;
    }
}
