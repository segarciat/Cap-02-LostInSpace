package com.lostinspace.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * View that shows the introductory portion of the game in the frame, including prologue and instructions.
 */
public class IntroPanel extends ImagePanel {
    private static final String BACKGROUND_IMAGE = "/images_title/background.jpg";
    private static final String BUTTON_SKIP = "/images_title/skip.png";

    private static final int WINDOW_SIZE = 720;

    private static final int BUTTON_WIDTH = 250;
    private static final int BUTTON_HEIGHT = 40;

    // font size
    private static final Font MONOSPACE_PLAIN_MED = new Font("Monospaced", Font.PLAIN, 14);

    // font colors
    private static final Color COLOR_GREEN = new Color(76, 175, 82);

    private final JTextArea introTextArea;

    private final Iterator<String> introPageIterator;

    private final JButton skipButton;
    private final AppView app;

    public IntroPanel(AppView app) {
        super(BACKGROUND_IMAGE, app.getFrame().getWidth(), app.getFrame().getHeight());
        this.app = app;

        // Create skip button
        skipButton = SwingComponentCreator.createButtonWithImage(BUTTON_SKIP, 235, 40, BUTTON_WIDTH, BUTTON_HEIGHT);
        skipButton.addActionListener(new SkipIntroPage());

        // Customize text area.
        introPageIterator = introPagesIterator();
        introTextArea = SwingComponentCreator.createStyledTextArea(introPageIterator.next());

        // Set panel attributes
        this.setSize(WINDOW_SIZE, WINDOW_SIZE);
        this.setOpaque(false);
        this.setLayout(new GridBagLayout());

        // Add components to panel.
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(30, 0, 0, 0);
        this.add(skipButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(0,0,0,0);
        gbc.weighty = 1.0;
        this.add(introTextArea, gbc);

        app.getFrame().requestFocus();
    }

    /**
     * Splits text into pages that have at most linesPerPage lines in each page.
     *
     * @param text The String to split into pages.
     * @param linesPerPage The number of lines per page.
     * @return A list of strings, each having at most linesPerPage lines.
     */
    private java.util.List<String> splitIntoPages(String text, int linesPerPage) {
        java.util.List<String> pages = new ArrayList<>();

        String[] lines = text.split("\n");

        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= lines.length; i++) {
            sb.append(lines[i - 1]).append("\n");

            if (i % linesPerPage == 0) {
                pages.add(sb.toString());
                sb = new StringBuilder();
            }
        }

        if (sb.length() > 0)
            pages.add(sb.toString());

        return pages;
    }

    /**
     * Iterator with all the text from the introduction of the game.
     * @return Iterator for the pages.
     */
    public Iterator<String> introPagesIterator() {
        java.util.List<String> introPages = new ArrayList<>();

        java.util.List<String> prologuePages = splitIntoPages(app.getController().getPrologue(), 13);
        List<String> tutorialPages = splitIntoPages(app.getController().getTutorial(), 26);

        introPages.addAll(prologuePages);
        introPages.addAll(tutorialPages);

        return introPages.iterator();
    }

    public void allowPageSkipOnKeyPress() {
        app.getFrame().addKeyListener(new FlipPageOnKeyPress());
    }

    private class SkipIntroPage implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (introPageIterator.hasNext()) {
                introTextArea.setText(introPageIterator.next());
            } else {
                app.setRoute(Route.GAME);
                app.update();
            }
        }
    }

    /**
     * Action that allows updating the text in the intro panel by pressing a key, simulating flipping a page.
     */
    private class FlipPageOnKeyPress implements KeyListener {
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER)
                skipButton.doClick();

            if (!introPageIterator.hasNext() && Route.GAME.equals(app.getRoute())) {
                app.getFrame().removeKeyListener(this);
            }
        }

        @Override
        public void keyTyped(KeyEvent e) {}

        @Override
        public void keyReleased(KeyEvent e) {}
    }
}
