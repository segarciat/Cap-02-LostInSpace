package com.lostinspace.view;

import com.lostinspace.app.AppGUI;

import javax.swing.*;
import java.util.Objects;

class SwingComponentCreator {
    public static JLabel createBGImageLabel(ImageIcon bgImage) {
        // add image to label
        JLabel bgImageLabel = new JLabel(bgImage);
        bgImageLabel.setSize(720, 720);

        return bgImageLabel;
    }

    public static JButton createButtonWithText(String text, int x, int y, int w, int h) {
        JButton button = new JButton();                           // add image to button

        button.setBounds(x, y, w, h);                                      // set bounds (x, y, w, h)
        button.setBorder(BorderFactory.createEmptyBorder());               // remove borders
        button.setContentAreaFilled(false);                                // remove inside content of button

        return button;
    }

    public static JButton createButtonWithImage(String imageURL, int x, int y, int w, int h) {
        ImageIcon imageIcon = new ImageIcon(Objects.requireNonNull(AppGUI.class.getResource(imageURL)));
        JButton button = new JButton(imageIcon);                           // add image to button

        button.setBounds(x, y, w, h);                                      // set bounds (x, y, w, h)
        button.setBorder(BorderFactory.createEmptyBorder());               // remove borders
        button.setContentAreaFilled(false);                                // remove inside content of button

        return button;
    }
}
