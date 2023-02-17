package com.lostinspace.controller;

import com.lostinspace.model.ItemMod;
import com.lostinspace.model.Model;
import com.lostinspace.model.Player;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class GUIConsoleControllerTest {

    private GUIController controller;

    @Before
    public void setUp() throws Exception {
        controller = new GUIController(new Model());
    }

    @Test
    public void interactItem_whenItemIsPipes_shouldRestorePlayerOxygen() {
        // Set player oxygen to 0 to test that oxygen increases by the expected amount.
        Player player = controller.getPlayer();
        player.setOxygen(0);

        ItemMod pipes = controller.getModel().getItems().get("pipes");

        controller.interactItem(pipes);

        assertEquals(player.getOxygen(), 25, 0.0001);
    }

    @Test
    public void interactItem_whenItemIsPipes_andPipesHaveAlreadyBeenUsed_doNotReplenishOxygen() {
        // Set player oxygen to 0 to test that oxygen increases by the expected amount.
        Player player = controller.getPlayer();
        player.setOxygen(0);

        ItemMod pipes = controller.getModel().getItems().get("pipes");

        // Oxygen goes up to 25.
        controller.interactItem(pipes);

        // Oxygen should stay at 25, since pipes have already been used.
        controller.interactItem(pipes);

        assertEquals(player.getOxygen(), 25, 0.0001);
    }

    @Test
    public void interactItem_whenItemIsPipes_playerOxygenShouldNotExceedMaximum() {
        double maximumOxygen = 100.0;
        Player player = controller.getPlayer();
        player.setOxygen(maximumOxygen);

        ItemMod pipes = controller.getModel().getItems().get("pipes");

        // Oxygen should not exceed the maximum.
        controller.interactItem(pipes);

        assertEquals(player.getOxygen(), maximumOxygen, 0.0001);
    }
}