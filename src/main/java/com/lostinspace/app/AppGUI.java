package com.lostinspace.app;

import com.lostinspace.controller.ViewController;
import com.lostinspace.model.Room;
import com.lostinspace.view.IntroPanel;
import com.lostinspace.view.RoomPanel;
import com.lostinspace.view.Route;
import com.lostinspace.view.TitlePanel;

import javax.swing.*;
import java.util.*;

public class AppGUI {

    // size of objects
    private static final int FRAME_WIDTH = 736;
    private static final int FRAME_HEIGHT = 758;

    // titles
    private static final String GAME_TITLE = "Lost In Space";

    // java swing components
    private final JFrame frame;
    Map<String, RoomPanel> roomFrames;

    // controllers
    private final ViewController controller;

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

        controller = new ViewController();
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
            default:
                break;
        }
    }

    /*
     * Create title frame
     */
    private void createTitle() {
        JPanel titlePanel = new TitlePanel(this);
        frame.setContentPane(titlePanel);
        frame.setVisible(true);
        frame.requestFocus();
    }

    /*
     * Create prologue sequence
     */
    private void createPrologue() {
        JPanel introPanel = new IntroPanel(this);
        frame.setContentPane(introPanel);
    }

    /*
     * Create game
     */
    private void createGame() {
        createRooms();
        String startingLocation = controller.getPlayer().getCurrentRoom();
        frame.setContentPane(roomFrames.get(startingLocation));

        controller.test();
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

    public ViewController getController() {
        return controller;
    }
}
