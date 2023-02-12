package com.lostinspace.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

class TitlePanel extends ImagePanel {
    private static final String TITLE_SCREEN_IMAGE = "/images_title/title.gif";
    private static final String TITLE_SCREEN_IMAGE_FLY = "/images_title/title_fly.gif";

    private static final int BUTTON_WIDTH = 250;
    private static final int BUTTON_HEIGHT = 40;

    private static final String BUTTON_START = "/images_title/start.png";
    private static final String BUTTON_EXIT = "/images_title/exit.png";

    public static final int TRANSITION_DELAY = 2050;

    private final Image startMenuTransitionImage;
    JButton startGameButton;
    JButton exitGameButton;

    private final AppGUI app;

    public TitlePanel(AppGUI app, SwingComponentCreator frameCreator) {
        super(TITLE_SCREEN_IMAGE, app.getFrame().getWidth(), app.getFrame().getHeight());
        this.app = app;
        JFrame frame = app.getFrame();

        startMenuTransitionImage = new ImageIcon(Objects.requireNonNull(this.getClass().getResource(TITLE_SCREEN_IMAGE_FLY)))
                .getImage()
                .getScaledInstance(frame.getWidth(), frame.getHeight(), Image.SCALE_DEFAULT);

        // Create button to enter/exit game
        startGameButton = frameCreator.createButtonWithImage(BUTTON_START, 100, 175, BUTTON_WIDTH,
                BUTTON_HEIGHT);

        exitGameButton = frameCreator.createButtonWithImage(BUTTON_EXIT, 370, 175, BUTTON_WIDTH, BUTTON_HEIGHT);

        // Create and add action listeners
        startGameButton.addActionListener(new StartButtonAction(this));
        ActionListener exitGameButtonAction = e -> System.exit(0);
        exitGameButton.addActionListener(exitGameButtonAction);

        // Set panel options and add components.
        this.setSize(frame.getWidth(), frame.getHeight());
        this.setLayout(null);
        this.add(startGameButton);
        this.add(exitGameButton);
    }

    private class StartButtonAction implements ActionListener {
        JPanel panel;

        public StartButtonAction(JPanel panel) {
            this.panel = panel;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            // Change the image.
            setBackgroundImage(startMenuTransitionImage);

            // Set timer to change the game route after a short delay.
            Timer timer = new Timer(TRANSITION_DELAY, evt -> {
                app.setRoute(Route.PROLOGUE);

                panel.remove(startGameButton);
                panel.remove(exitGameButton);

                app.getFrame().requestFocus();

                app.execute();
            });

            // Happens only once.
            timer.setRepeats(false);

            // Start timer - After time starts, the program will go into the prologue.
            timer.start();
        }
    }
}
