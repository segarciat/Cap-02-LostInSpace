package com.lostinspace.view;

import com.lostinspace.app.GUIApp;
import com.lostinspace.controller.GUIController;
import com.lostinspace.model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.ArrayList;
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
    public static final int TEXTAREA_HEIGHT = 168;

    // other
    public static final int ROOM_TRANSITION_DELAY = 1500;
    public static final String ENVIRO_FIELD = "Enviro-Field";
    public static final Color THEME_COLOR = new Color( 76, 175, 83);
    public static final Color WARNING_OXYGEN_COLOR = new Color(246, 190, 0);
    private final int INVENTORY_SIZE = 48;

    // The text being shown in the room.
    private final JTextArea roomTextArea;
    private final GUIController controller;
    private final JPanel inventoryButtonsPane;
    private final JProgressBar oxygenBar;
    private final JPanel directionButtonsPane;

    private final Room room;

    public RoomPanel(AppView view, Room room, GUIController controller) {
        super(room.getImage(), view.getFrame().getWidth(), view.getFrame().getHeight());

        this.controller = controller;
        this.room = room;
        this.oxygenBar = new JProgressBar();
        oxygenBar.setStringPainted(true);
        oxygenBar.setBounds(20, 75, 200, 25);
        updateOxygenBar();

        // Set up frame attributes
        this.setLayout(null);
        this.setSize(this.getPreferredSize());

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
        directionButtonsPane = new JPanel();
        directionButtonsPane.setOpaque(false);
        directionButtonsPane.setBounds(0, WINDOW_SIZE -TEXTAREA_HEIGHT - 64, WINDOW_SIZE, 48);

        // Create direction buttons and add to panel
        for (String exit: roomExits.keySet()) {
            // If the room is NOT the Enviro-Field
            if  (!room.getName().equals(ENVIRO_FIELD)) {
                JButton directionButton = SwingComponentCreator.createButtonWithText(String.format("Go %s", exit));
                directionButton.addActionListener(new RoomExitAction(view, room, exit));
                directionButtonsPane.add(directionButton);

                // Add the oxygen bar
                this.add(oxygenBar);
            } else {
                JButton restartButton = SwingComponentCreator.createRestartButton(view);
                directionButtonsPane.add(restartButton);

                JButton exitButton = SwingComponentCreator.createExitButton();

                directionButtonsPane.add(exitButton);
            }

        }

        Model model = view.getController().getModel();

        // Add items for this room
        Set<ItemMod> itemMods = model.getRoomItems().get(room.getName());
        for (ItemMod item: itemMods) {
            if (item.getImage() != null && !item.isHidden()) {
                JButton button = SwingComponentCreator.createButtonWithImage(item.getImage(), item.getRectangle());
                button.addMouseListener(new ItemMouseAction(view, controller, item, this, button));
                this.add(button);
            }
        }

        // Create inventory item button pane
        inventoryButtonsPane = new JPanel();
        inventoryButtonsPane.setLayout(null);
        inventoryButtonsPane.setOpaque(false);
        inventoryButtonsPane.setBounds(WINDOW_SIZE - 245, 5, 225, 215);

        this.add(inventoryButtonsPane);
        this.add(directionButtonsPane);
    }

    public JPanel getDirectionButtonsPane() {
        return directionButtonsPane;
    }

    /*
     * Updates the display for the inventory
     */
    private void updateInventory() {
        removeInventoryItems();

        ArrayList<ItemMod> playerInventory = controller.getPlayer().getInventory();

        for (int i = 0; i <  playerInventory.size(); i++) {
            ItemMod item = playerInventory.get(i);

            createInventoryItem(item, i);
        }
    }

    /*
     * Creates each inventory item button and adds to the inventoryButtonsPane
     */
    private void createInventoryItem(ItemMod item, int index) {
        // Place the image of the item depending on number of items in the inventory and index
        Rectangle item1Area = new Rectangle(40, 35, INVENTORY_SIZE, INVENTORY_SIZE);
        Rectangle item2Area = new Rectangle(132, 35, INVENTORY_SIZE, INVENTORY_SIZE);
        Rectangle item3Area = new Rectangle(40, 95, INVENTORY_SIZE, INVENTORY_SIZE);
        Rectangle item4Area = new Rectangle(132, 95, INVENTORY_SIZE, INVENTORY_SIZE);
        Rectangle item5Area = new Rectangle(40, 155, INVENTORY_SIZE, INVENTORY_SIZE);
        Rectangle item6Area = new Rectangle(132, 155, INVENTORY_SIZE, INVENTORY_SIZE);

        Rectangle itemArea = new Rectangle();

        /*
         * Depending on how many items are in the inventory, place the new item the same spot
         * The item is added to the player's inventory BEFORE this method is called
         * Example: If the player has 0 items and picks up 1 item, then place the new item in spot 1
         */
        switch(index) {
            case 0:
                itemArea = item1Area;
                break;
            case 1:
                itemArea = item2Area;
                break;
            case 2:
                itemArea = item3Area;
                break;
            case 3:
                itemArea = item4Area;
                break;
            case 4:
                itemArea = item5Area;
                break;
            case 5:
                itemArea = item6Area;
                break;
            default:
                break;
        }

        JButton inventoryItemButton = SwingComponentCreator.createButtonWithImage(item.getImage(), itemArea);
        inventoryItemButton.addMouseListener(new InventoryItemAction(controller, item, this));
        inventoryButtonsPane.add(inventoryItemButton);
    }

    /*
     * Remove items from the inventory, then reimplement the buttons for persistence on screen
     */
    private void removeInventoryItems() {
        for (Component itemButton : inventoryButtonsPane.getComponents()) {
            inventoryButtonsPane.remove(itemButton);
        }
    }

    /**
     * Displays the player's current oxygen in a certain color according to the amount.
     */
    private void updateOxygenBar() {
        int oxygen = (int) controller.getPlayer().getOxygen();
        oxygenBar.setString(String.format("Oxygen: %s", oxygen));
        oxygenBar.setBackground(Color.BLACK);
        oxygenBar.setValue(oxygen);
        Color color;
        if (oxygen > 70)
            color = THEME_COLOR;
        else if (oxygen > 30)
            color = WARNING_OXYGEN_COLOR;
        else
            color = Color.RED;
        oxygenBar.setForeground(color);
    }

    public void updateView() {
        updateOxygenBar();
        updateInventory();
    }

    public JTextArea getRoomTextArea() {
        return roomTextArea;
    }
}
