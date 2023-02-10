package com.lostinspace.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class Frame {
    private JFrame frame;
    private ImageIcon bgImage;
    private JLabel bgImageLabel;
    private JButton enterGameButton;

    public static void main(String[] args) {
        new Frame();
    }

    public Frame() {
        bgImage = new ImageIcon(this.getClass().getResource("/images_title/title_fly.gif"));
        bgImageLabel = new JLabel(bgImage);
        bgImageLabel.setSize(720,720);

        enterGameButton = new JButton("Enter Game"); // set text for "enter game" button
        enterGameButton.setBounds(310,550,100,50); // center button within the frame
        enterGameButton.addActionListener(new ActionListener() { // created an action listener when button is pushed
            @Override
            public void actionPerformed(ActionEvent e) { // shows a message when button has been clicked
                JOptionPane.showMessageDialog(null, "You are now entering the game.");
            }
        });

        bgImageLabel.add(enterGameButton);                                    // adds a button with display of "enter game"
        frame =new JFrame("Lost In Space");                         // set text title for gui frame
        frame.add(bgImageLabel);
        frame.setSize(736, 758);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);           // closes window and terminates
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        frame.setVisible(true);
    }
}
