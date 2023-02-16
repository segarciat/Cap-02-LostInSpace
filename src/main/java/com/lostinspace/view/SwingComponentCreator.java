package com.lostinspace.view;

import com.lostinspace.app.GUIApp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class SwingComponentCreator {
    private static final int WINDOW_SIZE = 720;
    private static final Font MONOSPACE_PLAIN_MED = new Font("Monospaced", Font.PLAIN, 14);
    private static final java.awt.Color COLOR_GREEN = new Color(76, 175, 82);

    /**
     * Creates a JButton with text and sets the bounds, sets an empty border, and removes the content area
     * @param text Text you want the JButton to have
     * @param x Sets the x-variable bound
     * @param y Sets the y-variable bound
     * @param w Sets the width bound of button
     * @param h Sets the height bound of button
     * @return JButton
     */
    public static JButton createButtonWithText(String text, int x, int y, int w, int h) {
        JButton button = createButtonWithText(text);                           // add image to button

        button.setBounds(x, y, w, h);                                      // set bounds (x, y, w, h)
        button.setFocusable(false);

        return button;
    }

    /**
     * Creates a JButton with an image and sets the bounds, sets an empty border, and removes the content area
     * @param imageURL String path of the image
     * @param x Sets the x-variable bound
     * @param y Sets the y-variable bound
     * @param w Sets the width bound of button
     * @param h Sets the height bound of button
     * @return JButton
     */
    public static JButton createButtonWithImage(String imageURL, int x, int y, int w, int h) {
        ImageIcon imageIcon = new ImageIcon(Objects.requireNonNull(AppView.class.getResource(imageURL)));
        JButton button = new JButton(imageIcon);                           // add image to button
        button.setFocusable(false);

        button.setBounds(x, y, w, h);                                      // set bounds (x, y, w, h)
        button.setBorder(BorderFactory.createEmptyBorder());               // remove borders
        button.setContentAreaFilled(false);                                // remove inside content of button

        return button;
    }

    /**
     * Creates a JButton with an image based on the Rectangle object coordinates (based on XML file)
     * @param imageURL String path of the image
     * @param r Rectangle object that specifies an area in a coordinate space (x space, y space, width, height)
     * @return JButton
     */
    public static JButton createButtonWithImage(String imageURL, Rectangle r) {
        return createButtonWithImage(imageURL, (int) r.getX(), (int) r.getY(), (int) r.getWidth(), (int) r.getHeight());
    }

    /**
     * Creates a button that will not be focusable.
     * @param text The text on the bottom.
     * @return The button created with the text on it.
     */
    public static JButton createButtonWithText(String text) {
        JButton button = new JButton(text);
        button.setFocusable(false);
        return button;
    }


    /**
     * Creates a text area with the theme font of the game.
     * It is non-focusable, editable, and cannot steal focus.
     *
     * @param text The initial text of the textarea.
     */
    public static JTextArea createStyledTextArea(String text) {
        JTextArea textArea = new JTextArea(text);                                // initial text
        textArea.setSize(WINDOW_SIZE, WINDOW_SIZE);                              // size
        textArea.setEditable(false);                                             // non-editable
        textArea.setFocusable(false);                                            // prevent from stealing focus on click
        textArea.setOpaque(false);                                               // no background
        textArea.setLineWrap(true);                                              // wrap lines
        textArea.setWrapStyleWord(true);                                         // wrap by word
        textArea.setFont(MONOSPACE_PLAIN_MED);                                   // font type
        textArea.setForeground(COLOR_GREEN);                                     // font color
        textArea.setMargin(new Insets(30, 80, 0, 80));    // margins
        return textArea;
    }

    /**
     * Create a restart and exit button for Help options area
     * @param app text within the options
     * @return button with text
     */

    public static JButton createRestartButton(AppView app) {
        JButton button = createButtonWithText("Restart");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GUIApp.main(null);
                app.getFrame().dispose();
            }
        });
        return button;
    }

    public static JButton createExitButton() {
        JButton button = createButtonWithText("Exit");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        return button;
    }
}
