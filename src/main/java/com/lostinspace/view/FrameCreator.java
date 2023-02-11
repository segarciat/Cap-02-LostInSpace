package com.lostinspace.view;

import javax.swing.*;
import java.util.Iterator;
import java.util.Objects;

class FrameCreator {
    public JLabel createBGImageLabel(ImageIcon bgImage) {
        // add image to label
        JLabel bgImageLabel = new JLabel(bgImage);
        bgImageLabel.setSize(720, 720);

        return bgImageLabel;
    }

    public Iterator<String> createInitialFullScreenText(JTextArea textArea, Iterator<String> iterator, Route route) {
        int lineLimit;

        if (route.getRoute().equals("Prologue")) {
            lineLimit = 13;
        } else {
            lineLimit = 26;
        }

        StringBuilder nextText = new StringBuilder();

        while (iterator.hasNext() && lineLimit > 0) {
            nextText.append(iterator.next()).append("\n");
            lineLimit--;
        }

        textArea.setText(nextText.toString());

        return iterator;
    }

    public void createSubsequentFullScreenText(JTextArea textArea, Iterator<String> iterator) {
        StringBuilder nextText = new StringBuilder();
        int lineLimit = 13;

        while (iterator.hasNext() && lineLimit > 0) {
            nextText.append(iterator.next()).append("\n");
            lineLimit--;
        }

        textArea.setText(nextText.toString());
    }

    public JButton createButtonWithText(String text, int x, int y, int w, int h) {
        JButton button = new JButton();                           // add image to button

        button.setBounds(x, y, w, h);                                      // set bounds (x, y, w, h)
        button.setBorder(BorderFactory.createEmptyBorder());               // remove borders
        button.setContentAreaFilled(false);                                // remove inside content of button

        return button;
    }

    public JButton createButtonWithImage(String imageURL, int x, int y, int w, int h) {
        ImageIcon imageIcon = new ImageIcon(Objects.requireNonNull(this.getClass().getResource(imageURL)));
        JButton button = new JButton(imageIcon);                           // add image to button

        button.setBounds(x, y, w, h);                                      // set bounds (x, y, w, h)
        button.setBorder(BorderFactory.createEmptyBorder());               // remove borders
        button.setContentAreaFilled(false);                                // remove inside content of button

        return button;
    }
}
