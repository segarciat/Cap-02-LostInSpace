package com.lostinspace.view;

import com.lostinspace.controller.GUIController;
import com.lostinspace.model.Model;
import com.lostinspace.model.Room;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;

public class AppView {
    // size of objects
    private static final int FRAME_WIDTH = 720;
    private static final int FRAME_HEIGHT = 720;

    // titles
    private static final String GAME_TITLE = "Lost In Space";

    // java swing components
    private final JFrame frame;
    Map<String, RoomPanel> roomFrames;

    private final TitlePanel titlePanel;
    private final IntroPanel introPanel;
    private final MenuPanel menuPanel;

    // controllers
    private final GUIController controller;
    private final Model model;

    // other
    private Route route = Route.TITLE;               // routing section of story

    /*
     * GUI Constructor
     * Initializes the fields, controllers, and JFrame
     */
    public AppView(GUIController controller, Model model) {
        this.controller = controller;
        this.model = model;

        frame = new JFrame();
        setFrameAttributes();

        createRooms();
        titlePanel = new TitlePanel(this);
        menuPanel = new MenuPanel(this);
        introPanel = new IntroPanel(this);

        frame.addKeyListener(new MenuToggleAction());
    }

    /*
     * Set frame attributes
     */
    private void setFrameAttributes() {
        frame.setTitle(GAME_TITLE);
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);                           // Set frame size
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);               // Closes window and terminates
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        frame.setResizable(false);
    }

    /*
     * Entry into App GUI
     */
    public void update() {
        switch (route) {
            case TITLE:
                showTitleScreen();
                break;
            case PROLOGUE:
                showPrologue();
                break;
            case GAME:
                showGame();
                break;
            case MENU:
                showMenu();
                break;
            default:
                break;
        }
    }

    private void showMenu() {
        frame.setContentPane(menuPanel);
        frame.requestFocus();
    }

    /*
     * Create title frame
     */
    private void showTitleScreen() {
        frame.setContentPane(titlePanel);
//        frame.revalidate();
        frame.setVisible(true);
        frame.requestFocus();
    }

    /*
     * Create prologue sequence
     */
    private void showPrologue() {
        frame.setContentPane(introPanel);
        frame.requestFocus();
    }

    /*
     * Create game
     */
    private void showGame() {
        String currentRoomName = controller.getPlayer().getCurrentRoom();
        frame.setContentPane(roomFrames.get(currentRoomName));
        frame.revalidate();
    }

    /*
     * Create objects for the game
     */
    private void createRooms() {
        roomFrames = new HashMap<>();

        Map<String, Room> roomMap = controller.getRoomMap();

        for (String roomName : roomMap.keySet()) {
            Room room = roomMap.get(roomName);
            roomFrames.put(roomName, new RoomPanel(this, room, controller));
        }
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public JFrame getFrame() {
        return frame;
    }

    public GUIController getController() {
        return controller;
    }

    /**
     * Allows player to toggle (open/close) the menu when pressing ESC key.
     */
    private class MenuToggleAction implements KeyListener {
        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                // if the route is game, show the menu
                if (Route.GAME.equals(route)) {
                    setRoute(Route.MENU);
                    update();

                } else if (Route.MENU.equals(route)) {
                    // if the route is menu, it should hide the menu
                    setRoute(Route.GAME);
                    update();
                }

            }

        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
    }
}
