package com.lostinspace.model;

import com.lostinspace.controller.Controller;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ItemUseMethodsTest {
    private static final String ROOM_WITH_PIPES = "Cockpit";
    private static final String ROOM_WITHOUT_PIPES = "Docking Bay";
    private static final String PIPES = "pipes";
    private Controller controller;
    private Player player;

    @Before
    public void setUp() throws Exception {
        controller = new Controller();

        // Set starting location to a room with pipes
        player = controller.getPlayer();
        player.setCurrentRoom(ROOM_WITH_PIPES);
    }

    @Test
    public void usePipes_shouldRestorePlayerOxygen() {
        // Set player's oxygen before using pipes
        player.setOxygen(10);

        // player should regain 25.5 oxygen, or ItemUseMethods.OXYGEN_REFILL
        double expected = 35.5;

        // use the pipes
        controller.useItem(controller.getInventory(), controller.getInteractables(), PIPES);

        // Make sure player's oxygen increased as expected.
        assertEquals(
                expected,
                player.getOxygen(),
                0.0001
        );
    }

    @Test
    public void usePipes_shouldCapPlayerOxygenAt100() {
        // Set player's oxygen before using pipes
        player.setOxygen(90);

        // player should regain 25.5 oxygen, but since it exceeds 100, it should be 100 afterwards.
        double expected = 100;

        // use the pipes
        controller.useItem(controller.getInventory(), controller.getInteractables(), PIPES);

        // Make sure player's oxygen increased as expected.
        assertEquals(
                expected,
                player.getOxygen(),
                0.0001
        );
    }

    @Test
    public void usePipes_shouldNotRestoreMoreOxygen_afterFirstUse() {
        // Set player's oxygen before using pipes
        player.setOxygen(40);

        // player should regain 25.5 oxygen after 1 use, and none after the second.
        double expected = 65.5;

        // use the pipes
        controller.useItem(controller.getInventory(), controller.getInteractables(), PIPES);

        // use them again
        controller.useItem(controller.getInventory(), controller.getInteractables(), PIPES);

        // Make sure player's oxygen increased as expected.
        assertEquals(
                expected,
                player.getOxygen(),
                0.0001
        );
    }

    @Test
    public void usePipes_shouldNotRestoreOxygen_whenRoomDoesNotHavePipes() {
        player.setCurrentRoom(ROOM_WITHOUT_PIPES);

        // Set player's oxygen before using pipes
        player.setOxygen(40);

        // current room does not have pipes, so using the command will not cause oxygen to change.
        double expected = 40;

        // use the pipes
        controller.useItem(controller.getInventory(), controller.getInteractables(), PIPES);

        // Make sure player's oxygen increased as expected.
        assertEquals(
                expected,
                player.getOxygen(),
                0.0001
        );
    }
}