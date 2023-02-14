package com.lostinspace.view;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuPanel {

    public MenuPanel() {
//        super();
    }

    // background transparent

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        JPanel panel = new JPanel(new BorderLayout(8,8));

        // Exit Button
        JButton exitButton = new JButton("Exit");

        // Help Button
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
                JOptionPane.showMessageDialog(panel, "Eventually will show help text");
            }
        });

        panel.add(exitButton, BorderLayout.NORTH);
        panel.add(helpButton, BorderLayout.CENTER);
//        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
//        helpButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setBackground(new Color(0,0,0,65));
//        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        frame.add(panel);
        frame.setSize(100, 100);
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}









