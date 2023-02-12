package com.lostinspace.model;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class LisGui {
    private JFrame frame;
    private ImageIcon bgImage;
    private JLabel label;
    private JButton enterGame;


    public static void main(String[] args) {
        new LisGui();

    }

    public LisGui() {

        bgImage = new ImageIcon(this.getClass().getResource("/images_title/title_fly.gif"));
        label = new JLabel(bgImage);
        label.setSize(720, 720);

        enterGame = new JButton("Enter Game"); // set text for "enter game" button
        enterGame.setBounds(310, 550, 100, 50); // center button within the frame
        enterGame.addKeyListener(new KeyListener() { // shows a message when enter key is pressed
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                }
                label.setVisible(false);
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        label.add(enterGame); // adds a button with display of "enter game"

        frame = new JFrame("Lost In Space"); // set text title for gui frame
        frame.add(label);
        frame.setSize(720, 720);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // closes window and terminates
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        frame.setVisible(true);
    }
}

