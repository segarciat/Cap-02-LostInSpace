package com.lostinspace.view;

import com.lostinspace.model.Room;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

class AppGUI {
    // resource file paths
    private static final String TITLE_SCREEN_IMAGE = "/images_title/title.gif";
    private static final String TITLE_SCREEN_IMAGE_FLY = "/images_title/title_fly.gif";
    private static final String BACKGROUND_IMAGE = "/images_title/background.jpg";
    private static final String BUTTON_START = "/images_title/start.png";
    private static final String BUTTON_EXIT = "/images_title/exit.png";
    private static final String BUTTON_SKIP = "/images_title/skip.png";


    // size of objects
    private static final int WINDOW_SIZE = 720;
    private static final int FRAME_WIDTH = 736;
    private static final int FRAME_HEIGHT = 758;
    private static final int BUTTON_WIDTH = 250;
    private static final int BUTTON_HEIGHT = 40;

    // titles
    private static final String GAME_TITLE = "Lost In Space";

    // java swing components
    private JFrame frame;
    private ImageIcon bgImage;
    private JLabel bgImageLabel;
    private JPanel panel;
    private JTextArea textArea;
    private JButton skipButton;
    private ActionListener skipButtonAction;
    private JLabel roomFrame;
    Map<String, JLabel> roomFrames;

    // controllers
    private ViewController controller;
    private SwingComponentCreator frameCreator;

    // other
    private Route route = Route.TITLE;               // routing section of story
    private Iterator<String> iterator;                      // iterator for text

    // font size
    private static final Font MONOSPACE_PLAIN_MED = new Font("Monospaced", Font.PLAIN, 14);
    private static final Font MONOSPACE_BOLD_MED = new Font("Monospaced", Font.BOLD, 14);

    // font colors
    private static final Color COLOR_WHITE = new Color(255, 255, 255);
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
        bgImage = new ImageIcon();
        bgImageLabel = new JLabel();
        panel = new JPanel();
        textArea = new JTextArea();
        skipButton = new JButton();
        skipButtonAction = e -> {};
        roomFrame = new JLabel();

        frame = new JFrame();
        frame.setTitle(GAME_TITLE);
        setFrameAttributes();

        // Frame key listener for 'enter' key
        frame.addKeyListener(new KeyListener() {
            private boolean isMovingOn = false;

            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    route = getRoute();
                    Iterator<String> iterator = getIterator();

                    if (!iterator.hasNext()) {
                        isMovingOn = true;
                    }

                    switch(route) {
                        case PROLOGUE:
                            if (isMovingOn) {
                                setRoute(Route.TUTORIAL);
                                execute();
                            } else {
                                setTextArea(iterator);
                            }
                            break;
                        case TUTORIAL:
                            if (isMovingOn) {
                                setRoute(Route.GAME);
                                execute();
                            } else {
                                setTextArea(iterator);
                            }
                        default:
                            break;
                    }

                    isMovingOn = false;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        controller = new ViewController();
        this.frameCreator = new SwingComponentCreator();
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
            case TUTORIAL:
                createTutorial();
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
        // create timer object
        Timer timer = new Timer(2050, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setRoute(Route.PROLOGUE);
                execute();
            }
        });
        timer.setRepeats(false);                                                    // Can only execute once

        // Set background image icon
        bgImage = new ImageIcon(Objects.requireNonNull(this.getClass().getResource(TITLE_SCREEN_IMAGE)));

        // Add image to label
        bgImageLabel = frameCreator.createBGImageLabel(bgImage);

        // Create button to enter/exit game
        JButton startGameButton = frameCreator.createButtonWithImage(BUTTON_START, 100, 175, BUTTON_WIDTH,
                BUTTON_HEIGHT);
        JButton exitGameButton = frameCreator.createButtonWithImage(BUTTON_EXIT, 370, 175, BUTTON_WIDTH, BUTTON_HEIGHT);

        // Create action listeners
        ActionListener startGameButtonAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ImageIcon imageIcon = new ImageIcon(Objects.requireNonNull(this.getClass().getResource(TITLE_SCREEN_IMAGE_FLY)));
                bgImageLabel.setIcon(imageIcon);

                // Start timer - After time starts, the program will go into the prologue
                timer.start();

                // Remove buttons after timer stops
                bgImageLabel.remove(startGameButton);
                bgImageLabel.remove(exitGameButton);

