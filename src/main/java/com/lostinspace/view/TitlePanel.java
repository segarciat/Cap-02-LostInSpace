package com.lostinspace.view;

import com.lostinspace.app.AppGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Objects;

// Creates the title JPanel for the starting panel of the game
public class TitlePanel extends ImagePanel {
    // Background image paths
    private static final String TITLE_SCREEN_IMAGE = "/images_title/title.gif";
    private static final String TITLE_SCREEN_IMAGE_FLY = "/images_title/title_fly.gif";

    // Button width and height
    private static final int BUTTON_WIDTH = 250;
    private static final int BUTTON_HEIGHT = 40;

    // Button image path
    private static final String BUTTON_START = "/images_title/start.png";
    private static final String BUTTON_EXIT = "/images_title/exit.png";

    // Transition delay time for transitioning
    public static final int TRANSITION_DELAY = 2050;

    // Obejct fields
    private final Image startMenuTransitionImage;
    private final JButton startGameButton;
    private final JButton exitGameButton;

    // Instance of AppGUI
    private final AppGUI app;

    // Constructor
    public TitlePanel(AppGUI app) {
        super(TITLE_SCREEN_IMAGE, app.getFrame().getWidth(), app.getFrame().getHeight());
        this.app = app;

        // Get frame from the AppGUI
        JFrame frame = app.getFrame();

        // Set the transition image after the player presses the "START" button
        startMenuTransitionImage = new ImageIcon(Objects.requireNonNull(this.getClass().getResource(TITLE_SCREEN_IMAGE_FLY)))
                .getImage()
                .getScaledInstance(frame.getWidth(), frame.getHeight(), Image.SCALE_DEFAULT);

        // Create button to enter/exit game
        startGameButton = SwingComponentCreator.createButtonWithImage(BUTTON_START, 100, 175, BUTTON_WIDTH,
                BUTTON_HEIGHT);
        exitGameButton = SwingComponentCreator.createButtonWithImage(BUTTON_EXIT, 370, 175, BUTTON_WIDTH, BUTTON_HEIGHT);

        // Create and add action listeners
        startGameButton.addActionListener(new StartButtonAction(this));
        ActionListener exitGameButtonAction = e -> System.exit(0);
        exitGameButton.addActionListener(exitGameButtonAction);

        // Set panel options and add components
        this.setSize(frame.getWidth(), frame.getHeight());
        this.setLayout(null);
        this.add(startGameButton);
        this.add(exitGameButton);

        // Add key listener to the frame
        frame.addKeyListener(new StartGameOnEnter());
    }

    /*
     * Key listener for the "START" button - pressing start button
     */
    private class StartButtonAction implements ActionListener {
        JPanel panel;

        public StartButtonAction(JPanel panel) {
            this.panel = panel;
        }

        // When the button is pressed (left-click)
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == startGameButton)
                // Player can only trigger action once
                startGameButton.removeActionListener(this);

                // Change the image
                setBackgroundImage(startMenuTransitionImage);

                // Set the route to PROLOGUE, so the next panel is the PROLOGUE panel
                app.setRoute(Route.PROLOGUE);

                // Set timer to change the game route after a short delay.
                Timer timer = new Timer(TRANSITION_DELAY, evt -> {
                    panel.remove(startGameButton);
                    panel.remove(exitGameButton);

                    app.update();
                });

                // Happens only once.
                timer.setRepeats(false);

                // Start timer - after time starts, the program will go into the prologue
                timer.start();
        }
    }

    /*
     * Key listener for the "START" button - pressing enter key
     */
    private class StartGameOnEnter implements KeyListener {
        // When enter key is pressed
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                // Perform click action on the start game button
                startGameButton.doClick();

                // Remove the key listener, so it only triggers once
                app.getFrame().removeKeyListener(this);
            }
        }

        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }
    }
}
