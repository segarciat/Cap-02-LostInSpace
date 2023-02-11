package com.lostinspace.view;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Iterator;
import java.util.Objects;

class AppGUI {
    // resource file paths
    private static final String TITLE_SCREEN_IMAGE = "/images_title/title.gif";
    private static final String TITLE_SCREEN_IMAGE_FLY = "/images_title/title_fly.gif";
    private static final String BACKGROUND_IMAGE = "/images_title/background.jpg";
    private static final String BUTTON_START = "/images_title/start.png";
    private static final String BUTTON_EXIT = "/images_title/exit.png";
    private static final String BUTTON_SKIP = "/images_title/skip.png";
    private static final String LABEL_OBJECTIVE = "/images_title/objective.png";

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

    // controllers
    private ViewController controller;
    private FrameCreator frameCreator;

    // other
    private Route route = new Route("Title");               // routing section of story
    private Iterator<String> iterator;                      // iterator for text

    // font size
    private static final Font MONOSPACE_PLAIN_MED = new Font("Monospaced", Font.PLAIN, 14);

    // font colors
    private static final Color COLOR_GREEN = new Color(76, 175, 82);

    public static void main(String[] args) {
        AppGUI app = new AppGUI();
        app.execute();
    }

    /*
     * GUI constructor
     * Initializes the fields, controllers, and JFrame
     */
    public AppGUI() {
        bgImage = new ImageIcon();
        bgImageLabel = new JLabel();
        panel = new JPanel();
        textArea = new JTextArea();
        skipButton = new JButton();
        skipButtonAction = e -> {};

        frame = new JFrame();
        frame.setTitle(GAME_TITLE);
        setFrameAttributes();

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

                    switch(route.getRoute()) {
                        case "Title":
                            break;
                        case "Prologue":
                            if (isMovingOn) {
                                setRoute(new Route("Tutorial"));
                                execute();
                            } else {
                                setTextArea(iterator);
                            }
                            break;
                        case "Tutorial":
                            if (isMovingOn) {
                                setRoute(new Route("Game"));
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
        this.frameCreator = new FrameCreator();
    }

    private void setFrameAttributes() {
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);                           // Set frame size
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);               // Closes window and terminates
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        frame.setResizable(false);
    }

    public void execute() {
        switch(route.getRoute()) {
            case "Title":
                createTitle();
                break;
            case "Prologue":
                createPrologue();
                break;
            case "Tutorial":
                createTutorial();
                break;
            case "Game":
                createGame();
                break;
            default:
                break;
        }
    }

    private void createTitle() {
        // create timer object
        Timer timer = new Timer(2050, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                route.setRoute("Prologue");
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
                    setRoute(new Route("Tutorial"));
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

    public void createTutorial() {
        // Re-initialize skip button action and re-add
        skipButtonAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setRoute(new Route("Game"));
                execute();

                // Set frame as focus
                frame.requestFocus();
            }
        };
        skipButton.addActionListener(skipButtonAction);

        iterator = controller.getTutorialsText().lines().iterator();                         // Initialize iterator
        // Re-initialize after displaying first 13 lines to avoid pressing the 'enter' key
        iterator = frameCreator.createInitialFullScreenText(textArea, iterator, getRoute());
    }

    private void createGame() {
        // Remove skip button action and button from panel
        skipButton.removeActionListener(skipButtonAction);
        panel.remove(skipButton);

        String currentRoom = controller.getCurrentRoom();

        textArea.setText("HI");
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
}