                // Set frame as focus
                frame.requestFocus();
            }
        };

        ActionListener exitGameButtonAction = e -> System.exit(0);

        startGameButton.addActionListener(startGameButtonAction);
        exitGameButton.addActionListener(exitGameButtonAction);

        // Add buttons to label
        bgImageLabel.add(startGameButton);
        bgImageLabel.add(exitGameButton);

        frame.setContentPane(bgImageLabel);
        frame.setVisible(true);
    }

    /*
     * Create prologue sequence
     */
    private void createPrologue() {
        // Set background image icon
        bgImage = new ImageIcon(Objects.requireNonNull(this.getClass().getResource(BACKGROUND_IMAGE)));
        bgImageLabel.setIcon(bgImage);

        // Initialize iterator
        iterator = controller.getPrologue().lines().iterator();                         // Initialize iterator

        // Re-initialize after displaying first 13 lines to avoid pressing the 'enter' key
        iterator = frameCreator.createInitialFullScreenText(textArea, iterator, getRoute());

        // Create skip button
        skipButton = frameCreator.createButtonWithImage(BUTTON_SKIP, 235, 40, BUTTON_WIDTH, BUTTON_HEIGHT);

        // Create action listener
        skipButtonAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (iterator.hasNext()) {
                    // re-initialize, like 'enter' key
                    iterator = frameCreator.createInitialFullScreenText(textArea, iterator, getRoute());
                } else {
                    skipButton.removeActionListener(skipButtonAction);
                    setRoute(Route.TUTORIAL);
                    execute();
                }

                // Set frame as focus
                frame.requestFocus();
            }
        };

        skipButton.addActionListener(skipButtonAction);

        // Set text area attributes
        textArea.setSize(WINDOW_SIZE, WINDOW_SIZE);                          // size
        textArea.setEditable(false);                                         // non-editable
        textArea.setFocusable(false);                                       // prevent from stealing focus on click
        textArea.setOpaque(false);                                           // no background
        textArea.setLineWrap(true);                                          // wrap lines
        textArea.setWrapStyleWord(true);                                     // wrap by word
        textArea.setFont(MONOSPACE_PLAIN_MED);                               // font type
        textArea.setForeground(COLOR_GREEN);                                 // font color
        textArea.setMargin(new Insets(0,140,0,140));  // margins

        // Set panel attributes
        panel.setSize(WINDOW_SIZE, WINDOW_SIZE);
        panel.setOpaque(false);

        // Set panel layout
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(30, 0, 0, 0);
        panel.add(skipButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(0,0,0,0);
        gbc.weighty = 1.0;
        panel.add(textArea, gbc);

        // Add panel to frame
        frame.add(panel);
    }

    /*
     * Create tutorial frame
     */
    public void createTutorial() {
        // Re-initialize skip button action and re-add
        skipButtonAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setRoute(Route.GAME);
                execute();

                // Set frame as focus
                frame.requestFocus();
            }
        };
        skipButton.addActionListener(skipButtonAction);

        iterator = controller.getTutorial().lines().iterator();                         // Initialize iterator
        // Re-initialize after displaying first 13 lines to avoid pressing the 'enter' key
        iterator = frameCreator.createInitialFullScreenText(textArea, iterator, getRoute());
    }

    /*
     * Create game
     */
    private void createGame() {
        // Remove skip button action and button from panel
        skipButton.removeActionListener(skipButtonAction);
        panel.remove(skipButton);

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
        ImageIcon bgImageRoom = new ImageIcon();
        Map<String, Room> roomMap1 = controller.getRoomMap();
        Room room = roomMap1.get(roomName);
        String imageFileName = room.getImage();
        bgImageRoom = new ImageIcon(Objects.requireNonNull(this.getClass().getResource(imageFileName)));


        roomFrame = new JLabel(bgImageRoom);
        roomFrame.setSize(WINDOW_SIZE, WINDOW_SIZE);
        roomFrame.setLayout(new GridBagLayout());

        Map<String, Room> roomMap = controller.getRoomMap();

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

    public Iterator<String> getIterator() {
        return iterator;
    }

    private void setTextArea(Iterator <String> iterator) {
        frameCreator.createSubsequentFullScreenText(textArea, iterator);
    }

    public JFrame getFrame() {
        return frame;
    }
}
