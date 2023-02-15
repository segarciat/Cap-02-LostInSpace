package com.lostinspace.view;

import com.lostinspace.app.AppGUI;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class SwingComponentCreator {
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
        button.setBorder(BorderFactory.createEmptyBorder());               // remove borders
        button.setContentAreaFilled(false);                                // remove inside content of button

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
        ImageIcon imageIcon = new ImageIcon(Objects.requireNonNull(AppGUI.class.getResource(imageURL)));
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
}
