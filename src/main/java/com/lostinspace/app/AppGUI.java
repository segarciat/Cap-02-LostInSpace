package com.lostinspace.app;

import com.lostinspace.controller.GUIController;
import com.lostinspace.model.Room;
import com.lostinspace.view.*;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;

public class AppGUI {
    // size of objects
//    private static final int FRAME_WIDTH = 736;
//    private static final int FRAME_HEIGHT = 758;
    private static final int FRAME_WIDTH = 720;
    private static final int FRAME_HEIGHT = 720;

    // titles
    private static final String GAME_TITLE = "Lost In Space";

    // java swing components
    private final JFrame frame;
    Map<String, RoomPanel> roomFrames;

    private TitlePanel titlePanel;
    private IntroPanel introPanel;
    private MenuPanel menuPanel;
    private RoomPanel roomPanel;

    // controllers
    private final GUIController controller;

    // other
    private Route route = Route.TITLE;               // routing section of story

    /*
     * Main method
     */
    public static void main(String[] args) {
        AppGUI app = new AppGUI();
        app.execute();
    }

    /*
     * GUI Constructor
     * Initializes the fields, controllers, and JFrame
     */
    public AppGUI() {
        frame = new JFrame();
        setFrameAttributes();

        controller = new GUIController();
        createRooms();
        titlePanel = new TitlePanel(this);
        menuPanel = new MenuPanel(this);
        introPanel = new IntroPanel(this);

        frame.addKeyListener(new KeyListener() { // shows a message when enter key is pressed
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    // if the route is game, show the menu
                    if (Route.GAME.equals(route)) {
                        setRoute(Route.MENU);
                        execute();

                    } else if (Route.MENU.equals(route)) {
                        // if the route is menu, it should hide the menu
                        setRoute(Route.GAME);
                        execute();
                    }

                }

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });;

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
    public void execute() {
        switch(route) {
            case TITLE:
                createTitle();
                break;
            case PROLOGUE:
                createPrologue();
                break;
            case GAME:
                createGame();
                break;
            case MENU:
                createMenu();
                break;
            default:
                break;
        }
    }

    private void createMenu() {
        frame.setContentPane(menuPanel);
        frame.requestFocus();
    }

    /*
     * Create title frame
     */
    private void createTitle() {
        frame.setContentPane(titlePanel);
//        frame.revalidate();
        frame.setVisible(true);
        frame.requestFocus();
    }

    /*
     * Create prologue sequence
     */
    private void createPrologue() {
        frame.setContentPane(introPanel);
        frame.requestFocus();
    }

    /*
     * Create game
     */
    private void createGame() {

        String startingLocation = controller.getPlayer().getCurrentRoom();
        frame.setContentPane(roomFrames.get(startingLocation));
        frame.revalidate();
        AppGUI app = this;

    }

    /*
     * Create objects for the game
     */
    private void createRooms() {
        roomFrames = new HashMap<>();

        Map<String, Room> roomMap = controller.getRoomMap();

        for (String roomName: roomMap.keySet()) {
            Room room = roomMap.get(roomName);
            roomFrames.put(roomName, new RoomPanel(this, room));
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

    public Map<String, RoomPanel> getRoomFrames() {
        return roomFrames;
    }

    public GUIController getController() {
        return controller;
    }
}
