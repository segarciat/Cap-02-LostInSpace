package com.lostinspace.view;

import com.lostinspace.model.Room;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

class AppGUI {

    // size of objects
    private static final int WINDOW_SIZE = 720;
    private static final int FRAME_WIDTH = 736;
    private static final int FRAME_HEIGHT = 758;

    // titles
    private static final String GAME_TITLE = "Lost In Space";

    // java swing components
    private final JFrame frame;
    private JLabel roomFrame;
    Map<String, JLabel> roomFrames;

    // controllers
    private final ViewController controller;

    // other
    private Route route = Route.TITLE;               // routing section of story

    // font size
    private static final Font MONOSPACE_BOLD_MED = new Font("Monospaced", Font.BOLD, 14);

    // font colors
    private static final Color COLOR_GREEN = new Color(76, 175, 82);

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
        roomFrame = new JLabel();

        frame = new JFrame();
        frame.setTitle(GAME_TITLE);
        setFrameAttributes();

        controller = new ViewController();
    }

    /*
     * Set frame attributes
     */
    private void setFrameAttributes() {
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
    }

    /*
     * Create objects for the game
     */
    // Create rooms
    private void createRooms() {
        roomFrames = new HashMap<>();

        for (String roomName : controller.getRoomMap().keySet()) {
            roomFrames.put(roomName, createRoomFrame(roomName));
        }
    }

    // create room frames
    private JLabel createRoomFrame(String roomName) {
        Map<String, Room> roomMap = controller.getRoomMap();
        Room room = roomMap.get(roomName);

        // Create room background.
        String imageFileName = room.getImage();
        ImageIcon bgImageRoom = new ImageIcon(Objects.requireNonNull(this.getClass().getResource(imageFileName)));
        roomFrame = new JLabel(bgImageRoom);
        roomFrame.setSize(WINDOW_SIZE, WINDOW_SIZE);
        roomFrame.setLayout(new GridBagLayout());

        // Get room description
        Room currentRoom = roomMap.get(roomName);
        JTextArea roomTextArea = new JTextArea(currentRoom.getDescription());

        // Set text area attributes
        roomTextArea.setForeground(COLOR_GREEN);
        roomTextArea.setFont(MONOSPACE_BOLD_MED);
        roomTextArea.setMinimumSize(new Dimension(WINDOW_SIZE, 168));
        roomTextArea.setLineWrap(true);
        roomTextArea.setWrapStyleWord(true);
        roomTextArea.setOpaque(false);
        roomTextArea.setEditable(false);
        roomTextArea.setFocusable(false);
        roomTextArea.setMargin(new Insets(12,24,0,24));

        // Set spacer text area
        JTextArea spacer = new JTextArea();
        spacer.setOpaque(false);
        spacer.setEditable(false);
        spacer.setFocusable(false);

        // Create layout constraints
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipady = 504;
        roomFrame.add(spacer, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.ipady = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        roomFrame.add(roomTextArea, gbc);

        // Create direction buttons for each exit
        Map<String, String> roomExits = currentRoom.getExits();
        Map<String, String> roomExitDescriptions = currentRoom.getExit_descriptions();

        JPanel buttonPane = new JPanel();
        buttonPane.setOpaque(false);

        for (String exit: roomExits.keySet()) {
            JButton directionButton = new JButton(String.format("Go %s", exit));

            directionButton.addActionListener((e) -> {
                String exitRoomName = roomExits.get(exit);
                roomTextArea.setText(roomExitDescriptions.get(exitRoomName));

                // Set time for room transition
                Timer timer = new Timer(1500, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        frame.setContentPane(roomFrames.get(exitRoomName));
                    }
                });
                timer.setRepeats(false);

                timer.start();
            });

            buttonPane.add(directionButton);
        }

        gbc.gridy = 1;
        roomFrame.add(buttonPane, gbc);
        return roomFrame;
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

    public ViewController getController() {
        return controller;
    }
}
