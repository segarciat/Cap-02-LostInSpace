package com.lostinspace.view;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuPanel extends ImagePanel {

    public MenuPanel() {
        super();
    }

    // background transparent
    public static void main(String[] args) {
        JFrame frame = new JFrame("Menu Button");
        JButton exitButton = new JButton("Exit");
        JButton helpButton = new JButton("Help");

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        helpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame, "Eventually will show help text");
            }
        });

        frame.add(exitButton, BorderLayout.CENTER);
        frame.add(helpButton, BorderLayout.CENTER);
        frame.setLayout(new FlowLayout());
        frame.setSize(300, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}



    // Exit Button




    // Help Button
