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

    /*
     * Normal mode
     * When the player has an oxygen level of 0, if the player tries to move to another room, they should be sent to
     * the end game area (Enviro-Field)
     */
    @Test
    public void player_normalMode_oxygenLevelZero_whenMoveRoom_goToEnviroField() {
        Player player = controller.getPlayer();
        player.setOxygen(0);

        controller.movePlayer("Junction Hallway 1");

        assertEquals(player.getCurrentRoom(), "Enviro-Field");
    }

    /*
     * Easy-mode
     * When the player has an oxygen level of 0, if the player tries to move to another room, should be able to move
     * rooms since oxygen depletion is turned off
     */
    @Test
    public void player_easyMode_oxygenLevelZero_ableToMoveRoom() {
        Player player = controller.getPlayer();
        player.setOxygen(0);
        controller.toggleEasyMode();

        controller.movePlayer("Junction Hallway 1");

        assertEquals(player.getCurrentRoom(), "Junction Hallway 1");
    }

    /*
     * Easy-mode
     * When the player has an oxygen level of 0, if the player tries to move to another room, should be able to move
     * rooms since oxygen depletion is turned off
     */
    @Test
    public void player_easyMode_oxygenLevelSame_whenMoveRoom() {
        Player player = controller.getPlayer();
        player.setOxygen(50);
        controller.toggleEasyMode();

        controller.movePlayer("Junction Hallway 1");

        assertEquals(player.getOxygen(), 50, 0.001);
    }
}