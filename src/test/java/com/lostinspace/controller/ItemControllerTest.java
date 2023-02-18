package com.lostinspace.controller;

import com.lostinspace.model.ItemMod;
import com.lostinspace.model.Model;
import com.lostinspace.model.Officer;
import com.lostinspace.model.Player;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ItemControllerTest {
    private GUIController controller;

    @Before
    public void setUp() throws Exception {
        controller = new GUIController(new Model());
    }

    @Test
    public void player_useKeyItem_addToOfficerInventory() {
        Player player = controller.getPlayer();
        Officer officer = controller.getModel().getOfficerZhang();
        String[] keyItems = new String[] {"component", "tool", "manual"};

        // Add key items to the player's inventory
        for (String item : keyItems) {
            ItemMod keyItem = controller.getModel().getItemByName(item);
            player.addToInventory(keyItem);
        }

        // Add each item to the Officer's inventory
        for (ItemMod itemInInventory : player.getInventory()) {
            controller.getItemController().useInventoryItem(itemInInventory);
        }

        for (int i = 0; i < officer.getInventory().size(); i++) {
            Assert.assertEquals(officer.getInventory().get(i).getName(), keyItems[i]);
        }
    }

    @Test
    public void player_useNonKeyItem_notAddToOfficerInventory() {
        Player player = controller.getPlayer();
        Officer officer = controller.getModel().getOfficerZhang();
        String[] keyItems = new String[] {"key", "keycard", "scrambler"};

        // Add key items to the player's inventory
        for (String item : keyItems) {
            ItemMod keyItem = controller.getModel().getItemByName(item);
            player.addToInventory(keyItem);
        }

        // Add each item to the Officer's inventory
        for (ItemMod itemInInventory : player.getInventory()) {
            controller.getItemController().useInventoryItem(itemInInventory);
        }

        Assert.assertEquals(0, officer.getInventory().size(), 0.001);
    }
}